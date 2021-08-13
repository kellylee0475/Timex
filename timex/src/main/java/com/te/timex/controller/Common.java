
package com.te.timex.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;

@Component
public class Common{
	
	
	//private UserRepository userRepository;

	public int getUserId(Authentication authentication,UserRepository userRepository) {
	
		//로그인한 정보로 email주소 가져온다
		String email = authentication.getName();

		//로그인한  email정보로 user_id 가져온다
		User user= userRepository.findByEmail(email);
		int user_id=user.getId();
		
		return user_id;		
	}
}