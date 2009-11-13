package com.glu.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Student entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "student", catalog = "test")
public class Student extends AbstractStudent {

	// Constructors

	/** default constructor */
	public Student() {
	}

	/** full constructor */
	public Student(Integer age, String name, Boolean sex, Integer teacherId) {
		super(age, name, sex, teacherId);
	}

}
