package com.te.timex.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="task")
public class Task{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	
	private String name;
	private boolean enabled;
	
	@OneToMany(mappedBy = "task")
	private List<ProjectTask> tasks = new ArrayList<>();

//	@OneToMany(mappedBy = "task")
//	private List<ProjectTask> protask = new ArrayList<>();
//	
//
//	public List<ProjectTask> getProtask() {
//		return protask;
//	}
//	public void setProtask(List<ProjectTask> protask) {
//		this.protask = protask;
//	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	
	
}