/**
 * 
 */
package com.glu.db.bean;

import org.hibernate.HibernateException;

import com.glu.db.dao.StudentDAO;
import com.glu.db.entity.Student;

/**
 * Student bean
 * 
 * @author yubingxing
 * 
 */
public class StudentBean extends ManagerBase<Student> {

	public StudentBean() throws HibernateException {
		super(new StudentDAO());
	}

	public void setStudentInfo(String name, boolean sex, int age, int teacherId)
			throws HibernateException {
		Student st = new Student();
		st.setName(name);
		st.setSex(sex);
		st.setAge(age);
		st.setTeacherId(teacherId);
		this.add(st);
	}

	/**
	 * delete a student entity from persistence
	 * 
	 * @param entity
	 * @throws HibernateException
	 */
	public void delete(Student entity) throws HibernateException {
		super.delete(entity);
	}

	/**
	 * find the student entity by the id from persistence
	 * 
	 * @param id
	 * @return result
	 */
	public Student findById(int id) {
		return super.findById(new Long(id));
	}
}
