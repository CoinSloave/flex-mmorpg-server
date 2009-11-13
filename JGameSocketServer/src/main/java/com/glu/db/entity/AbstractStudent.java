package com.glu.db.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * AbstractStudent entity provides the base persistence definition of the
 * Student entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractStudent extends AbstractBaseEntity {

	// Fields

	private Integer age;
	private String name;
	private Boolean sex;
	private Integer teacherId;

	// Constructors

	/** default constructor */
	public AbstractStudent() {
	}

	/** full constructor */
	public AbstractStudent(Integer age, String name, Boolean sex,
			Integer teacherId) {
		this.age = age;
		this.name = name;
		this.sex = sex;
		this.teacherId = teacherId;
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

	@Column(name = "teacherId")
	public Integer getTeacherId() {
		return this.teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

}