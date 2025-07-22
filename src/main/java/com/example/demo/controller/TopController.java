package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

@Controller
public class TopController {
    @Autowired
    private ItemService itemService;
    
    //トップページ
	@GetMapping(value="/")
	public String toppage(Model model) {
		List<Item> itemList = itemService.getAllItem();
		model.addAttribute("items",itemList);
		return "top";
	}
	
	//検索結果一覧
	@GetMapping(value="/search")
	public String search(@RequestParam(name = "keywords", required = false) String keywords, Model model) {
		if(keywords == null || keywords.isBlank()) {
			List<Item> itemList = itemService.getAllItem();
			model.addAttribute("items", itemList);
			model.addAttribute("keywords", " ");
		}else {
			List<Item> itemList = itemService.searchItemFromName(keywords);
			model.addAttribute("items", itemList);
			model.addAttribute("keywords", keywords);
		}
		return "top";
	}
	
	//商品詳細ページへ遷移
	@GetMapping(value="/item/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		Item item = itemService.itemDetail(id);
		model.addAttribute("item", item);
		return "purchase/itemDetail";
	}
}