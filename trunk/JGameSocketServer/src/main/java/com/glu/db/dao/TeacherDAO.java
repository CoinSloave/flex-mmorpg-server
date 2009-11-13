package com.glu.db.dao;

import java.util.List;
import java.util.logging.Level;

import javax.persistence.Query;

import com.glu.db.EntityManagerHelper;
import com.glu.db.entity.Teacher;

/**
 * A data access object (DAO) providing persistence and search support for
 * Teacher entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.glu.db.entity.Teacher
 * @author MyEclipse Persistence Tools
 */

public class TeacherDAO extends AbstractBaseDao<Teacher> implements ITeacherDAO {
	// property constants
	public static final String AGE = "age";
	public static final String NAME = "name";
	public static final String SEX = "sex";

	/**
	 * Perform an initial save of a previously unsaved Teacher entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * TeacherDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Teacher entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Teacher entity) {
		EntityManagerHelper.log("saving Teacher instance", Level.INFO, null);
		super.save(entity);
	}

	/**
	 * Delete a persistent Teacher entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * TeacherDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Teacher entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Teacher entity) {
		EntityManagerHelper.log("deleting Teacher instance", Level.INFO, null);
		super.delete(entity);
	}

	/**
	 * Persist a previously saved Teacher entity and return it or a copy of it
	 * to the sender. A copy of the Teacher entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = TeacherDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Teacher entity to update
	 * @return Teacher the persisted Teacher entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Teacher update(Teacher entity) {
		EntityManagerHelper.log("updating Teacher instance", Level.INFO, null);
		return super.update(entity);
	}

	public Teacher findById(int id) {
		EntityManagerHelper.log("finding Teacher instance with id: " + id,
				Level.INFO, null);
		return super.findById(new Long(id));
	}

	/**
	 * Find all Teacher entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Teacher property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Teacher> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Teacher instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Teacher model where model."
					+ propertyName + "= :propertyValue";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find by property name failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	public List<Teacher> findByAge(Object age, int... rowStartIdxAndCount) {
		return findByProperty(AGE, age, rowStartIdxAndCount);
	}

	public List<Teacher> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<Teacher> findBySex(Object sex, int... rowStartIdxAndCount) {
		return findByProperty(SEX, sex, rowStartIdxAndCount);
	}

	/**
	 * Find all Teacher entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Teacher> all Teacher entities
	 */
	@SuppressWarnings("unchecked")
	public List<Teacher> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Teacher instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Teacher model";
			Query query = getEntityManager().createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}