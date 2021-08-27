package com.te.timex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.te.timex.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);

	User findByResetpwtoken(String token);

	@Transactional
	@Modifying
	@Query(value = "update User u set u.firstname = ?1, u.lastname = ?2, u.email = ?3, u.address = ?4"
			+ ", u.zipcode = ?5,  u.memo = ?6 , u.city = ?7 where u.id = ?8")
	void setUserInfoById(String firstname, String lastname, String email, String address, int zipcode, String memo,
			String city, int id);

	@Transactional
	@Modifying
	@Query(value = "update User u set u.photo = ?1 ,u.photopath= ?2 where u.id = ?3")
	void setUserInfoById(String photo,String photopath, int id);

}