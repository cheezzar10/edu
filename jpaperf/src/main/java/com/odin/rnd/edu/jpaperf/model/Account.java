package com.odin.rnd.edu.jpaperf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
	@Id
	@GeneratedValue
	@Column(name = "account_id")
	private Integer id;
	
	@Column(name = "company_name", length = 64, nullable = false)
	private String companyName;
	
	public Account() {
		
	}

	public Account(String companyName) {
		this.companyName = companyName;
	}

	public Integer getId() {
		return id;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
}
