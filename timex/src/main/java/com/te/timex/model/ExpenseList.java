package com.te.timex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

	private int status;
	
	//단방향으로 추가한부분
	@ManyToOne
    @JoinColumn(name="project_id",referencedColumnName="id", insertable = false, updatable = false)
    private Project project;

	@ManyToOne
    @JoinColumn(name="expense_id",referencedColumnName="id", insertable = false, updatable = false)
    private Expense expense;
	
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName="id", insertable = false, updatable = false)
    private User user;
	
	
	
	
//	@OneToMany
//    private List<User> users = new ArrayList<>();
//	
//	 public void addUser(final User image) {
//		 expenseList.add(users);
//     }
//	@OneToOne
//	@JoinColumn(name="user_id")
//	private List<User> users = new ArrayList<>();
	
//	@OneToOne
//	@JoinTable(name = "expense", 
//				joinColumns = @JoinColumn(name = "userId"), 
//				inverseJoinColumns = @JoinColumn(name = "roleId"))
//	private List<Role> roles = new ArrayList<>();
	
//  @OneToOne
//  @JoinColumn(name = "project_id", insertable = false, updatable = false)
//  private List<Project> project;
//
//  @OneToOne
//  @JoinColumn(name = "user_id",insertable = false, updatable = false)
//  private List<User> user;
//  
//
//  @OneToOne
//  @JoinColumn(name = "expense_id",insertable = false, updatable = false)
//  private List<Expense> expense;
	
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



	@Override
	public String toString() {
		return "ExpenseList [id=" + id + ", userId=" + userId + ", projectId=" + projectId + ", expenseId=" + expenseId
				+ ", qty=" + qty + ", date=" + date + ", total_amount=" + total_amount + ", status=" + status
				+ ", project=" + project + ", expense=" + expense + ", user=" + user + "]";
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
	public String getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	
	

}