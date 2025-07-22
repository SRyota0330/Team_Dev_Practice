package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;

@Controller
public class ManageController {
	
	@Autowired
	ItemService itemService;
	
	@GetMapping("/addItems")
	public String addItems(Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			return "admin/addItems";
		}else {
			return "user/login";
		}
	}
	
	@GetMapping("/editItems/{itemId}")
	public String editItem(@PathVariable Long itemId, Model model, HttpSession session) {
	    Long userid = (Long) session.getAttribute("userid");
	    
	    if (userid == 1) { // 管理者の確認
	        // 商品IDを使って商品情報を取得
	        Item item = itemService.getOneItemFromId(itemId);
	        model.addAttribute("item", item); // 商品情報をモデルに追加
	        return "admin/editItems"; // 編集ページに遷移
	    } else {
	        return "user/login"; // ログインページに遷移
	    }
	}

	
	@GetMapping("/addUsers")
	public String addUsers(Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			return "admin/addItems";
		}else {
			return "user/login";
		}
	}
	
	@GetMapping("/editUsers")
	public String editUsers(Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			return "admin/addItems";
		}else {
			return "user/login";
		}
	}
	
	@PostMapping("/editItems")
	public String manageTop(Item item, Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			itemService.editItem(item);
			model.addAttribute("allItem", itemService.getAllItem());
			return "admin/manageItemsAndUsers";
		}else {
			return "user/login";
		}
	}
	
//	@PostMapping("/dellItems/{itemId}")
//	public String dellItems(@PathVariable Long itemId, Model model, HttpSession session) {
//		Long userid = (Long) session.getAttribute("userid");
//		if(userid == 1) {
//			itemService.dellItem(itemId);
//			return "redirect:/logged";
//		}else {
//			return "user/login";
//		}
//	}
	
	@PostMapping("/addItems")
	public String addItems(Item item, Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			itemService.addItem(item);
			model.addAttribute("allItem", itemService.getAllItem());
			return "admin/manageItemsAndUsers";
		}else {
			return "user/login";
		}
	}

	
	

}
