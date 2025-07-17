package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

@Controller
public class TopController {
    @Autowired
    private ItemService itemService;
    
	@GetMapping(value="/")
	public String toppage(Model model) {
		List<Item> itemList = itemService.getAllItem();
		model.addAttribute("items",itemList);
		return "top";
	}

}
