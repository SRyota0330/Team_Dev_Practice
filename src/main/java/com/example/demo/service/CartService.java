package com.example.demo.service;

import java.util.ArrayList;
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
	
	public void addItemToCart(Order order, Item item, int quantity) {
		
		if(orderItemRepository.idCheck(item.getItemid(),order.getOrderid())==true) {
			orderItemRepository.updateQuantityOfCart(order, item, quantity);
		}else {
			orderItemRepository.addItemToCart(order, item, quantity);
		}
	}
	
	public void delItemFromCart(Order order, Item item) {
		orderItemRepository.delItemFromCart(order, item);
	}
	
	public void editItemOnCart(int quantity) {
		orderItemRepository.editItemOnCart(quantity);
	}
	
	public List<OrderItem> getAllItems(Long orderId){
		return orderItemRepository.getAllItems(orderId);
	}
	
	public List<Item> getItemFromOrderItem(List<OrderItem> list){
		List<Item> resultList = new ArrayList<Item>();
		for(OrderItem item : list) {
			resultList.add(item.getItem());
		}
		System.out.println(resultList+"getItemFromOrderItem");
		return resultList;
	}
	
}
