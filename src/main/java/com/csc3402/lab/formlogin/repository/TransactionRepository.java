package com.csc3402.lab.formlogin.repository;

import com.csc3402.lab.formlogin.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByGroupBudgetId(Long budgetId);

}
