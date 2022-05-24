package com.chdtu.stanislav.service;

import com.chdtu.stanislav.domain.*;

import java.util.List;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, Shipping shippingAddress, Payment payment, User user);
	
	List<Order> findByUser(User user);
	
	Order findOrderWithDetails(Long id);
}
