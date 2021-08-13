package com.te.timex.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="project_task")
public class ProjectTask{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
//	private int task_id;
//	private int project_id;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "taskId")
    private Task task;
    
    
	
    
    
    
    
    
    
}