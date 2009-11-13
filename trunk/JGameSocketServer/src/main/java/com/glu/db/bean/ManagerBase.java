/**
 * 
 */
package com.glu.db.bean;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.HibernateException;

import com.glu.db.EntityManagerHelper;
import com.glu.db.dao.AbstractBaseDao;
import com.glu.db.entity.IBaseEntity;

/**
 * @author yubingxing
 * 
 */
public abstract class ManagerBase<T extends IBaseEntity> {
	public EntityManager manager = null;
	public EntityTransaction transaction = null;
	public AbstractBaseDao<T> dao = null;

	public ManagerBase(AbstractBaseDao<T> dao) throws HibernateException {
		this.dao = dao;
	}
	
	/**
	 * start a JPA hibernate transaction
	 * 
	 * @throws HibernateException
	 */
	public void beginTransaction() throws HibernateException {
		manager = EntityManagerHelper.getEntityManager();
		transaction = manager.getTransaction();
		transaction.begin();
	}

	/**
	 * stop the JPA hibernate transaction
	 * 
	 * @param commit
	 * @throws HibernateException
	 */
	public void endTransaction(boolean commit) throws HibernateException {
		if (transaction == null || manager == null) {
			return;
		}
		if (commit) {
			transaction.commit();
		} else {
			transaction.rollback();
		}
		manager.close();
	}

	/**
	 * add a entity into the persistence
	 * 
	 * @param entity
	 * @throws HibernateException
	 */
	public void add(T entity) throws HibernateException {
		if (entity == null)
			return;
		beginTransaction();
		dao.save(entity);
		endTransaction(true);
	}

	/**
	 * delete a entity from the persistence
	 * 
	 * @param entity
	 * @throws HibernateException
	 */
	public void delete(T entity) throws HibernateException {
		if (entity == null)
			return;
		beginTransaction();
		dao.delete(entity);
		endTransaction(true);
	}

	/**
	 * update the entity in the persistence
	 * 
	 * @param entity
	 * @throws HibernateException
	 * @return result
	 */
	public T update(T entity) throws HibernateException {
		if (entity == null)
			return null;
		beginTransaction();
		T result = dao.update(entity);
		endTransaction(true);
		return result;
	}

	/**
	 * find by the id from persistence
	 * 
	 * @param id
	 * @return result
	 */
	public T findById(Long id) {
		beginTransaction();
		T result = dao.findById(id);
		endTransaction(true);
		return result;
	}
}
