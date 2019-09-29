package com.sudhirt.practice.batch.transaction.repository;

import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, Long> {
}