package com.te.timex.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.model.ExpenseList;
import com.te.timex.repository.ExpenseListRepository;

@Service
public class ExpenseListService{
	@Autowired
	private ExpenseListRepository expenseListRepository;

	/*
	 * public ArrayList<ExpenseList> findByUserId(int userId) {
	 * ArrayList<ExpenseList> expenseList =
	 * expenseListRepository.findByUserId(userId);
	 * 
	 * 
	 * return expenseList;
	 * 
	 * 
	 * }
	 */
	
	/*
	 * public ExpenseList save(String email, ExpenseList expenseList) { // User user
	 * = userRepository.findByEmail(email); // board.setUser(user); return
	 * expenseListRepository.save(expenseList); }
	 */
	
}