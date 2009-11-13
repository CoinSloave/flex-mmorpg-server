/**
 * 
 */
package com.glu.db.bean;

import org.hibernate.HibernateException;

import com.glu.db.dao.TeacherDAO;
import com.glu.db.entity.Teacher;

/**
 * Teacher bean
 * 
 * @author yubingxing
 * 
 */
public class TeacherBean extends ManagerBase<Teacher> {

	public TeacherBean() throws HibernateException {
		super(new TeacherDAO());
	}

}
