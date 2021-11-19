package com.shoppingcart.dao;

import com.shoppingcart.model.CartInfo;

public interface OrderDAO {

	public void saveOrder(CartInfo cartInfo);
	
}
