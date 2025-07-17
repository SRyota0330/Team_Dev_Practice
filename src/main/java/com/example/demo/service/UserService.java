package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public void addUser(User user) {
		userRepository.addUser(user);
	}
	
	public void dellUser(User user) {
		
	}
	
	public void editUser() {
		
	}
	
	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}
	
//	public User getUserDetail() {
//		return userDetail;
//	}
//	
	public User userVerify(String mail, String password) {
	    return userRepository.userVerify(mail, password);
	}

	
	
	

}
