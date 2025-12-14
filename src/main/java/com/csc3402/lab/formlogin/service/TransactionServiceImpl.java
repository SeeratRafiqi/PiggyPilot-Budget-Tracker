package com.csc3402.lab.formlogin.service;

import com.csc3402.lab.formlogin.model.Transaction;
import com.csc3402.lab.formlogin.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

//    @Autowired
//    private BudgetRepository budgetRepository;
//
//    public Transaction createTransaction(Transaction transaction) {
//        // Validate that the budget exists
//        Budget budget = budgetRepository.findById(transaction.getBudget().getBudgetId())
//                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
//
//        // Set the transaction's budget to the found budget
//        transaction.setBudget(budget);
//
//        return transactionRepository.save(transaction);
//    }

    public TransactionServiceImpl(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Transaction> listAllTransaction() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction addNewTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> findTransactionById(Integer transaction_id) {
        return transactionRepository.findById(transaction_id);
    }
    public void deleteTransactionById(Integer id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public List<Transaction> listTransactionsByBudgetId(Long budgetId) {
        return transactionRepository.findByGroupBudgetId(budgetId);
    }
}
