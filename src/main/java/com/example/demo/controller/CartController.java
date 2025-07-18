package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
		if(userid != null) {
//			User loggedUser = userService.getOneUser(userid);
			Order loggedUserOrder = orderService.getOrderFromUser(userid);
			Long orderId = orderService.getOrderId(loggedUserOrder);
			List<OrderItem> orderItem = cartService.getAllItems(orderId);
			model.addAttribute("itemList", cartService.getItemFromOrderItem(orderItem));
			return "purchace/cartList";
		}else {
			return "redirect:user/login";
		}
	}

}
