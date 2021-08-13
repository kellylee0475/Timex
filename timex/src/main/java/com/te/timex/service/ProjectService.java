package com.te.timex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.te.timex.repository.ProjectRepository;

@Service
public class ProjectService{
	@Autowired
	private ProjectRepository projectRepository;
	

	
}