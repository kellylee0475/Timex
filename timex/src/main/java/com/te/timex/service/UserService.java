package com.te.timex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.te.timex.model.Role;
import com.te.timex.model.User;
import com.te.timex.repository.UserRepository;

import javassist.NotFoundException;

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
	
	
	 public void updateResetPasswordToken(String token, String email) throws NotFoundException {
	        User user = userRepository.findByEmail(email);
	        if (user != null) {
	        	user.setResetpwtoken(token);
	        	userRepository.save(user);
	        } else {
	            throw new NotFoundException("Could not find any customer with the email " + email);
	        }
	    }
	     
	    public User getByResetPasswordToken(String token) {
	        return userRepository.findByResetpwtoken(token);
	    }
	     
	    public void updatePassword(User user, String newPassword) {
	        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	        String encodedPassword = passwordEncoder.encode(newPassword);
	        user.setPassword(encodedPassword);	         
	        user.setResetpwtoken(null);
	        userRepository.save(user);
	    }

}

