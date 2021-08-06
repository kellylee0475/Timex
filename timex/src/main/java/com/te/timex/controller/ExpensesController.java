
package com.te.timex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/expenses")
public class ExpensesController{

	
	@GetMapping
	public String index() {
		return "expenses/expenses";
	}
	

}