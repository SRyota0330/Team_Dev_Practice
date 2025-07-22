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
	
	public Item getItemDetail(Item item) {
		return itemRepository.getOneItemFromId(item.getItemid());
	}
	
	public Item getOneItemFromId(Long itemId) {
		return itemRepository.getOneItemFromId(itemId);
	}

}
