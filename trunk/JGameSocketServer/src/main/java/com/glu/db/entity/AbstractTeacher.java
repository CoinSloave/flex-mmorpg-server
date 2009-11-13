package com.glu.db.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * AbstractTeacher entity provides the base persistence definition of the
 * Teacher entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractTeacher extends AbstractBaseEntity {

	// Fields

	private Integer age;
	private String name;
	private Boolean sex;

	// Constructors

	/** default constructor */
	public AbstractTeacher() {
	}

	/** full constructor */
	public AbstractTeacher(Integer age, String name, Boolean sex) {
		this.age = age;
		this.name = name;
		this.sex = sex;
	}

	// Property accessors

	@Column(name = "age")
	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "sex")
	public Boolean getSex() {
		return this.sex;
	}

	public void setSex(Boolean sex) {
		this.sex = sex;
	}

}