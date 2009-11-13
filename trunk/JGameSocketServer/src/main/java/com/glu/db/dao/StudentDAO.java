package com.glu.db.dao;

import com.glu.db.EntityManagerHelper;

import com.glu.db.entity.Student;

import java.util.List;
import java.util.logging.Level;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Student entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see com.glu.db.entity.Student
 * @author MyEclipse Persistence Tools
 */

public class StudentDAO extends AbstractBaseDao<Student> implements IStudentDAO {
	// property constants
	public static final String AGE = "age";
	public static final String NAME = "name";
	public static final String SEX = "sex";
	public static final String TEACHER_ID = "teacherId";

	/**
	 * Perform an initial save of a previously unsaved Student entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * StudentDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Student entity) {
		EntityManagerHelper.log("saving Student instance", Level.INFO, null);
		super.save(entity);
	}

	/**
	 * Delete a persistent Student entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * StudentDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Student entity) {
		EntityManagerHelper.log("deleting Student instance", Level.INFO, null);
		super.delete(entity);
	}

	/**
	 * Persist a previously saved Student entity and return it or a copy of it
	 * to the sender. A copy of the Student entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = StudentDAO.update(entity);
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
	public Student update(Student entity) {
		EntityManagerHelper.log("updating Student instance", Level.INFO, null);
		return super.update(entity);
	}

	public Student findById(int id) {
		EntityManagerHelper.log("finding Student instance with id: " + id,
				Level.INFO, null);
		return super.findById(new Long(id));
	}

	/**
	 * Find all Student entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Student property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<Student> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Student> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding Student instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Student model where model."
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

	public List<Student> findByAge(Object age, int... rowStartIdxAndCount) {
		return findByProperty(AGE, age, rowStartIdxAndCount);
	}

	public List<Student> findByName(Object name, int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<Student> findBySex(Object sex, int... rowStartIdxAndCount) {
		return findByProperty(SEX, sex, rowStartIdxAndCount);
	}

	public List<Student> findByTeacherId(Object teacherId,
			int... rowStartIdxAndCount) {
		return findByProperty(TEACHER_ID, teacherId, rowStartIdxAndCount);
	}

	/**
	 * Find all Student entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<Student> all Student entities
	 */
	@SuppressWarnings("unchecked")
	public List<Student> findAll(final int... rowStartIdxAndCount) {
		EntityManagerHelper.log("finding all Student instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Student model";
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