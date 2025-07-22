package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;

@Repository
public class OrderRepository {
	
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	OrderItemRepository orderItemRepository;
	
	public Order getOrderFromUser(Long userid) {
		String query = "SELECT * FROM orders WHERE user_id = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, userid);
		
		Map<String, Object> resultMap = resultList.get(0);
		System.out.println(resultMap+"MAP");
		
		Order order = new Order();
		
		order.setOrderid(((Number) resultMap.get("orderid")).longValue());
		System.out.println(order.getOrderid());
		order.setStatus((String)resultMap.get("status"));
		
		List<OrderItem> orderItemList = orderItemRepository.getAllItems(order.getOrderid());
		
		order.setOrderitems(orderItemList);
		System.out.println(order.getOrderitems()+"OrderRepositoryのgetOrderFromUser");
		
		return order;
	}
	
}