package com.sudhirt.practice.batch.accountservice.exception;

public class AccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -254360863550452659L;

	public AccountNotFoundException() {
		super("Account not found");
	}

}