
package com.te.timex.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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
	
		//@Query("SELECT p from POSTS p inner join p.user u where u.username=?1")
	//	System.out.println("***************1");
	//	System.out.println(user_id);

		List<ExpenseList> expenselist = expenseListRepository.findByUserId(user_id);
	//	System.out.println("***************2");
	
	//	System.out.println(expenselist.toString());
	//	System.out.println("***************3");
		model.addAttribute("expenselist",expenselist);
		
		//System.out.println(expenselist);
		
		
	//	Query query = 
	//			em.createQuery("SELECT m.userName, m.age FROM Member m");//(2)

	   
	     
		return "expenses/expenses";
	}
	
	@GetMapping("/selectExpense")
	public ResponseEntity<List<Expense>> selectCategory() {
	//	System.out.println("selectExpense");
		List<Expense> expenses = expenseRepository.findAll();
		if (expenses.isEmpty()) {
			System.out.println("empty");
		}
		//System.out.println("here!!!!!!!!");
		//System.out.println(expenses.toString());
		return new ResponseEntity<List<Expense>>(expenses, HttpStatus.OK);
	}

	
	
	@GetMapping("/selectExpenseDetail")
	public ResponseEntity selectExpenseDetail(int expense_id) {
	//System.out.println(expense_id);
		Optional<Expense> expenseDetail = expenseRepository.findById(expense_id);
		
	//	System.out.println(expenseDetail.toString());
		return new ResponseEntity(expenseDetail,HttpStatus.OK);
	}

	
	
	
	@PostMapping("/saveExpenses")
	@ResponseBody
	public String saveExpenses(@RequestBody HashMap<String, Object> param,Authentication authentication){
		System.out.println("saveExpenses");
		System.out.println(param);		
	
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

		int project_id=Integer.parseInt((String) param.get("project_id"));

		user_id=(int) param.get("user_id");
		int qty = Integer.parseInt((String) param.get("qty"));
		String total_amount = (String)param.get("total_amount");
		expenseList.setExpenseId(expense_id);
		expenseList.setProjectId(project_id);
		expenseList.setUserId(user_id);
		expenseList.setStatus(2);
		expenseList.setQty(qty);
		expenseList.setTotal_amount(total_amount);


		expenseListRepository.save(expenseList);

		String message = "success saving";
        
        return message;
		
		
	}

	
}