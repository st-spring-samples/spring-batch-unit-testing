package com.sudhirt.practice.batch.transaction.listener;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;
import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntryProcessListener implements ItemProcessListener<TransactionEntry, Transaction> {

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public void beforeProcess(TransactionEntry item) {
		item.setStatus("IN_PROGRESS");
		transactionEntryRepository.save(item);
	}

	@Override
	@Transactional(value = TxType.REQUIRES_NEW)
	public void afterProcess(TransactionEntry item, Transaction result) {
		item.setStatus(result == null ? "FAILURE" : "SUCCESS");
		transactionEntryRepository.save(item);
	}

	@Override
	public void onProcessError(TransactionEntry item, Exception e) {
	}

}