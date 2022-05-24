package com.chdtu.stanislav.service;

import com.chdtu.stanislav.domain.Article;
import com.chdtu.stanislav.domain.CartItem;
import com.chdtu.stanislav.domain.ShoppingCart;
import com.chdtu.stanislav.domain.User;


public interface ShoppingCartService {

	ShoppingCart getShoppingCart(User user);
	
	int getItemsNumber(User user);
	
	CartItem findCartItemById(Long cartItemId);
	
	CartItem addArticleToShoppingCart(Article article, User user, int qty);
		
	void clearShoppingCart(User user);
	
	void updateCartItem(CartItem cartItem, Integer qty);

	void removeCartItem(CartItem cartItem);
	
}
