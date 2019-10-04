package com.sudhirt.practice.batch.transaction.policy;

import org.springframework.batch.core.step.skip.SkipPolicy;

public class TransactionEntrySkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) {
		return true;
	}

}