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
@Table(name="project")
public class Project{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	
	private String number;
	private String title;
	
	@OneToMany(mappedBy = "project")
	private List<ProjectTask> projectTask = new ArrayList<>();
	
	//@OneToMany(mappedBy = "project")
//	private List<ExpenseList> projectExpense = new ArrayList<>();
//	@OneToMany(mappedBy = "project")
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
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	


	
	
}