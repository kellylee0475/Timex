
package com.te.timex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.te.timex.model.User;
import com.te.timex.service.UserService;


@Controller
@RequestMapping("/account")
public class AccountController{
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login() {
		return "account/login";//login 페이지로 이동
	}
	
	@GetMapping("/register")
	public String register() {

		return "account/register";//register페이지로 이동
	}
	
	
	@PostMapping("/register")
	public String register(User user) {
		System.out.println("here");
		System.out.println(user);
		userService.save(user);
		return "redirect:/";
	}
}