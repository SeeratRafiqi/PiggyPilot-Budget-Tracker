package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.Transaction;

import java.util.List;
import java.util.Optional;


public interface TransactionService {
    List<Transaction> listAllTransaction();
    Transaction addNewTransaction(Transaction transaction);
    Optional<Transaction> findTransactionById(Integer transaction_id);
    Transaction updateTransaction(Transaction transaction);
    void deleteTransaction(Transaction transaction);

    List<Transaction> listTransactionsByBudgetId(Long budgetId);

    void deleteTransactionById(Integer id);
}
