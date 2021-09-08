package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Project;


public interface ProjectRepository extends JpaRepository<Project, Integer> {
		
	List<Project> findByNumber(String number);

}