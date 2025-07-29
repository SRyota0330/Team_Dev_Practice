package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Item;
import com.example.demo.repository.StockRepository;

@Service
public class StockService {
	@Autowired
	StockRepository stockRepository;
	
	public void manageCount(int count,Item item) {
		stockRepository.manageCount(count,item);
	}
	
	public void addCount(int count, Item item) {
		stockRepository.addCount(count, item);
	}
	
	public int getCount(Long itemid) {
		return stockRepository.getCount(itemid);
	}
	
	

}
