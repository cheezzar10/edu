package com.odin.rnd.edu.jpaperf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accounts")
public class Account {
	private Integer id;
	
	private String companyName;
	
	private Account owner;
	
	public Account() {
		
	}

	public Account(String companyName) {
		this.companyName = companyName;
	}
	
	@Id
	@GeneratedValue
	@Column(name = "account_id")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "company_name", length = 64, nullable = false)
	public String getCompanyName() {
		return companyName;
	}
	
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	// will be loaded eagerly if FetchType hasn't been specified
	// @ManyToOne(fetch = FetchType.LAZY)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	public Account getOwner() {
		return owner;
	}

	public void setOwner(Account owner) {
		this.owner = owner;
	}
}
