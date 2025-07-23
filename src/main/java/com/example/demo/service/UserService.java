package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OrderService orderService;
	
	public void addUser(User user) {
		userRepository.addUser(user);
	}
	
	public void editUser(User user) {
		userRepository.editUser(user);
	}
	
	public User getOneUser(Long id) {
		return userRepository.getOneUser(id);
	}
	
	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}
	
	public User userDetail(Long userid) {
		Map<String, Object> resultMap = userRepository.userDetail(userid);
		
		User user = new User();
		
		user.setUserid((Long)resultMap.get("userid"));
		user.setName((String)resultMap.get("name"));
		user.setMailaddress((String)resultMap.get("mailaddress"));
		user.setAddress((String)resultMap.get("Address"));
		user.setPassword((String)resultMap.get("password"));
		user.setOrder(orderService.getOrderFromUser(userid));
		
		return user;
	}
	
	public User userVerify(String mail, String password) {
		System.out.println("サービス");
	    return userRepository.userVerify(mail, password);
	}
	
	
	

}
