
package com.te.timex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;
import com.te.timex.service.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/login")
	public String login() {
		return "account/login";// login 페이지로 이동
	}

	@PostMapping("/loginInfo")
	public ResponseEntity loginInfo(Authentication authentication) {

		//로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		//로그인한  email정보로 user_id 가져온다
		User user= userRepository.findByEmail(email);
		int user_id=user.getId();
			
		return new ResponseEntity(user_id, HttpStatus.OK);

	}

	@GetMapping("/register")
	public String register() {

		return "account/register";// register페이지로 이동
	}

	@PostMapping("/register")
	public String register(User user) {
		System.out.println("here");
		System.out.println(user);
		userService.save(user);
		return "redirect:/";
	}
}