package com.sudhirt.practice.batch.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TransactionBatchApplication.class, TransactionBatchTests.TestConfig.class })
public class TransactionBatchTests {

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertThat("COMPLETED").isEqualTo(jobExecution.getExitStatus().getExitCode());
		assertThat(9).isEqualTo(transactionEntryRepository.count());
	}

	@Configuration
	static class TestConfig {

		@Bean
		JobLauncherTestUtils jobLauncherTestUtils() {
			return new JobLauncherTestUtils();
		}

	}

}