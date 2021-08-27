
package com.te.timex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.te.timex.model.ExpenseList;
import com.te.timex.model.Timesheet;
import com.te.timex.repository.ExpenseListRepository;
import com.te.timex.repository.ExpenseRepository;
import com.te.timex.repository.ProjectRepository;
import com.te.timex.repository.ProjectTaskRepository;
import com.te.timex.repository.TimesheetRepository;
import com.te.timex.repository.UserRepository;
import com.te.timex.repository.WeekRepository;
import com.te.timex.service.ProjectService;

@Controller
@RequestMapping("/report")
public class ReportController{
	@Autowired
	private ExpenseRepository expenseRepository;

	@Autowired
	private ExpenseListRepository expenseListRepository;

	@Autowired
	private ProjectService projectService;
	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	@Autowired
	private TimesheetRepository timesheetRepository;
	@Autowired
	private UserRepository userRepository;

	int user_id;
	
	@GetMapping("/time")
	public String time(Authentication authentication, Model model) {
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		List<Timesheet> timesheetlist = timesheetRepository.findByUserId(user_id);
		model.addAttribute("timesheetlist", timesheetlist);
		System.out.println("***************!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(timesheetlist.toString());
		return "report/time";
	}

	@GetMapping("/expense")
	public String expense(Authentication authentication, Model model) {
		// 1. user정보가져오기
		Common common = new Common();
		user_id = common.getUserId(authentication, userRepository);
		List<ExpenseList> expenselist = expenseListRepository.findByUserId(user_id);
		model.addAttribute("expenselist", expenselist);
		System.out.println("***************!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println(expenselist.toString());
		return "report/expense";
	}


}