package com.sudhirt.practice.batch.transaction.writer;

import java.util.List;

import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.accountservice.service.AccountService;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionWriter implements ItemWriter<Transaction> {

	@Autowired
	private AccountService accountService;

	@Override
	public void write(List<? extends Transaction> items) throws Exception {
		items.forEach(accountService::addTransaction);
	}

}