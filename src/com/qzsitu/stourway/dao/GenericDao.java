package com.qzsitu.stourway.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class GenericDao<T>{
	@Autowired
	SessionFactory sessionFactory;
	
	public Session openSession() {
		return sessionFactory.openSession();
	}
	
    public T create(T t) {
    	sessionFactory.getCurrentSession().save(t);
        return t;
    }

    public T update(T t) {
    	sessionFactory.getCurrentSession().update(t);
        return t;
    }
    
    public void delete(T t) {
    	sessionFactory.getCurrentSession().delete(t);
    }


	@SuppressWarnings("unchecked")
	public T read(Class<?> domainClass, Serializable id) {
		T t =(T) sessionFactory.getCurrentSession().get(domainClass, id);
        return t;
    }
	
	public void execute(String queryName, String[] paramNames, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			if(paramObjs[i] instanceof Collection<?>){ 
				query.setParameterList(paramNames[i], (Collection<?>) paramObjs[i]);
			} else {
				query.setParameter(paramNames[i], paramObjs[i]);
			}
		}
		query.executeUpdate();
	}

	public int readCount(String queryName, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			query.setParameter(i, paramObjs[i]);
		}
		Number n = (Number) query.uniqueResult();
		return n == null?0:n.intValue();
	}
	
	public int readCount(String queryName, String[] paramNames, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			if(paramObjs[i] instanceof Collection<?>){ 
				query.setParameterList(paramNames[i], (Collection<?>) paramObjs[i]);
			} else {
				query.setParameter(paramNames[i], paramObjs[i]);
			}
		}
		Number n = (Number) query.uniqueResult();
		return n == null?0:n.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public T queryOne(String queryName, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			query.setParameter(i, paramObjs[i]);
		}
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public T queryOne(String queryName, String[] paramNames, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			if(paramObjs[i] instanceof Collection<?>){ 
				query.setParameterList(paramNames[i], (Collection<?>) paramObjs[i]);
			} else {
				query.setParameter(paramNames[i], paramObjs[i]);
			}
		}
		return (T) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<T> queryAll(Class<?> domainClass) {
//		有BUG，user关联多个group时重复取出
//		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(domainClass);
//		List<T> rs = new ArrayList<T>();
//		rs.addAll(criteria.list());
		
		Query query = sessionFactory.getCurrentSession().createQuery("from "+domainClass.getSimpleName());
		List<T> rs = new ArrayList<T>();
		rs.addAll(query.list());
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryAll(String queryName, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			query.setParameter(i, paramObjs[i]);
		}
		
		List<T> rs = new ArrayList<T>();
		rs.addAll(query.list());
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryAll(String queryName, String[] paramNames, Object[] paramObjs) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			if(paramObjs[i] instanceof Collection<?>){ 
				query.setParameterList(paramNames[i], (Collection<?>) paramObjs[i]);
			} else {
				query.setParameter(paramNames[i], paramObjs[i]);
			}
		}
		
		List<T> rs = new ArrayList<T>();
		rs.addAll(query.list());
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryLimit(Class<?> domainClass, int index, int count) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(domainClass);
		criteria.setFirstResult(index);
		criteria.setMaxResults(count);
		List<T> rs = new ArrayList<T>();
		rs.addAll(criteria.list());
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryLimit(String queryName, Object[] paramObjs, int index, int count) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			query.setParameter(i, paramObjs[i]);
		}
		query.setFirstResult(index);
		query.setMaxResults(count);
		
		List<T> rs = new ArrayList<T>();
		rs.addAll(query.list());
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> queryLimit(String queryName, String[] paramNames, Object[] paramObjs, int index, int count) {
		//this.getHibernateTemplate().findByCriteria(criteria, firstResult, maxResults)
		Query query = sessionFactory.getCurrentSession().getNamedQuery(queryName);
		for(int i = 0; i < paramObjs.length; i++) {
			if(paramObjs[i] instanceof Collection<?>){ 
				query.setParameterList(paramNames[i], (Collection<?>) paramObjs[i]);
			} else {
				query.setParameter(paramNames[i], paramObjs[i]);
			}
		}
		query.setFirstResult(index);
		query.setMaxResults(count);

		List<T> rs = new ArrayList<T>();
		rs.addAll(query.list());
		return rs;
	}
}
