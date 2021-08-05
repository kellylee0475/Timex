package com.te.timex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.te.timex.model.Role;
import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;

@Service
public class UserService{
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User save(User user) {
		System.out.println("USer SErvice");
		System.out.println(user.getId());
		System.out.println(user.getEmail());
		System.out.println(user.getPassword());
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());//password encoding
		user.setPassword(encodedPassword);
		user.setEmail(user.getEmail());
		user.setFirstname(user.getFirstname());
		user.setLastname(user.getLastname());
		
		Role role = new Role();//role table에 함께 입력
		System.out.println("hi????");
		role.setId(1);
		user.getRoles().add(role);
		
		System.out.println("woo????");
		return userRepository.save(user);
	}
		

}