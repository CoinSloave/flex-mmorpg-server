package com.glu.db.dao;

import java.util.List;

import com.glu.db.entity.Teacher;

/**
 * Interface for TeacherDAO.
 * 
 * @author MyEclipse Persistence Tools
 */

public interface ITeacherDAO {
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
	 * ITeacherDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Teacher entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Teacher entity);

	/**
	 * Delete a persistent Teacher entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * ITeacherDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Teacher entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Teacher entity);

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
	 * entity = ITeacherDAO.update(entity);
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
	public Teacher update(Teacher entity);

	public Teacher findById(int id);

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
	 *            count of results to return.
	 * @return List<Teacher> found by query
	 */
	public List<Teacher> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<Teacher> findByAge(Object age, int... rowStartIdxAndCount);

	public List<Teacher> findByName(Object name, int... rowStartIdxAndCount);

	public List<Teacher> findBySex(Object sex, int... rowStartIdxAndCount);

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
	public List<Teacher> findAll(int... rowStartIdxAndCount);
}