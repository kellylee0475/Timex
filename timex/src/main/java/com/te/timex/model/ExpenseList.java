package com.te.timex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="expense_list")
public class ExpenseList{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private int id;
	
	//repository에서 findByUserId로 사용하기위해 바꿈. _사용불가
	@Column(name = "user_id")
	private int userId;
	@Column(name = "project_id")
	private int projectId;
	@Column(name = "expense_id")
	private int expenseId;
	private int qty;
	
	@Temporal(TemporalType.DATE)
	private Date date;
	private String total_amount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public int getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(int expenseId) {
		this.expenseId = expenseId;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	
	
//    @ManyToOne
//    @JoinColumn(name = "project_id")
//    private Project project;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//    
//
//    @ManyToOne
//    @JoinColumn(name = "expense_id")
//    private Expense expense;
}