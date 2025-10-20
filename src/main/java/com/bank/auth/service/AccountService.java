package com.bank.auth.service;

import com.bank.auth.dto.*;
import com.bank.auth.dto.AccountDetailDto;
import com.bank.auth.dto.AccountListItemDto;
import com.bank.auth.dto.AccountListResponse;
import com.bank.auth.dto.BalanceDto;
import com.bank.auth.dto.TransactionDto;
import com.bank.auth.dto.TransactionListResponse;
import com.bank.auth.entity.Account;
import com.bank.auth.entity.Transaction;
import com.bank.auth.repository.AccountRepository;
import com.bank.auth.repository.TransactionRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    // simple in-memory rate limiter (per-user per-endpoint)
    private final Map<String, Long> rateLimitMap = new ConcurrentHashMap<>();

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository){
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public ResponseEntityWrapper<AccountListResponse> listAccounts(String userId, Pageable pageable, String sort){
        Page<Account> page = accountRepository.findByUserId(userId, pageable);
          String now = DateTimeFormatter.ISO_INSTANT.format(Instant.now());
        // Account acc1 = new Account();
        //     acc1.setUserId(userId);
        //     acc1.setAccountNumber("100000004");
        //     acc1.setOwnerName("Nguyen Van A");
        //     acc1.setCurrency("VND");
        //     acc1.setBalance(5_000_000.0);
        //     acc1.setCreatedAt(now);
        //     acc1.setUpdatedAt(now);

        //     Account acc2 = new Account();
        //     acc2.setUserId(userId);
        //     acc2.setAccountNumber("100000005");
        //     acc2.setOwnerName("Tran Thi B");
        //     acc2.setCurrency("USD");
        //     acc2.setBalance(2_000.0);
        //     acc2.setCreatedAt(now);
        //     acc2.setUpdatedAt(now);

        //     Account acc3 = new Account();
        //     acc3.setUserId(userId);
        //     acc3.setAccountNumber("100000006");
        //     acc3.setOwnerName("Le Van C");
        //     acc3.setCurrency("VND");
        //     acc3.setBalance(1_200_000.0);
        //     acc3.setCreatedAt(now);
        //     acc3.setUpdatedAt(now);

        //     accountRepository.save(acc1);
        //     accountRepository.save(acc2);
        //     accountRepository.save(acc3);
        List<AccountListItemDto> items = page.stream()
                .map(a -> new AccountListItemDto(a.getId(), a.getAccountNumber(), a.getOwnerName(), a.getCurrency()))
                .collect(Collectors.toList());

        AccountListResponse resp = new AccountListResponse(items, page.getTotalElements(), page.getNumber(), page.getSize());
        // ETag based on content hash
        String etag = generateEtag(items);
        HttpHeaders headers = new HttpHeaders();
        headers.setETag(etag);
        return new ResponseEntityWrapper<>(resp, headers);
    }

    public ResponseEntityWrapper<AccountDetailDto> getAccountDetail(Long accountId, String userId){
        Account acct = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        ensureOwnerOrAdmin(userId, acct.getUserId());
        AccountDetailDto dto = new AccountDetailDto();
        dto.setId(acct.getId());
        dto.setAccountNumber(acct.getAccountNumber());
        dto.setOwnerName(acct.getOwnerName());
        dto.setCurrency(acct.getCurrency());
        dto.setBalance(acct.getBalance());
        dto.setUpdatedAt(acct.getUpdatedAt());

        HttpHeaders headers = new HttpHeaders();
        if (acct.getUpdatedAt() != null) {
            headers.set("Last-Modified", acct.getUpdatedAt());
            headers.setETag(generateEtag(acct.getUpdatedAt()));
        }
        return new ResponseEntityWrapper<>(dto, headers);
    }

    public BalanceDto getBalance(Long accountId, String userId){
        Account acct = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        ensureOwnerOrAdmin(userId, acct.getUserId());
        return new BalanceDto(acct.getBalance(), acct.getCurrency(), acct.getUpdatedAt());
    }

    public ResponseEntityWrapper<TransactionListResponse> getTransactions(Long accountId, String from, String to, String type, Pageable pageable, String userId){
        // rate-limit check (simple): allow one call per 2 seconds per user
        // List<Account> accounts = accountRepository.findAll();

        // for (Account account : accounts) {
        //     for (int i = 0; i < 5; i++) {
        //         Transaction tran = new Transaction();
        //         tran.setAccountId(account.getId().toString());
        //         Random random = new Random();
        //         tran.setAmount(BigDecimal.valueOf(random.nextInt(1_000_000) + 1).doubleValue()); // random 1-1tr
        //         tran.setType(random.nextBoolean() ? "CREDIT" : "DEBIT");
        //         tran.setDescription("Mock transaction " + (i + 1));
        //         tran.setTimestamp(LocalDateTime.now().minusDays(random.nextInt(30)).now().toString());

        //         transactionRepository.save(tran);
        //     }
        // }
        String key = "transactions:" + userId;
        long now = System.currentTimeMillis();
        if (rateLimitMap.containsKey(key) && now - rateLimitMap.get(key) < 2000) {
            throw new RuntimeException("Too many requests - rate limited");
        }
        rateLimitMap.put(key, now);

        Account acct = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        ensureOwnerOrAdmin(userId, acct.getUserId());

        Page<Transaction> page;
        if (from != null && to != null && type != null) {
            page = transactionRepository.findByAccountIdAndTimestampBetweenAndType(accountId.toString(), from, to, type, pageable);
        } else if (from != null && to != null) {
            page = transactionRepository.findByAccountIdAndTimestampBetween(accountId.toString(), from, to, pageable);
        } else {
            page = transactionRepository.findByAccountId(accountId.toString(), pageable);
        }

        List<TransactionDto> items = page.stream()
                .map(t -> new TransactionDto(t.getId(), t.getType(), t.getAmount(), t.getDescription(), t.getTimestamp()))
                .collect(Collectors.toList());

        TransactionListResponse resp = new TransactionListResponse(items, page.getTotalElements(), page.getNumber());
        HttpHeaders headers = new HttpHeaders();
        // last-modified based on most recent transaction timestamp
        Optional<String> last = items.stream().map(TransactionDto::getTimestamp).max(String::compareTo);
        last.ifPresent(ts -> {
            headers.set("Last-Modified", ts);
            headers.set("Cache-Control", "no-cache");
        });
        return new ResponseEntityWrapper<>(resp, headers);
    }

    @Transactional(readOnly = true)
    public List<AccountListItemDto> syncSince(String sinceTimestamp){
        List<Account> list = accountRepository.findByUpdatedAtGreaterThan(sinceTimestamp);
        return list.stream()
                .map(a -> new AccountListItemDto(a.getId(), a.getAccountNumber(), a.getOwnerName(), a.getCurrency()))
                .collect(Collectors.toList());
    }

    public ResponseEntity<byte[]> exportStatement(Long accountId, String format, String from, String to, String userId){
        // rate limit for export
        String key = "export:" + userId;
        long now = System.currentTimeMillis();
        if (rateLimitMap.containsKey(key) && now - rateLimitMap.get(key) < 5000) {
            throw new RuntimeException("Too many requests - rate limited");
        }
        rateLimitMap.put(key, now);

        Account acct = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        ensureOwnerOrAdmin(userId, acct.getUserId());

        List<Transaction> txs;
        if (from != null && to != null) {
            txs = transactionRepository.findByAccountIdAndTimestampBetween(accountId.toString(), from, to);
        } else {
            txs = transactionRepository.findByAccountIdAndTimestampBetween(accountId.toString(), "1970-01-01T00:00:00Z", Instant.now().toString());
        }

        if ("csv".equalsIgnoreCase(format)) {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,amount,description,timestamp\n");
            for (Transaction t : txs) {
                sb.append(t.getId()).append(",")
                        .append(t.getType()).append(",")
                        .append(t.getAmount()).append(",\"")
                        .append(t.getDescription() == null ? "" : t.getDescription().replace("\"", "\"\"")).append("\",")
                        .append(t.getTimestamp()).append("\n");
            }
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment; filename=statement-" + accountId + ".csv");
            headers.setContentLength(data.length);
            headers.set("Content-Type", "text/csv; charset=UTF-8");
            return new ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
        } else if ("pdf".equalsIgnoreCase(format)) {
            // Simple fallback: return CSV bytes with pdf mime (for demo). In production generate real PDF.
            StringBuilder sb = new StringBuilder();
            sb.append("ID  Type  Amount  Description  Timestamp\n");
            for (Transaction t : txs) {
                sb.append(t.getId()).append(" ").append(t.getType()).append(" ").append(t.getAmount()).append(" ").append(t.getTimestamp()).append("\n");
            }
            byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Disposition", "attachment; filename=statement-" + accountId + ".pdf");
            headers.setContentLength(data.length);
            headers.set("Content-Type", "application/pdf");
            return new ResponseEntity<>(data, headers, org.springframework.http.HttpStatus.OK);
        } else {
            throw new RuntimeException("Unsupported format");
        }
    }

    private String generateEtag(Object o){
        int h = Objects.hashCode(o);
        return "\"" + Integer.toHexString(h) + "\"";
    }

    private void ensureOwnerOrAdmin(String requesterUserId, String ownerUserId) {
        // simple check: requesterUserId equals ownerUserId OR caller has admin role is assumed done at controller level.
        if (requesterUserId == null) throw new RuntimeException("Unauthenticated");
        if (!requesterUserId.equals(ownerUserId)) {
            // production: check role ADMIN via SecurityContext; here we simply throw
            throw new RuntimeException("Forbidden - not owner");
        }
    }

    // wrapper for returning DTO + headers
    public static class ResponseEntityWrapper<T> {
        private final T body;
        private final HttpHeaders headers;
        public ResponseEntityWrapper(T body, HttpHeaders headers){
            this.body = body; this.headers = headers;
        }
        public T getBody(){ return body; }
        public HttpHeaders getHeaders(){ return headers; }
    }
}
