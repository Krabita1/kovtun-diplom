package com.chdtu.stanislav.service.impl;

import com.chdtu.stanislav.domain.Article;
import com.chdtu.stanislav.domain.CartItem;
import com.chdtu.stanislav.domain.ShoppingCart;
import com.chdtu.stanislav.domain.User;
import com.chdtu.stanislav.repository.CartItemRepository;
import com.chdtu.stanislav.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Override
	public ShoppingCart getShoppingCart(User user) {
		return new ShoppingCart(cartItemRepository.findAllByUserAndOrderIsNull(user));
	}
	
	@Override
	@Cacheable("itemcount")
	public int getItemsNumber(User user) {
		return cartItemRepository.countDistinctByUserAndOrderIsNull(user);
	}

	@Override
	public CartItem findCartItemById(Long cartItemId) {
		Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
		return opt.get();
	}

	@Override
	@CacheEvict(value = "itemcount", allEntries = true)
	public CartItem addArticleToShoppingCart(Article article, User user, int qty) {
		ShoppingCart shoppingCart = this.getShoppingCart(user);
		CartItem cartItem = shoppingCart.findCartItemByArticleAndSize(article.getId());
		if (cartItem != null) {
			cartItem.addQuantity(qty);
			cartItem = cartItemRepository.save(cartItem);
		} else {
			cartItem = new CartItem();
			cartItem.setUser(user);
			cartItem.setArticle(article);
			cartItem.setQty(qty);
			cartItem = cartItemRepository.save(cartItem);
		}		
		return cartItem;	
	}

	@Override
	@CacheEvict(value = "itemcount", allEntries = true)
	public void removeCartItem(CartItem cartItem) {
		cartItemRepository.deleteById(cartItem.getId());
	}
	
	@Override
	@CacheEvict(value = "itemcount", allEntries = true)
	public void updateCartItem(CartItem cartItem, Integer qty) {
		if (qty == null || qty <= 0) {
			this.removeCartItem(cartItem);
		} else if (cartItem.getArticle().hasStock(qty)) {
			cartItem.setQty(qty);
			cartItemRepository.save(cartItem);
		}
	}

	@Override
	@CacheEvict(value = "itemcount", allEntries = true)
	public void clearShoppingCart(User user) {
		cartItemRepository.deleteAllByUserAndOrderIsNull(user);	
	}	
}
