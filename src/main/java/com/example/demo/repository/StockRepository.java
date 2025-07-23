package com.example.demo.repository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;

@Repository
public class StockRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void manageCount(int count,Item item) {
	    String query = "UPDATE stock SET count = ? WHERE item_id = ?";
	    jdbcTemplate.update(query,count,item.getItemid());
	}
	
	public void addCount(int count, Item item) {
		String query = "INSERT INTO stock (count, item_id) VALUES (?, ?)";
		jdbcTemplate.update(query,count,item.getItemid());
	}
	
	public int getCount(Long itemid) {
		String query = "SELECT count FROM stock WHERE item_id = ? LIMIT 1";
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,itemid);
		Map<String, Object> resultMap = searchResultList.get(0);
		return (int)resultMap.get("count");
	}
	

}

