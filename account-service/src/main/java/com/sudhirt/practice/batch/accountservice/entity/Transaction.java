package com.sudhirt.practice.batch.accountservice.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sudhirt.practice.batch.accountservice.constants.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
	private Long id;

	private LocalDate transactionDate;

	private Double transactionAmount;

	@Enumerated(EnumType.STRING)
	private TransactionType transactionType;

	@ManyToOne
	@JoinColumn(name = "account_number")
	private Account account;

}