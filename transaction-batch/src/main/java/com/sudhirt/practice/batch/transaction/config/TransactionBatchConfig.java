package com.sudhirt.practice.batch.transaction.config;

import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;
import com.sudhirt.practice.batch.transaction.writer.TransactionEntryWriter;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class TransactionBatchConfig {

	@Autowired
	private TransactionEntryWriter transactionEntryWriter;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job transactionProcessingJob() {
		return jobBuilderFactory.get("transactionProcessingJob").start(importStep()).build();
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

}