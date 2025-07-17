package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;

@Repository
public class OrderItemRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	OrderItemRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void addItemToCart(Order order, Item item) {
		String query = "INSERT INTO orderitem (order_id, item_id, quantity) VALUES (?, ?, ?)";
		jdbcTemplate.update(query, order.getOrderid(), item.getItemid());
	}
	
	public void delItemFromCart(Order order, Item item) {
		String query = "DELETE FROM orderitem (order_id, item_id) VALUES (?, ?)";
		jdbcTemplate.update(query, order, item);
	}
	
	public void editItemOnCart(int quantity) {
		String query = "UPDATE orderitem SET quantity = ?";
		jdbcTemplate.update(query);
	}
	
	public List<OrderItem> getAllItems(OrderItem orderItem){
		List<OrderItem>  allItemsList = new ArrayList<OrderItem>();
		
		String query = "SELECT * FROM ORDERITEM WHERE order_id = ?";
		
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, orderItem.getOrder());
		
		for(Map<String, Object> searchResultMap : resultList) {
			OrderItem orderItem1 = new OrderItem();
			
			orderItem1.setOrder((Order)searchResultMap.get("ORDER_ID"));
			orderItem1.setItem((Item)searchResultMap.get("ITEM_ID"));
			orderItem1.setQuantity((int)searchResultMap.get("QUANTITY"));
			
			allItemsList.add(orderItem1);
		}
		
		return allItemsList;
	}
}
