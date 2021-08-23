package com.te.timex.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	
	
	User findByResetpwtoken(String token);
}