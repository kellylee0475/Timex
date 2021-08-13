
package com.te.timex.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.te.timex.model.Expense;
import com.te.timex.model.ExpenseList;
import com.te.timex.model.ProjectTask;
import com.te.timex.model.User;
import com.te.timex.repository.ExpenseListRepository;
import com.te.timex.repository.ExpenseRepository;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.service.ExpenseListService;


@Controller
@RequestMapping("/expenses")
public class ExpensesController{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ExpenseRepository expenseRepository;
	
	@Autowired
	private ExpenseListRepository expenseListRepository;
	
	@Autowired
	private ExpenseListService expenseListService;
	
	int user_id;
	
	@GetMapping
	public String index(Model model,Authentication authentication) {
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
	//	List<ExpenseList> expenselist = expenseListService.findByUserId(user_id);
		List<ExpenseList> expenselist = expenseListRepository.findByUserId(user_id);
		//System.out.println("***************");
	//	System.out.println(expenselist);
		model.addAttribute(expenselist);
		return "expenses/expenses";
	}
	
	@GetMapping("/selectExpense")
	public ResponseEntity<List<Expense>> selectCategory() {
		System.out.println("selectExpense");
		List<Expense> expenses = expenseRepository.findAll();
		if (expenses.isEmpty()) {
			System.out.println("empty");
		}
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	@PostMapping("/saveExpenses")
	@ResponseBody
	public String saveExpenses(@RequestBody HashMap<String, Object> param,Authentication authentication){
	//	System.out.println("saveExpenses");
	//	System.out.println(param);		
	
	//	Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
	//	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	//	System.out.println(userDetails.getUsername());
	
		ExpenseList expenseList = new ExpenseList();
		DateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = simpleDateFormat.parse((String) param.get("date"));
			expenseList.setDate(date);
		} catch (ParseException e) {		
			e.printStackTrace();
		}

		int expense_id=Integer.parseInt((String) param.get("expense_id"));
		int project_Id=Integer.parseInt((String) param.get("project_Id"));
		
		//로그인한 정보로 email주소 가져온다
		String email = authentication.getName();
	
		expenseList.setExpenseId(expense_id);
		expenseList.setProjectId(project_Id);
		
		//로그인한  email정보로 user_id 가져온다
		User user= userRepository.findByEmail(email);
		expenseList.setUserId(user.getId());
		
		expenseListRepository.save(expenseList);

		String message = "success saving";
        
        return message;
		
		
	}

	
}