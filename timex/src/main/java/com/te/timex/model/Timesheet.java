package com.te.timex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="timesheet")
public class Timesheet{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="project_task_id")
	private int projecttaskId;
	
	@Column(name="week_id")
	private int weekId;
	
	private String sun;

	private String mon;
	private String tue;
	private String wed;
	private String thur;
	private String fri;
	private String sat;

	
	@ManyToOne
    @JoinColumn(name="project_task_id",referencedColumnName="id", insertable = false, updatable = false)
    private ProjectTask projecttask;
	

	
	public ProjectTask getProjecttask() {
		return projecttask;
	}
	public void setProjecttask(ProjectTask projecttask) {
		this.projecttask = projecttask;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}


	public int getProjecttaskId() {
		return projecttaskId;
	}
	public void setProjecttaskId(int projecttaskId) {
		this.projecttaskId = projecttaskId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getWeekId() {
		return weekId;
	}
	public void setWeekId(int weekId) {
		this.weekId = weekId;
	}
	public String getSun() {
		return sun;
	}
	public void setSun(String sun) {
		this.sun = sun;
	}
	public String getMon() {
		return mon;
	}
	public void setMon(String mon) {
		this.mon = mon;
	}
	public String getTue() {
		return tue;
	}
	public void setTue(String tue) {
		this.tue = tue;
	}
	public String getWed() {
		return wed;
	}
	public void setWed(String wed) {
		this.wed = wed;
	}
	public String getThur() {
		return thur;
	}
	public void setThur(String thur) {
		this.thur = thur;
	}
	public String getFri() {
		return fri;
	}
	public void setFri(String fri) {
		this.fri = fri;
	}
	public String getSat() {
		return sat;
	}
	public void setSat(String sat) {
		this.sat = sat;
	}
	@Override
	public String toString() {
		return "Timesheet [id=" + id + ", userId=" + userId + ", projecttaskId=" + projecttaskId + ", weekId=" + weekId
				+ ", sun=" + sun + ", mon=" + mon + ", tue=" + tue + ", wed=" + wed + ", thur=" + thur + ", fri=" + fri
				+ ", sat=" + sat + ", projecttask=" + projecttask + "]";
	}
	

	
	
	
}


	