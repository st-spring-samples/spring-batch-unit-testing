package com.sudhirt.practice.batch.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.sudhirt.practice.batch.accountservice.entity.Account;
import com.sudhirt.practice.batch.accountservice.repository.AccountRepository;
import com.sudhirt.practice.batch.accountservice.repository.TransactionRepository;
import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBatchTest
public class TransactionBatchTests {

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	private void createAccount(String... accountNumbers) {
		for (String accountNumber : accountNumbers) {
			Account account = Account.builder().accountNumber(accountNumber).balance(0d).build();
			accountRepository.save(account);
		}
	}

	@Test
	public void testJob() throws Exception {
		createAccount("1234567");
		createAccount("1234568");
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
		assertThat(transactionEntryRepository.count()).isEqualTo(9);
		assertThat(transactionRepository.count()).isEqualTo(9);
		assertThat(accountRepository.count()).isEqualTo(2);
	}

}