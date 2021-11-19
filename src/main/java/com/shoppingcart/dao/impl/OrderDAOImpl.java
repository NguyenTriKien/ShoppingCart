package com.shoppingcart.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shoppingcart.dao.ProductDAO;

@Repository
@Transactional
public class OrderDAOImpl {
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;
	
	private int getMaxOrderNum() {
		Session session = sessionFactory.getCurrentSession();
		String hql = "SELECT MAX(ORD.orderNum) FROM Order ORD";
		Query<Integer> query = session.createQuery(hql);
		Integer value = (Integer) query.uniqueResult();
		if(value == null) {
			return 0;
		}
		
		return value;
		
	}
	
}
