package com.te.timex.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

	// TODO Auto-generated m
//	@Autowired
//	LoginService loginService;
//	
	// 로그인페이지
	@GetMapping
	public String login() {
		System.out.println("여기는 왔나?");
		return "login/login";
	}
		


}
