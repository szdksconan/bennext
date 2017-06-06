package com.unipay.benext.dao;

import java.io.Serializable;
import java.util.Map;

public interface BaseDaoI<T> {
	public T get(String hql, Map<String, Object> params);

	public T get(Class<T> c, Serializable id);
}
