
package com.te.timex.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.te.timex.model.Board;

@Component
public class BoardValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		
		return Board.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		// TODO Auto-generated method stub
		Board b = (Board) obj;
		if(StringUtils.isEmpty(b.getContent())) {
			errors.rejectValue("content","key","add content");
		}
	}
	
	
}