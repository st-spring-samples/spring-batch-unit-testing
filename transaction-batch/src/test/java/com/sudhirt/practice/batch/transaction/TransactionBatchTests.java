package com.sudhirt.practice.batch.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import javax.transaction.Transactional;

import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TransactionBatchTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;

	@Test
	public void testJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
		assertThat(10).isEqualTo(transactionEntryRepository.count());
	}

}