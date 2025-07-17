package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;

@Repository
public class StockRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void manageCount(int count,Item item) {
	    String query = "UPDATE stock SET count = ?, WHERE item_id = ?";
	    jdbcTemplate.update(query,count,item);
	}

}

