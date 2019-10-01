package com.sudhirt.practice.batch.transaction.repository;

import java.util.List;

import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntry, Long> {

	List<TransactionEntry> findByStatus(String status);

}