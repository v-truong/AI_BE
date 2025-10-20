package com.bank.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.auth.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Page<Account> findByUserId(String userId, Pageable pageable);
    Page<Account> findAll(Pageable pageable);
    List<Account> findByUpdatedAtGreaterThan(String since);
}
