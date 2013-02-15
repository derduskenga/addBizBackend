package com.addBusiness.addbiz;
public class Account {

	//declare fields
	private long id;
	private String email;
	private String password;
	private String msg;
	
	
	
	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Account(String msg) {
		
		this.msg = msg;
	}


	//empty constructor.
	public Account() {
		
	}
	
	
	public Account(String email, String password) {
		
		this.email = email;
		this.password = password;
	}

	public Account(int id, String email, String password) {
	
		this.id = id;
		this.email = email;
		this.password = password;
	}
	public long getId() {
		return id;
	}


	public void setId( long id) {
		this.id = id;
	}


	public String getEmailAddress() {
		return email;
	}


	public void setEmailAddress(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


}
