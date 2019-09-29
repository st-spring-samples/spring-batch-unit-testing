package com.sudhirt.practice.batch.transaction.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TransactionEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_entry_seq")
    private Long id;
    private String accountNo;
    private String transactionType;
    private String amount;
    private String transactionDate;
}