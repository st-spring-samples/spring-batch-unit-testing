package com.sudhirt.practice.batch.accountservice.service;

import javax.transaction.Transactional;

import com.sudhirt.practice.batch.accountservice.constants.TransactionType;
import com.sudhirt.practice.batch.accountservice.entity.Account;
import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.accountservice.exception.AccountNotFoundException;
import com.sudhirt.practice.batch.accountservice.exception.InsufficientBalanceException;
import com.sudhirt.practice.batch.accountservice.repository.AccountRepository;
import com.sudhirt.practice.batch.accountservice.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

	private TransactionRepository transactionRepository;

	private AccountRepository accountRepository;

	@Autowired
	AccountService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
		this.transactionRepository = transactionRepository;
		this.accountRepository = accountRepository;
	}

	public Account get(String accountNumber) {
		return accountRepository.findById(accountNumber).orElseThrow(AccountNotFoundException::new);
	}

	@Transactional
	public void addTransaction(Transaction transaction) {
		var account = get(transaction.getAccount().getAccountNumber());
		transactionRepository.save(transaction);
		if (transaction.getTransactionType() == TransactionType.CREDIT) {
			credit(account, transaction.getTransactionAmount());
		}
		else if (transaction.getTransactionType() == TransactionType.DEBIT) {
			debit(account, transaction.getTransactionAmount());
		}
		account.setLastTransactionDate(transaction.getTransactionDate());
		accountRepository.save(account);
	}

	private void credit(Account account, Double creditAmount) {
		account.setBalance(account.getBalance() + creditAmount);
	}

	private void debit(Account account, Double debitAmount) {
		if (account.getBalance() >= debitAmount) {
			account.setBalance(account.getBalance() - debitAmount);
		}
		else {
			throw new InsufficientBalanceException("Insufficient Balance");
		}
	}

}