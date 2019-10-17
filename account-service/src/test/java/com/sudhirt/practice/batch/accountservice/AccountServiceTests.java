package com.sudhirt.practice.batch.accountservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import java.time.LocalDate;
import javax.transaction.Transactional;
import com.sudhirt.practice.batch.accountservice.constants.TransactionType;
import com.sudhirt.practice.batch.accountservice.entity.Account;
import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.accountservice.exception.AccountNotFoundException;
import com.sudhirt.practice.batch.accountservice.exception.InsufficientBalanceException;
import com.sudhirt.practice.batch.accountservice.repository.AccountRepository;
import com.sudhirt.practice.batch.accountservice.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class AccountServiceTests {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	private Account createAccount(Double balance) {
		var account = Account.builder().accountNumber("123456").balance(balance).build();
		return accountRepository.save(account);
	}

	@Test
	public void account_balance_should_be_increased_when_credit_transaction_is_added() {
		createAccount(0d);
		var transaction = Transaction.builder().account(Account.builder().accountNumber("123456").build())
				.transactionType(TransactionType.CREDIT).transactionAmount(100d).transactionDate(LocalDate.now())
				.build();
		accountService.addTransaction(transaction);
		var account = accountRepository.getOne("123456");
		assertThat(account.getBalance()).isEqualTo(100d);
	}

	@Test
	public void account_balance_should_be_decreased_when_credit_transaction_is_added() {
		createAccount(100d);
		var transaction = Transaction.builder().account(Account.builder().accountNumber("123456").build())
				.transactionType(TransactionType.DEBIT).transactionAmount(100d).transactionDate(LocalDate.now())
				.build();
		accountService.addTransaction(transaction);
		var account = accountRepository.getOne("123456");
		assertThat(account.getBalance()).isEqualTo(0d);
	}

	@Test
	public void should_throw_InsufficientBalanceException_when_debit_amount_is_more_than_balance() {
		createAccount(100d);
		var transaction = Transaction.builder().account(Account.builder().accountNumber("123456").build())
				.transactionType(TransactionType.DEBIT).transactionAmount(101d).transactionDate(LocalDate.now())
				.build();
		assertThatExceptionOfType(InsufficientBalanceException.class)
				.isThrownBy(() -> accountService.addTransaction(transaction)).withMessage("Insufficient Balance");
	}

	@Test
	public void should_throw_AccountNotFoundException_when_account_does_not_exist() {
		var transaction = Transaction.builder().account(Account.builder().accountNumber("234567").build())
				.transactionType(TransactionType.DEBIT).transactionAmount(101d).transactionDate(LocalDate.now())
				.build();
		assertThatExceptionOfType(AccountNotFoundException.class)
				.isThrownBy(() -> accountService.addTransaction(transaction)).withMessage("Account not found");
	}

}