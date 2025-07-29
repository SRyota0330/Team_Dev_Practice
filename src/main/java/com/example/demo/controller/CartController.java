package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Item;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;

@Controller
public class CartController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	OrderService orderService;
	
	@GetMapping("/cart")
	public String cart(Model model, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		Order order = userService.userDetail(userid).getOrder();
		List<OrderItem> allOrderItems = cartService.getAllItems(order.getOrderid());
		List<OrderItem> orderItems = cartService.getCartItemFromOrderItem(allOrderItems); 
		model.addAttribute("itemList", orderItems);
//		if(userid != null) {
//			Order loggedUserOrder = orderService.getOrderFromUser(userid);
//			Long orderId = orderService.getOrderId(loggedUserOrder);
//			List<OrderItem> orderItem = cartService.getAllItems(orderId);
//			model.addAttribute("itemList", cartService.getItemFromOrderItem(orderItem));
		return "purchase/cartList";
//		}else {
//			return "redirect:/user/login";
//		}
	}
	
	@PostMapping("/cart/add")
	public String cartAdd(@RequestParam("itemid") Long itemid,
						  @RequestParam("quantity") int quantity,
						  HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		if (userid != null) {
			Order order = orderService.getOrderFromUser(userid);
			if(order == null) {
				orderService.addRecord(userid);
				order = orderService.getOrderFromUser(userid);
			}
			
			Item item = new Item();
			item.setItemid(itemid); //商品をセット
			cartService.addItemToCart(order, item, quantity); //カートに追加
			return "redirect:/cart"; // カート一覧へリダイレクト
		}else {
			return "redirect:/login";
		}
	}
	
	@PostMapping("/cart/increase")
	public String increaseItem(@RequestParam Long itemid, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		Order order = userService.userDetail(userid).getOrder();
		cartService.changeQuantity(order, itemid, +1);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/decrease")
	public String decreaseItem(@RequestParam Long itemid, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		Order order = userService.userDetail(userid).getOrder();
		cartService.changeQuantity(order, itemid, -1);
		return "redirect:/cart";
	}
	
	@PostMapping("/cart/delete")
	public String delItemFromCart(@RequestParam Long itemid, HttpSession session) {
		Long userid = (Long) session.getAttribute("userid");
		Order order = userService.userDetail(userid).getOrder();
		cartService.delItemFromCart(order, itemid);
		return "redirect:/cart";
	}
}
