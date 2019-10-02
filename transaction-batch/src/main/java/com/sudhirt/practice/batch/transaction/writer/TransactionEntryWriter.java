package com.sudhirt.practice.batch.transaction.writer;

import java.util.List;

import javax.transaction.Transactional;

import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;
import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntryWriter implements ItemWriter<TransactionEntry> {

	private TransactionEntryRepository repository;

	@Autowired
	public TransactionEntryWriter(TransactionEntryRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional
	public void write(List<? extends TransactionEntry> items) throws Exception {
		items.forEach(item -> item.setStatus("NEW"));
		repository.saveAll(items);
	}

}