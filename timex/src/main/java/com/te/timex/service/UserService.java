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

		String encodedPassword = passwordEncoder.encode(user.getPassword());//password encoding and saved in database
		user.setPassword(encodedPassword);
		user.setEmail(user.getEmail());
		user.setFirstname(user.getFirstname());
		user.setLastname(user.getLastname());
		user.setEnabled(true);
		
		Role role = new Role();//to add the data in user_role at the same time
		role.setId(1);//default is role id 1(user)
		user.getRoles().add(role);
		
		return userRepository.save(user);
	}
		

}