package com.bank.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.auth.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByAccountIdAndTimestampBetweenAndType(String accountId, String from, String to, String type, Pageable pageable);
    Page<Transaction> findByAccountIdAndTimestampBetween(String accountId, String from, String to, Pageable pageable);
    Page<Transaction> findByAccountId(String accountId, Pageable pageable);
    List<Transaction> findByAccountIdAndTimestampBetween(String accountId, String from, String to);
}
