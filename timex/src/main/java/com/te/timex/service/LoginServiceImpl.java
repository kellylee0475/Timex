package com.te.timex.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.commons.TimexMap;
import com.te.timex.login.repository.LoginRepository;

@Service
public class LoginServiceImpl implements LoginService{
	@Autowired
	LoginRepository loginRepository;
	
	@Override
	public TimexMap login(String user_email, String user_password) {
		return loginRepository.selectUser(user_email, user_password);
		
	};
}