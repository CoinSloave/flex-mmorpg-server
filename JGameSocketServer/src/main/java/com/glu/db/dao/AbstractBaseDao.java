/**
 * 
 */
package com.glu.db.dao;

import java.lang.reflect.ParameterizedType;
import java.util.logging.Level;

import javax.persistence.EntityManager;

import com.glu.db.EntityManagerHelper;
import com.glu.db.entity.IBaseEntity;

/**
 * @author yubingxing
 * @param <T>
 * 
 */
public abstract class AbstractBaseDao<T extends IBaseEntity> {

	private Class<T> entityClass = null;

	@SuppressWarnings("unchecked")
	public AbstractBaseDao() {
		entityClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved entity. All subsequent
	 * persist actions of this entity should use the #update() method. This
	 * operation must be performed within the a database transaction context for
	 * the entity's data to be permanently saved to the persistence store, i.e.,
	 * database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IStudentDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(T entity) {
		// todo: add logger
		try {
			getEntityManager().persist(entity);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException ex) {
			EntityManagerHelper.log("save failed", Level.SEVERE, ex);
			throw ex;
		}
	}

	/**
	 * Delete a persistent entity. This operation must be performed within the a
	 * database transaction context for the entity's data to be permanently
	 * deleted from the persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IStudentDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(T entity) {
		// todo: add logger
		try {
			entity = getEntityManager().getReference(entityClass,
					entity.getId());
			getEntityManager().remove(entity);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException ex) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, ex);
			throw ex;
		}
	}

	/**
	 * Persist a previously saved entity and return it or a copy of it to the
	 * sender. A copy of the Student entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = IStudentDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to update
	 * @return Student the persisted Student entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public T update(T entity) {
		// todo add logger
		try {
			T result = getEntityManager().merge(entity);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException ex) {
			EntityManagerHelper.log("update failed", Level.SEVERE, ex);
			throw ex;
		}
	}

	public T findById(Long id) {
		// todo add logger
		try {
			T entity = getEntityManager().find(entityClass, id);
			return entity;
		} catch (RuntimeException ex) {
			EntityManagerHelper.log("find failed", Level.SEVERE, ex);
			throw ex;
		}
	}
}
