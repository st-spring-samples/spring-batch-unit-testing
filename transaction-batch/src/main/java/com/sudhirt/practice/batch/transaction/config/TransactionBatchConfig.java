package com.sudhirt.practice.batch.transaction.config;

import java.util.HashMap;
import java.util.Map;

import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;
import com.sudhirt.practice.batch.transaction.processor.TransactionEntryProcessor;
import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;
import com.sudhirt.practice.batch.transaction.writer.TransactionEntryWriter;
import com.sudhirt.practice.batch.transaction.writer.TransactionWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Configuration
public class TransactionBatchConfig {

	@Autowired
	private TransactionEntryWriter transactionEntryWriter;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private TransactionEntryRepository transactionEntryRepository;

	@Autowired
	private TransactionEntryProcessor transactionEntryProcessor;

	@Autowired
	private TransactionWriter transactionWriter;

	@Bean
	public Job transactionProcessingJob() {
		return jobBuilderFactory.get("transactionProcessingJob").start(importStep()).next(processStep()).build();
	}

	@Bean
	public Step importStep() {
		return stepBuilderFactory.get("transactionImportStep").<TransactionEntry, TransactionEntry>chunk(1)
				.reader(transactionFileReader()).writer(transactionEntryWriter).build();
	}

	@Bean
	public FlatFileItemReader<TransactionEntry> transactionFileReader() {
		return new FlatFileItemReaderBuilder<TransactionEntry>().name("transactionReader")
				.resource(new ClassPathResource("transactions.csv")).delimited()
				.names(new String[] { "accountNo", "transactionType", "amount", "transactionDate" }).linesToSkip(1)
				.fieldSetMapper(new BeanWrapperFieldSetMapper<TransactionEntry>() {
					{
						setTargetType(TransactionEntry.class);
					}
				}).build();
	}

	@Bean
	public Step processStep() {
		return stepBuilderFactory.get("transactionProcessStep").<TransactionEntry, Transaction>chunk(1)
				.reader(transactionItemReader()).processor(transactionEntryProcessor).writer(transactionWriter).build();
	}

	@Bean
	public RepositoryItemReader<TransactionEntry> transactionItemReader() {
		RepositoryItemReader<TransactionEntry> reader = new RepositoryItemReader<>();
		reader.setRepository(transactionEntryRepository);
		reader.setMethodName("findAll");
		Map<String, Sort.Direction> sorts = new HashMap<>();
		sorts.put("id", Direction.ASC);
		reader.setSort(sorts);
		return reader;
	}

}