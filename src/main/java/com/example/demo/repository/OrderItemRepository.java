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
	public boolean idCheck(Long itemid, Long orderid) {
		String query = "SELECT * FROM orderitem WHERE item_id = ? AND order_id = ? AND status = 'cart'";
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,itemid,orderid);
		if(searchResultList.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	public void addItemToCart(Order order, Item item, int quantity) {
		String query = "INSERT INTO orderitem (order_id, item_id, quantity, status) VALUES (?, ?, ?, ?)";
		jdbcTemplate.update(query, order.getOrderid(), item.getItemid(), quantity, "cart");
	}
	
	public void updateQuantityOfCart(Order order, Item item, int quantity) {
		String query = "UPDATE orderitem SET quantity = quantity + ? WHERE order_id = ? AND item_id = ?";
		jdbcTemplate.update(query, quantity, order.getOrderid(), item.getItemid());
	}
	
	public void delItemFromCart(Long orderid, Long itemid) {
		String query = "DELETE FROM orderitem WHERE order_id = ? AND item_id = ?";
		jdbcTemplate.update(query, orderid, itemid);
	}
	
	public void editItemOnCart(int quantity) {
		String query = "UPDATE orderitem SET quantity = ?";
		jdbcTemplate.update(query, quantity);
	}
	
//	public List<OrderItem> getAllItems(Long orderId){
//		List<OrderItem>  allItemsList = new ArrayList<OrderItem>();
//		
//		String query = "SELECT * FROM orderitem WHERE order_id = ?";
//		
//		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, orderId);
//		
//		for(Map<String, Object> searchResultMap : resultList) {
//			OrderItem orderItem1 = new OrderItem();
//			
//	        Order order = new Order();
//	        order.setOrderid(((Number) searchResultMap.get("order_id")).longValue());
//	        orderItem1.setOrder(order);
//	        
//	        Item item = new Item();
//	        item.setItemid(((Number) searchResultMap.get("item_id")).longValue());
//	        orderItem1.setItem(item);
//	        
//			orderItem1.setQuantity((int)searchResultMap.get("quantity"));
//			orderItem1.setOrderitemid((Long)searchResultMap.get("orderitemid"));
//			
//			allItemsList.add(orderItem1);
//		}
//		
//		return allItemsList;
//	}
	public List<OrderItem> getAllItems(Long orderId) {
		
		System.out.println("OrderItemRepositoryのげっとおーるあいてむ使われたよん！");
		
	    List<OrderItem> allItemsList = new ArrayList<>();

	    String query =
	    	    "SELECT " +
	    	    "oi.orderitemid, oi.quantity, oi.status, " +  // ← ここを追加！✨
	    	    "o.orderid AS o_orderid," +
	    	    "i.itemid AS i_itemid, i.name AS i_name, i.price AS i_price, " +
	    	    "i.picturelink AS i_picturelink, i.detail AS i_detail, i.genre AS i_genre " +
	    	    "FROM orderitem oi " +
	    	    "JOIN orders o ON oi.order_id = o.orderid " +
	    	    "JOIN item i ON oi.item_id = i.itemid " +
	    	    "WHERE oi.order_id = ?";

	    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, orderId);

	    for (Map<String, Object> row : resultList) {
	        // Order
	        Order order = new Order();
	        order.setOrderid(((Number) row.get("o_orderid")).longValue());

	        // Item
	        Item item = new Item();
	        item.setItemid(((Number) row.get("i_itemid")).longValue());
	        item.setName((String) row.get("i_name"));
	        item.setPrice(((Number) row.get("i_price")).intValue());
	        item.setPicturelink((String) row.get("i_picturelink"));
	        item.setDetail((String) row.get("i_detail"));
	        item.setGenre((String) row.get("i_genre"));

	        // OrderItem
	        OrderItem orderItem = new OrderItem();
	        orderItem.setOrderitemid(((Number) row.get("orderitemid")).longValue());
	        orderItem.setQuantity(((Number) row.get("quantity")).intValue());
	        orderItem.setOrder(order);
	        orderItem.setItem(item);
	        orderItem.setStatus((String)row.get("status"));
	        System.out.println("オーダーアイテムのステータス取得確認："+orderItem.getStatus());

	        allItemsList.add(orderItem);
	    }

	    return allItemsList;
	}
	
	public void updateStatusToPurchased(Long orderId) {
		String query = "UPDATE orderitem SET status = 'purchased' WHERE order_id = ?";
		jdbcTemplate.update(query, orderId);
	}
	
	public int getQuantity(Long orderId, Long itemId) {
		String query = "SELECT quantity FROM orderitem WHERE order_id = ? AND item_id = ? AND status = 'cart'";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query,orderId,itemId);
		Map<String, Object> resultMap = resultList.get(0);
		return (int)resultMap.get("quantity");
	}
	
	public void changeQuantity(Long orderid, Long itemid, int delta) {
		String query = "UPDATE orderitem SET quantity = quantity + ? WHERE order_id = ? AND item_id = ? AND status = 'cart'";
		jdbcTemplate.update(query, delta, orderid, itemid);
	}
	
	public void clearCart(Long orderid) {
		String query = "DELETE FROM orderitem WHERE order_id = ?";
		jdbcTemplate.update(query, orderid);
	}
	
	public int statusCartItems(Long orderId, Long itemId) {
		String query = "SELECT * FROM orderitem WHERE order_id = ? AND item_id = ? AND status = 'cart'";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query,orderId,itemId);
		Map<String, Object> resultMap = resultList.get(0);
		int cartAmount = (int)resultMap.get("quantity");
		return cartAmount;
	}
}
