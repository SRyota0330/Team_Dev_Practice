package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Item;
import com.example.demo.entity.User;
import com.example.demo.service.ItemService;
import com.example.demo.service.S3Service;
import com.example.demo.service.StockService;
import com.example.demo.service.UserService;

@Controller
public class ManageController {
	
	@Autowired
	ItemService itemService;
	@Autowired
	StockService stockService;
	@Autowired
	S3Service s3Service;
	@Autowired
	UserService userService;
	
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
	        Item item = itemService.itemDetail(itemId);
	        int count = stockService.getCount(itemId);
	        model.addAttribute("item", item); // 商品情報をモデルに追加
	        model.addAttribute("count", count);
	        return "admin/editItems"; // 編集ページに遷移
	    } else {
	        return "user/login"; // ログインページに遷移
	    }
	}

	
	@GetMapping("/editUsers/{userId}")
	public String editUsers(@PathVariable Long userId, Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			
			User user = userService.userDetail(userId);
			model.addAttribute("user", user);
			return "admin/editUsers";
		}else {
			return "user/login";
		}
	}
	
	
//	@PostMapping("/editItems")
//	public String manageTop(@RequestParam("count") int count, Item item, Model model, HttpSession session) {
//		Long userid = (Long) session.getAttribute("userid");
//		if(userid == 1) {
//			itemService.editItem(item);
//			stockService.manageCount(count, item);
//			model.addAttribute("allItem", itemService.getAllItem());
//			model.addAttribute("allUser", userService.getAllUser());
//			return "admin/manageItemsAndUsers";
//		}else {
//			return "user/login";
//		}
//	}
	
	@PostMapping("/editItems")
	public String manageTop(@RequestParam("count") int count,
	                        @RequestParam(value = "file", required = false) MultipartFile file,
	                        Item item, Model model, HttpSession session) {
	    Long userid = (Long) session.getAttribute("userid");
	    if (userid == 1) {
	        try {
	            // 新しい画像がアップロードされた場合のみ処理
	            if (file != null && !file.isEmpty()) {
	                String imageUrl = s3Service.uploadFile(file);
	                item.setPicturelink(imageUrl);
	            } else {
	                // 既存の画像URLを保持（DBから取得）
	                Item existingItem = itemService.itemDetail(item.getItemid());
	                if (existingItem != null) {
	                    item.setPicturelink(existingItem.getPicturelink());
	                }
	            }

	            itemService.editItem(item);
	            stockService.manageCount(count, item);

	            model.addAttribute("allItem", itemService.getAllItem());
	            model.addAttribute("allUser", userService.getAllUser());
	            return "admin/manageItemsAndUsers";
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("error", "商品編集に失敗しました: " + e.getMessage());
	            return "admin/manageItemsAndUsers";
	        }
	    } else {
	        return "user/login";
	    }
	}


	//	Itemテーブルのみ挿入
	@PostMapping("/addItems")
	public String addItems(@RequestParam("name") String name,
			@RequestParam("price") int price,
			@RequestParam("genre") String genre,
			@RequestParam("detail") String detail,
			@RequestParam("file") MultipartFile file,
			Model model, HttpSession session) {
		System.out.println("POST /addItems 受信しました");
		Long userid = (Long) session.getAttribute("userid");
		if (userid == 1) {

			try {
				String imageUrl = s3Service.uploadFile(file);

				Item item = new Item();

				item.setName(name);
				item.setPrice(price);
				item.setGenre(genre);
				item.setDetail(detail);
				item.setPicturelink(imageUrl);

				itemService.addItem(item);
				stockService.addCount(0, itemService.recentlyItem());
				model.addAttribute("allItem", itemService.getAllItem());
				model.addAttribute("allUser", userService.getAllUser());
				return "admin/manageItemsAndUsers";
			} catch (Exception e) {
				e.printStackTrace();
				model.addAttribute("error", "商品追加に失敗しました: " + e.getMessage());
				return "admin/manageItemsAndUsers";
			}
		} else {
			return "user/login";
		}
	}
	
	@PostMapping("/editUsers/{id}")
	public String editUser(@PathVariable("id") Long sessionUserid, User user, Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if(userid == 1) {
			user.setUserid(sessionUserid);
			System.out.println(user);
			userService.editUser(user);
			model.addAttribute("allItem", itemService.getAllItem());
			model.addAttribute("allUser", userService.getAllUser());
			return "admin/manageItemsAndUsers";
		}else {
			return "user/login";
		}
	}
	@GetMapping("/admin")
	public String adminHome(Model model, HttpSession session) {
	    Long userid = (Long) session.getAttribute("userid");
	    if (userid == 1) {
	        model.addAttribute("allItem", itemService.getAllItem());
	        model.addAttribute("allUser", userService.getAllUser());
	        return "admin/manageItemsAndUsers"; // 管理トップページHTML
	    } else {
	        return "user/login"; // 権限がない場合はログイン画面へ
	    }
	}
	@PostMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate(); // セッションを破棄
	    return "redirect:/login"; // ← このURLがログインページのURLに合ってるか確認！
	}
}
