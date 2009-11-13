/**
 * 
 */
package com.glu.dbtest;

import java.util.List;

import com.glu.db.bean.UserBean;
import com.glu.db.dao.UserDAO;
import com.glu.db.entity.User;


/**
 * @author yubingxing
 * 
 */
public class TestDB {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserBean userbean = new UserBean();
		userbean.setUserInfo("yyyy8", "123456", "yubingxing123@gmail.com");
		UserDAO user = new UserDAO();
		List<User> result = user.findByName("yubingxing", 0);
		if(result.size() > 0){
			User u = result.get(0);
			System.out.println(u.getId());
			System.out.println(u.getName());
			System.out.println(u.getPassword());
			System.out.println(u.getEmail());
		}
	}

}
