package com.te.timex.login.repository;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.te.timex.commons.TimexMap;



@Mapper
@Repository
public interface LoginRepository {
	
	TimexMap selectUser(String user_id,String user_passwd);
	
}
