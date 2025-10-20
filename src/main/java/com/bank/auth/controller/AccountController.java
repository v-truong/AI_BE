package com.bank.auth.controller;

import com.bank.auth.dto.AccountDetailDto;
import com.bank.auth.dto.AccountListItemDto;
import com.bank.auth.dto.AccountListResponse;
import com.bank.auth.dto.BalanceDto;
import com.bank.auth.dto.TransactionListResponse;
import com.bank.auth.service.AccountService;
import com.bank.auth.service.AccountService.ResponseEntityWrapper;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    public AccountController(AccountService accountService){ this.accountService = accountService; }

    // List accounts (paged) - authenticated
    @GetMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountListResponse> listAccounts(
            Principal principal,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", required = false) String sort
    ){
        if(page > 0){
        page = page -1;
        }
        Pageable pageable = PageRequest.of(page, size);
        String userId = principal.getName();
        System.out.println("ssssssss=>"+userId);
        ResponseEntityWrapper<AccountListResponse> wrap = accountService.listAccounts(userId, pageable, sort);
        HttpHeaders headers = wrap.getHeaders();
        return new ResponseEntity<>(wrap.getBody(), headers, HttpStatus.OK);
    }

  
    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AccountDetailDto> getAccount(
            Principal principal,
            @PathVariable("id") Long id
    ){
        String userId = principal.getName();
        ResponseEntityWrapper<AccountDetailDto> wrap = accountService.getAccountDetail(id, userId);
        return new ResponseEntity<>(wrap.getBody(), wrap.getHeaders(), HttpStatus.OK);
    }

    // Balance
    // @GetMapping("/{id}/balance")
    // // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<BalanceDto> getBalance(Principal principal, @PathVariable("id") Long id){
    //     String userId = principal.getName();
    //     BalanceDto b = accountService.getBalance(id, userId);
    //     return ResponseEntity.ok(b);
    // }

    // Transaction history
    @GetMapping("/{id}/transactions")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionListResponse> transactions(
            Principal principal,
            @PathVariable("id") Long id,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ){
         if(page > 0){
        page = page -1;
        }
        String userId = principal.getName();
        Pageable pageable = PageRequest.of(page, size);
        ResponseEntityWrapper<TransactionListResponse> wrap = accountService.getTransactions(id, from, to, type, pageable, userId);
        return new ResponseEntity<>(wrap.getBody(), wrap.getHeaders(), HttpStatus.OK);
    }

    // Sync delta
    // @GetMapping("/sync")
    // // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<List<AccountListItemDto>> sync(@RequestParam("since") String since){
    //     List<AccountListItemDto> list = accountService.syncSince(since);
    //     return ResponseEntity.ok(list);
    // }

    // // Statement export
    // @GetMapping("/{id}/statements")
    // // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<byte[]> exportStatement(
    //         Principal principal,
    //         @PathVariable("id") Long id,
    //         @RequestParam(value = "format", defaultValue = "csv") String format,
    //         @RequestParam(value = "from", required = false) String from,
    //         @RequestParam(value = "to", required = false) String to
    // ){
    //     String userId = principal.getName();
    //     return accountService.exportStatement(id, format, from, to, userId);
    // }
}
