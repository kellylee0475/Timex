package com.te.timex.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.ExpenseList;
import com.te.timex.model.ProjectTask;


public interface ExpenseListRepository extends JpaRepository<ExpenseList, Integer> {

	ExpenseList save(List<ExpenseList> expenseList);

	List<ExpenseList> findByUserId(int user_id);
	

}