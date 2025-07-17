package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;

@Service
public class CartService {
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	public void addItemToCart(Order order, Item item) {
		orderItemRepository.addItemToCart(order, item);
	}
	
	public void delItemFromCart(int orderId, int itemId) {
		orderItemRepository.delItemFromCart(orderId, itemId);
	}
	
	public void editItemOnCart(int quantity) {
		orderItemRepository.editItemOnCart(quantity);
	}
	
	public List<OrderItem> getAllItems(OrderItem orderItem){
		return orderItemRepository.getAllItems(orderItem);
	}
	
}
