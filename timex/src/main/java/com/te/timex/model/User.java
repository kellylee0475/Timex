package com.te.timex.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Transient;


@Entity
@Table(name="Users")
public class User{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
		
	@NotBlank(message = "Please enter first name")
	private String firstname;
	
	@NotBlank(message = "Please enter last name")
	private String lastname;	
	@NotBlank(message = "Please enter email address.")
	@Email(message = "Please enter correct email address.")
	private String email;
	
	private String password;
	private String address;
	private String country;
	private int zipcode;
	private String memo;
	private String city;
	private Boolean enabled;	
	private String resetpwtoken;
	private String photo;
	private String photopath;
	
	@ManyToMany
	@JoinTable(name = "User_Role", 
				joinColumns = @JoinColumn(name = "userId"), 
				inverseJoinColumns = @JoinColumn(name = "roleId"))
	private List<Role> roles = new ArrayList<>();
	
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)//Board에서 한거 그대로 사용 (양방향매핑)//ManyToOne쪽에서 작성, OneToMany쪽에서는 mappedby키워드로 사용
	private List<Board> boards=new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<ExpenseList> expenselist;
	
	@Transient
	public String getPhotopath() {
		 return photopath;	
	}

	public void setPhotopath(String photopath) {
		this.photopath = photopath;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getResetpwtoken() {
		return resetpwtoken;
	}

	public void setResetpwtoken(String resetpwtoken) {
		this.resetpwtoken = resetpwtoken;
	}

	public List<ExpenseList> getExpenselist() {
		return expenselist;
	}

	public void setExpenselist(List<ExpenseList> expenselist) {
		this.expenselist = expenselist;
	}
	
	public List<Board> getBoards() {
		return boards;
	}

	public void setBoards(List<Board> boards) {
		this.boards = boards;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

}