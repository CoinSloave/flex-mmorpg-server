/**
 * 
 */
package com.glu.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author yubingxing
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractBaseEntity implements IBaseEntity {
	// Fields

	private Integer id;

	// Constructors

	/** default constructor */
	public AbstractBaseEntity() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setId(int id) {
		this.id = new Integer(id);
	}

}
