package com.glu.db.dao;

import java.util.List;

import com.glu.db.entity.Student;

/**
 * Interface for StudentDAO.
 * 
 * @author MyEclipse Persistence Tools
 */

public interface IStudentDAO {
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
	 * IStudentDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Student entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Student entity);

	/**
	 * Delete a persistent Student entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
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
	public void delete(Student entity);

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
	public Student update(Student entity);

	public Student findById(int id);

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
	 *            count of results to return.
	 * @return List<Student> found by query
	 */
	public List<Student> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<Student> findByAge(Object age, int... rowStartIdxAndCount);

	public List<Student> findByName(Object name, int... rowStartIdxAndCount);

	public List<Student> findBySex(Object sex, int... rowStartIdxAndCount);

	public List<Student> findByTeacherId(Object teacherId,
			int... rowStartIdxAndCount);

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
	public List<Student> findAll(int... rowStartIdxAndCount);
}