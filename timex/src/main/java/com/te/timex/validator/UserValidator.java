
package com.te.timex.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.te.timex.model.Board;
import com.te.timex.model.User;

@Component
public class UserValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		
		return Board.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		User u = (User) obj;
		/*
		 * if(StringUtils.isEmpty(u.getFirstname())) {
		 * errors.rejectValue("firstname","key","Please enter first name"); }
		 */
	}
	
	
}