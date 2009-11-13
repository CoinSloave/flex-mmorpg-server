package com.glu.db.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * AbstractUser entity provides the base persistence definition of the User
 * entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@MappedSuperclass
public abstract class AbstractUser extends AbstractBaseEntity {

	// Fields

	private String name;
	private String password;
	private String email;

	// Constructors

	/** default constructor */
	public AbstractUser() {
	}

	/** minimal constructor */
	public AbstractUser(String name, String password) {
		this.name = name;
		this.password = password;
	}

	/** full constructor */
	public AbstractUser(String name, String password, String email) {
		this.name = name;
		this.password = password;
		this.email = email;
	}

	// Property accessors
	@Column(name = "name", unique = true, nullable = false, length = 41)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "password", nullable = false)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "email", length = 400)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = (email == null) ? "" : email;
	}

}