package com.glu.db.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Teacher entity. @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "teacher", catalog = "test")
public class Teacher extends AbstractTeacher {

	// Constructors

	/** default constructor */
	public Teacher() {
	}

	/** full constructor */
	public Teacher(Integer age, String name, Boolean sex) {
		super(age, name, sex);
	}

}
