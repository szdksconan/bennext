package com.unipay.benext.dao.impl;

import com.unipay.benext.dao.BaseDaoI;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Repository
public class BaseDaoImpl<T> implements BaseDaoI<T> {
	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * 获得当前事物的session
	 *
	 * @return org.hibernate.Session
	 */
	public Session getCurrentSession() {
		return this.sessionFactory.getCurrentSession();
	}

	@Override
	public T get(String hql, Map<String, Object> params) {
		Query q = this.getCurrentSession().createQuery(hql);
		if ((params != null) && !params.isEmpty()) {
			for (String key : params.keySet()) {
				q.setParameter(key, params.get(key));
			}
		}
		List<T> l = q.list();
		if ((l != null) && (l.size() > 0)) {
			return l.get(0);
		}
		return null;
	}

	@Override
	public T get(Class<T> c, Serializable id) {
		return (T) this.getCurrentSession().get(c, id);
	}
}
