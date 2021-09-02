package com.te.timex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="timesheet2")
public class Timesheet2{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="project_task_id")
	private int projecttaskId;
	
	@Column(name="week_id")
	private int weekId;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	
	private String hours;
	private String day;
	
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

	@ManyToOne
    @JoinColumn(name="project_task_id",referencedColumnName="id", insertable = false, updatable = false)
    private ProjectTask projecttask;
	
	public Week getWeek() {
		return week;
	}
	public void setWeek(Week week) {
		this.week = week;
	}

	@ManyToOne
    @JoinColumn(name="week_id",referencedColumnName="id", insertable = false, updatable = false)
    private Week week;
	
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


	@Override
	public String toString() {
		return "Timesheet2 [id=" + id + ", userId=" + userId + ", projecttaskId=" + projecttaskId + ", weekId=" + weekId
				+ ", date=" + date + ", hours=" + hours + ", projecttask=" + projecttask + ", week=" + week + "]";
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
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


	
	
	
}


	