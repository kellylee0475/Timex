package com.te.timex.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.commons.TimexMap;
import com.te.timex.login.repository.LoginRepository;

public interface LoginService{
		
	TimexMap login(String user_id, String user_password);
}