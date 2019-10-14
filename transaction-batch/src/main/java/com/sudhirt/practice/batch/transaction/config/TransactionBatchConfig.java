package com.sudhirt.practice.batch.transaction.config;

import java.util.HashMap;
import java.util.List;
import com.sudhirt.practice.batch.accountservice.entity.Transaction;
import com.sudhirt.practice.batch.transaction.entity.TransactionEntry;
import com.sudhirt.practice.batch.transaction.listener.StepListener;
import com.sudhirt.practice.batch.transaction.listener.TransactionEntryProcessListener;
import com.sudhirt.practice.batch.transaction.policy.TransactionEntrySkipPolicy;
import com.sudhirt.practice.batch.transaction.processor.TransactionEntryProcessor;
import com.sudhirt.practice.batch.transaction.repository.TransactionEntryRepository;
import com.sudhirt.practice.batch.transaction.writer.TransactionEntryWriter;
import com.sudhirt.practice.batch.transaction.writer.TransactionWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@Configuration
public class TransactionBatchConfig {

	private static final String OVERRIDDEN_BY_EXPRESSION = null;

	private TransactionEntryWriter transactionEntryWriter;

	private TransactionEntryRepository transactionEntryRepository;

	private TransactionEntryProcessor transactionEntryProcessor;

	private TransactionEntryProcessListener transactionEntryProcessListener;

	private TransactionWriter transactionWriter;

	@Autowired
	public TransactionBatchConfig(TransactionEntryProcessor transactionEntryProcessor,
			TransactionEntryProcessListener transactionEntryProcessListener,
			TransactionEntryWriter transactionEntryWriter, TransactionWriter transactionWriter,
			TransactionEntryRepository transactionEntryRepository) {
		this.transactionEntryProcessor = transactionEntryProcessor;
		this.transactionEntryProcessListener = transactionEntryProcessListener;
		this.transactionEntryWriter = transactionEntryWriter;
		this.transactionWriter = transactionWriter;
		this.transactionEntryRepository = transactionEntryRepository;
	}

	@Bean
	public Job transactionProcessingJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("transactionProcessingJob").incrementer(new RunIdIncrementer())
				.flow(importStep(stepBuilderFactory)).next(processStep(stepBuilderFactory)).end().build();
	}

	@Bean
	public Step importStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("transactionImportStep").<TransactionEntry, TransactionEntry>chunk(1)
				.reader(transactionFileReader(OVERRIDDEN_BY_EXPRESSION)).writer(transactionEntryWriter).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<TransactionEntry> transactionFileReader(
			@Value("#{jobParameters[pathToTransactionFile]}") String pathToTransactionFile) {
		var fieldSetMapper = new BeanWrapperFieldSetMapper<TransactionEntry>();
		fieldSetMapper.setTargetType(TransactionEntry.class);
		return new FlatFileItemReaderBuilder<TransactionEntry>().name("transactionReader")
				.resource(new ClassPathResource(pathToTransactionFile)).delimited()
				.names(new String[] { "accountNo", "transactionType", "amount", "transactionDate" }).linesToSkip(1)
				.fieldSetMapper(fieldSetMapper).build();
	}

	@Bean
	public Step processStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("transactionProcessStep").<TransactionEntry, Transaction>chunk(1)
				.reader(transactionItemReader()).processor(transactionEntryProcessor)
				.listener(transactionEntryProcessListener).writer(transactionWriter).faultTolerant()
				.skip(Exception.class).skipPolicy(new TransactionEntrySkipPolicy()).listener(stepListener())
				.allowStartIfComplete(true).build();
	}

	@Bean
	public RepositoryItemReader<TransactionEntry> transactionItemReader() {
		var reader = new RepositoryItemReader<TransactionEntry>();
		reader.setRepository(transactionEntryRepository);
		reader.setMethodName("findByStatusIn");
		reader.setArguments(List.of(List.of("NEW", "IN_PROGRESS", "FAILURE")));
		var sorts = new HashMap<String, Sort.Direction>();
		sorts.put("id", Direction.ASC);
		reader.setSort(sorts);
		return reader;
	}

	@Bean
	public StepListener stepListener() {
		return new StepListener();
	}

}