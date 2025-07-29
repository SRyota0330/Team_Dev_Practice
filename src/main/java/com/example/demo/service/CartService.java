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
	
	@Autowired
	StockService stockService;
	
	@Autowired
	OrderService orderService;
	
	
	public void addItemToCart(Order order, Item item, int quantity) {
		if(orderItemRepository.idCheck(item.getItemid(),order.getOrderid())==true) {
			orderItemRepository.updateQuantityOfCart(order, item, quantity);
		}else {
			orderItemRepository.addItemToCart(order, item, quantity);
		}
	}
	
	public void delItemFromCart(Order order, Long itemid) {
		orderItemRepository.delItemFromCart(order.getOrderid(), itemid);
	}
	
	public void editItemOnCart(int quantity) {
		orderItemRepository.editItemOnCart(quantity);
	}
	
	public List<OrderItem> getAllItems(Long orderId){
		return orderItemRepository.getAllItems(orderId);
	}
	
	public List<Item> getItemFromOrderItem(List<OrderItem> list){
		List<Item> resultList = new ArrayList<Item>();
		for(OrderItem orderItem : list) {
			resultList.add(orderItem.getItem());
		}
		System.out.println(resultList+"getItemFromOrderItem");
		return resultList;
	}
	
	public List<OrderItem> getCartItemFromOrderItem(List<OrderItem> list){
		List<OrderItem> resultList = new ArrayList<OrderItem>();
		for(OrderItem orderItem : list) {
			
			if("cart".equals(orderItem.getStatus())) {
				resultList.add(orderItem);
			}
			
		}
		System.out.println(resultList+"getCartItemFromOrderItem");
		return resultList;
	}
	
	public List<OrderItem> getPurchasedItemFromOrderItem(List<OrderItem> list){
		List<OrderItem> resultList = new ArrayList<OrderItem>();
		for(OrderItem orderItem : list) {
			
			if("cart".equals(orderItem.getStatus())) {
				resultList.add(orderItem);
			}
			
		}
		System.out.println(resultList+"getPurchasedItemFromOrderItem");
		return resultList;
	}
	
	
	public void updateStatusToPurchased(Long orderId) {
		orderItemRepository.updateStatusToPurchased(orderId);
	}
	
	public void changeQuantity(Order order, Long itemid, int delta) {
		orderItemRepository.changeQuantity(order.getOrderid(), itemid, delta);
	}
	
	public void clearCart(Long orderid) {
		orderItemRepository.clearCart(orderid);
	}
	
//	public boolean checkStockAndOrder(User user) {
//		
//		List<OrderItem> orderItems = cartService.getAllItems(orderService.getOrderFromUser(user.getUserid()).getOrderid());
//		List<OrderItem> orderItemList = cartService.getCartItemFromOrderItem(orderItems);
//		
//		boolean result = true;
//		
//		for(OrderItem oi : orderItemList) {
//			int stockAmount = stockService.getCount(oi.getItem().getItemid());
//			int orderAmount = orderItemRepository.statusCartItems(oi.getOrder().getOrderid());
//			if(stockAmount-orderAmount<0) {
//				result = false;
//				break;
//			}
//		}
//		return result;
//	}
	
}
