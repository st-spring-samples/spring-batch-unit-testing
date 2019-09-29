package com.sudhirt.practice.batch.accountservice.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account {

	@Id
	private String accountNumber;

	@Column(nullable = false)
	private Double balance;

	@Column(nullable = true)
	private LocalDate lastTransactionDate;

	@OneToMany(mappedBy = "account")
	private List<Transaction> transactions;

}