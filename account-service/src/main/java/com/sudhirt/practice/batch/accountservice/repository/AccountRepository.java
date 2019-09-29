package com.sudhirt.practice.batch.accountservice.repository;

import com.sudhirt.practice.batch.accountservice.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {

}