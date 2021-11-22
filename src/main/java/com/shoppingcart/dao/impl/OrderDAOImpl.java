package com.shoppingcart.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.entity.Order;
import com.shoppingcart.entity.OrderDetail;
import com.shoppingcart.entity.Product;
import com.shoppingcart.model.CartInfo;
import com.shoppingcart.model.CartLineInfo;
import com.shoppingcart.model.CustomerInfo;

@Repository
@Transactional
public class OrderDAOImpl implements OrderDAO {
	
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
	
	@Override
	public void saveOrder(CartInfo cartInfo) {
		Session session = sessionFactory.getCurrentSession();
		int orderNum = getMaxOrderNum() + 1;
		
		Order order = new Order();
		order.setId(UUID.randomUUID().toString());//java.util.UUID
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());
		order.setAmount(cartInfo.getAmountTotal());
		
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhone());
		order.setCustomeAddress(customerInfo.getAddress());
		session.persist(order);
		
		List<CartLineInfo> cartLineInfos = cartInfo.getCartLineInfos();
		for (CartLineInfo cartLineInfo : cartLineInfos) {
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setId(UUID.randomUUID().toString());
			orderDetail.setOrder(order);
			orderDetail.setAmount(cartLineInfo.getAmount());
			orderDetail.setPrice(cartLineInfo.getProductInfo().getPrice());
			orderDetail.setQuantity(cartLineInfo.getQuantity());
			
			String code = cartLineInfo.getProductInfo().getCode();
			Product product = productDAO.getProductByCode(code);
			orderDetail.setProduct(product);
			
			
			session.persist(orderDetail);
		}
		
		cartInfo.setOrderNum(orderNum);
	}
	
}
