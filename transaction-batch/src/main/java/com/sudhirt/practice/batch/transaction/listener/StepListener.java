package com.sudhirt.practice.batch.transaction.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;

public class StepListener extends StepExecutionListenerSupport {

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getFilterCount() > 0 || stepExecution.getSkipCount() > 0) {
			return ExitStatus.FAILED;
		}
		return null;
	}

}