package com.te.timex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Expense;


public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
	

}