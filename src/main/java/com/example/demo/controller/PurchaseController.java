package com.example.demo.controller;
 
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.StockService;
import com.example.demo.service.UserService;
 
@Controller
public class PurchaseController {
	
	@Autowired CartService cartService;
	@Autowired UserService userService;
	@Autowired StockService stockService;
	@Autowired OrderItemRepository orderItemRepository;
	
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
		List<OrderItem> orderItemList = cartService.getCartItemFromOrderItem(orderItems);
		List<Item> itemList = cartService.getItemFromOrderItem(orderItemList);
		//金額計算
		
		if(orderItemList.isEmpty()) {
			System.out.println("カートがからっぽ");
			
			model.addAttribute("itemList", orderItemList);
			model.addAttribute("emptyCart", "カートに商品を追加してください");
			return "purchase/cartList";
		}else {
			int total = 0;
			for(OrderItem oi : orderItemList) {
				total += oi.getQuantity() * oi.getItem().getPrice();
			}
			//VIEWに渡す
			model.addAttribute("itemList", itemList);
			model.addAttribute("orderItemList", orderItemList);
			model.addAttribute("total", total);
			model.addAttribute("address", address);
			return "purchase/cashRegister";
		}
	}
	
	//購入確定ボタン
	@PostMapping(value="/confirm")
	public String confirm(HttpSession session, Model model, RedirectAttributes redirectAttributes){
		//セッションからログインユーザーを取得
		Long userid = (Long) session.getAttribute("userid");
		User loginUser = userService.userDetail(userid);
		
		//ユーザーのordersテーブルを取得
		Order order = loginUser.getOrder();
		
		//ordersテーブルに紐ついているOrderItemテーブル（注文の中身）を取得
		List<OrderItem> allOrderItems = cartService.getAllItems(order.getOrderid());
		List<OrderItem> orderItems = cartService.getCartItemFromOrderItem(allOrderItems);
		
		boolean result = true;
		List<Item> lessItemList = new ArrayList<>();
		
		int stockAmount = 0;
		int orderAmount = 0;
		
		for(OrderItem oi : orderItems) {
			stockAmount = stockService.getCount(oi.getItem().getItemid());
			orderAmount = orderItemRepository.statusCartItems(oi.getOrder().getOrderid(), oi.getItem().getItemid());
			
			System.out.println(stockAmount +"と"+ orderAmount);
			
			System.out.println(stockAmount + "在庫" + orderAmount + "注文数");
			if(stockAmount-orderAmount<0) {
				result = false;
				Item lessItem = oi.getItem();
				lessItemList.add(lessItem);
			}
		}
		
		System.out.println("在庫たるか"+result);
		System.out.println(lessItemList);
		
		
		if(result==true) {
			//カートのアイテムの数の分だけ「残り在庫数=在庫数-注文個数」を実施
			for(OrderItem oi : orderItems) {
				int count = 0;
				count = stockService.getCount(oi.getItem().getItemid()); //在庫数の取得
				int quantity = oi.getQuantity(); //注文個数の取得
				int reststock = count - quantity; //残り在庫数=在庫数-注文個数
				
				stockService.manageCount(reststock, oi.getItem()); //StockServiceのmanageCountメソッドに渡す
			}
			
			//該当のOrderidの商品のステータスを"purchased"に変更
			cartService.updateStatusToPurchased(order.getOrderid());
			
			
			//ユーザーのpurchasedステータスの購入リストをconsole表示
			//ユーザーのorderテーブルを取得
			Order userOrder = loginUser.getOrder();
			//商品情報を取得
			List<OrderItem> orderItemList = cartService.getAllItems(order.getOrderid());
			System.out.println("purchasedステータスのリスト："+cartService.getPurchasedItemFromOrderItem(orderItemList));
			
			//カートの中身を削除
//			cartService.clearCart(order.getOrderid());
			
			//購入完了ページに遷移
			return "purchase/purchased";
		}else {
			redirectAttributes.addFlashAttribute("errorMessage", "は在庫不足です");
			redirectAttributes.addFlashAttribute("lessItemList", lessItemList);
			return "redirect:/cart";
		}
		

	}
	
	
}