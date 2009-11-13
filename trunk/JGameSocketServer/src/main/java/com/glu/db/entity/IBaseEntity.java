/**
 * 
 */
package com.glu.db.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Interface for IBaseEntity
 * 
 * @author yubingxing
 */
@MappedSuperclass
public interface IBaseEntity extends Serializable {
	/**
	 * 映射表的自增字段id
	 */
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId();

	/**
	 * 写id字段
	 * 
	 * @param id
	 */
	public void setId(Integer id);

	/**
	 * 读id字段
	 * 
	 * @param id
	 */
	public void setId(int id);
}
