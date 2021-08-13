package com.te.timex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.te.timex.model.Project;
import com.te.timex.model.ProjectTask;


public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Integer> {
	
	
	List<ProjectTask> findByProjectId(int project_Id);

}