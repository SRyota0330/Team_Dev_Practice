package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Item;

@Repository
public class ItemRepository {
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public void addItem(Item item) {
	    String query = "INSERT INTO item (name,price,picturelink,detail,genre) VALUES (?, ?, ?, ?,?)";
	    jdbcTemplate.update(query, item.getName(), item.getPrice(), item.getPicturelink(), item.getDetail(),item.getGenre());
	}
	
	public void dellItem(Long itemid) {
	    String query = "DELETE FROM item WHERE ID = ?";
	    jdbcTemplate.update(query, itemid);
	}
	
	public void editItem(Item item) {
	    String query = "UPDATE item SET name = ?, price = ?, picturelink = ?, detail = ?,genre = ?  WHERE ID = ?";
	    jdbcTemplate.update(query, item.getName(), item.getPrice(), item.getPicturelink(), item.getDetail(), item.getGenre(), item.getItemid());
	}

	public List<Item> getAllItem(){
		List<Item> allItemList = new ArrayList<Item>();
		
		String query = "SELECT * FROM item";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query);
		
		for(Map<String,Object> resultMap : searchResultList) {
			Item item = new Item();
			
			item.setName((String)resultMap.get("name"));
			item.setItemid((Long)resultMap.get("itemid"));
			item.setPrice((int)resultMap.get("price"));
			item.setPicturelink((String)resultMap.get("picturelink"));
			item.setDetail((String)resultMap.get("detail"));
			item.setGenre((String)resultMap.get("genre"));
			
			allItemList.add(item);
		}
		return allItemList;
	}
	
	public List<Item> searchItemFromGenre(String genre){
		List<Item> allItemList = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE genre = ?";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,genre);
		
		for(Map<String,Object> resultMap : searchResultList) {
			Item item = new Item();
			
			item.setName((String)resultMap.get("name"));
			item.setItemid((Long)resultMap.get("itemid"));
			item.setPrice((int)resultMap.get("price"));
			item.setPicturelink((String)resultMap.get("picturelink"));
			item.setDetail((String)resultMap.get("detail"));
			item.setGenre((String)resultMap.get("genre"));
			
			allItemList.add(item);
		}
		return allItemList;
	}
	
	public List<Item> searchItemFromName(String name){
		List<Item> allItemList = new ArrayList<Item>();
		
		String query = "SELECT * FROM item WHERE name = ?";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,name);
		
		for(Map<String,Object> resultMap : searchResultList) {
			Item item = new Item();
			
			item.setName((String)resultMap.get("name"));
			item.setItemid((Long)resultMap.get("itemid"));
			item.setPrice((int)resultMap.get("price"));
			item.setPicturelink((String)resultMap.get("picturelink"));
			item.setDetail((String)resultMap.get("detail"));
			item.setGenre((String)resultMap.get("genre"));
			
			allItemList.add(item);
		}
		return allItemList;
	}
	
	public Item getOneItemFromId(Long id){
		
		String query = "SELECT * FROM item WHERE itemid = ? LIMIT 1";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,id);
		 Map<String, Object> resultMap = searchResultList.get(0);
			Item item = new Item();
			
			item.setName((String)resultMap.get("name"));
			item.setItemid((Long)resultMap.get("itemid"));
			item.setPrice((int)resultMap.get("price"));
			item.setPicturelink((String)resultMap.get("picturelink"));
			item.setDetail((String)resultMap.get("detail"));
			item.setGenre((String)resultMap.get("genre"));
			
		return item;
	}

}
