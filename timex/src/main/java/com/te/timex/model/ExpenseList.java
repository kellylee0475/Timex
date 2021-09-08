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
@Table(name = "expense_list")
public class ExpenseList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	// repository에서 findByUserId로 사용하기위해 바꿈. _사용불가
	@Column(name = "user_id")
	private int userId;
	@Column(name = "project_id")
	private int projectId;
	@Column(name = "expense_id")
	private int expenseId;
	private int qty;

	@Temporal(TemporalType.DATE)
	private Date date;
	private String totalcost;
	private int status;
	private String filename;
	private String filepath;

	// 단방향으로 추가한부분
	@ManyToOne
	@JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Project project;

	@ManyToOne
	@JoinColumn(name = "expense_id", referencedColumnName = "id", insertable = false, updatable = false)
	private Expense expense;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
	private User user;

	@Override
	public String toString() {
		return "ExpenseList [id=" + id + ", userId=" + userId + ", projectId=" + projectId + ", expenseId=" + expenseId
				+ ", qty=" + qty + ", date=" + date + ", totalcost=" + totalcost + ", status=" + status + ", filename="
				+ filename + ", filepath=" + filepath + ", project=" + project + ", expense=" + expense + ", user="
				+ user + "]";
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Expense getExpense() {
		return expense;
	}

	public void setExpense(Expense expense) {
		this.expense = expense;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}	

	public String getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(String totalcost) {
		this.totalcost = totalcost;
	}

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

}