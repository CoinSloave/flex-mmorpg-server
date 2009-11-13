/**
 * 
 */
package com.glu.db.bean;

import org.hibernate.HibernateException;

import com.glu.db.dao.UserDAO;
import com.glu.db.entity.User;

/**
 * @author yubingxing
 * 
 */
public class UserBean extends ManagerBase<User> {

	public UserBean() throws HibernateException {
		super(new UserDAO());
	}

	public void setUserInfo(String name, String password, String email) {
		User u = new User();
		u.setName(name);
		u.setPassword(password);
		u.setEmail(email);
		add(u);
	}

	/**
	 * delete a user entity from persistence
	 * 
	 * @param entity
	 * @throws HibernateException
	 */
	public void delete(User entity) throws HibernateException {
		super.delete(entity);
	}

	/**
	 * find the user by the id from persistence
	 * 
	 * @param id
	 * @return result
	 */
	public User findById(int id) {
		return super.findById(new Long(id));
	}
}
