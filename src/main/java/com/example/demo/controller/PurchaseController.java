package com.example.demo.controller;
 
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.service.CartService;
import com.example.demo.service.StockService;
import com.example.demo.service.UserService;
 
@Controller
public class PurchaseController {
	
	@Autowired CartService cartService;
	@Autowired UserService userService;
	@Autowired StockService stockService;
	
	//レジに進むメソッド
	@GetMapping(value="/checkout")
	public String checkout(HttpSession session, Model model) {
		//セッションからログインユーザーを取得
		Long userid = (Long) session.getAttribute("userid");
		System.out.println("ユーザIDは"+userid);
		User loginUser = userService.userDetail(userid);
		//ユーザーの住所を取得
		System.out.println("test");
		String address = userService.getAddressFromUser(userid);
		System.out.println("test");
		//ユーザーのorderテーブルを取得
		Order order = loginUser.getOrder();
		//商品情報を取得
		List<OrderItem> orderItems = cartService.getAllItems(order.getOrderid());
		List<Item> itemList = cartService.getItemFromOrderItem(orderItems);
		//金額計算
		int total = 0;
		for(OrderItem oi : orderItems) {
			total += oi.getQuantity() * oi.getItem().getPrice();
		}
		//VIEWに渡す
		model.addAttribute("itemList", itemList);
		model.addAttribute("total", total);
		model.addAttribute("address", address);
		return "purchase/cashRegister";
	}
	//カートの中身個数を調整するメソッド

	//カートから削除するメソッド
	
	//購入確定ボタン
	@PostMapping(value="/confirm")
	public String confirm(HttpSession session, Model model){
		//セッションからログインユーザーを取得
		Long userid = (Long) session.getAttribute("userid");
		User loginUser = userService.userDetail(userid);
		
		//ユーザーのordersテーブルを取得
		Order order = loginUser.getOrder();
		
		//ordersテーブルに紐ついているOrderItemテーブル（注文の中身）を取得
		List<OrderItem> orderItems = cartService.getAllItems(order.getOrderid());
		
		//カートのアイテムの数の分だけ「残り在庫数=在庫数-注文個数」を実施
		for(OrderItem oi : orderItems) {
			int count = 0;
			count = stockService.getCount(oi.getItem().getItemid()); //在庫数の取得
			int quantity = oi.getQuantity(); //注文個数の取得
			int reststock = count - quantity; //残り在庫数=在庫数-注文個数
			
			stockService.manageCount(reststock, oi.getItem()); //StockServiceのmanageCountメソッドに渡す
		}
		
		cartService.updateStatusToPurchased(order.getOrderid()); //該当のOrderidの商品のステータスを"purchased"に変更
		
		//購入完了ページに遷移
		return "purchase/purchased";
	}
	
	
}