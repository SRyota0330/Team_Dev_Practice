package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.ItemRepository;

@Service
public class ItemService {
	@Autowired
	ItemRepository itemRepository;
	
	public void addItem(Item item) {
		itemRepository.addItem(item);
	}
	
//	public void dellItem(Long itemId) {
//		itemRepository.dellItem(itemId);
//	}
	
	public void editItem(Item item) {
		itemRepository.editItem(item);
	}
	
	public List<Item> getAllItem() {
		return itemRepository.getAllItem();
	}
	
	public List<Item> searchItemFromGenre(String genre){
		return itemRepository.searchItemFromGenre(genre);
	}
	

	public List<Item> searchItemFromName(String keywords){
		return itemRepository.searchItemFromName(keywords);
	}
	
	public Item itemDetail(Long id) {
		return itemRepository.itemDetail(id);
	}
}
