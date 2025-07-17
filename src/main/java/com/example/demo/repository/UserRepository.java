package com.example.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;

@Repository
public class UserRepository{
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	UserRepository(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void addUser(User user) {
	    String query = "INSERT INTO USERS (NAME, MAIL, ADDRESS, PASSWORD) VALUES (?, ?, ?, ?)";
	    jdbcTemplate.update(query, user.getName(), user.getMailaddress(), user.getAddress(), user.getPassword());
	}
	
	public void dellUser(int userId) {
	    String query = "DELETE FROM USERS WHERE ID = ?";
	    jdbcTemplate.update(query, userId);
	}
	
	public void editUser(User user) {
	    String query = "UPDATE USERS SET NAME = ?, MAIL = ?, ADDRESS = ?, PASSWORD = ? WHERE ID = ?";
	    jdbcTemplate.update(query, user.getName(), user.getMailaddress(), user.getAddress(), user.getPassword(), user.getUserid());
	}


	
	public List<User> getAllUser(){
		List<User> allUserList = new ArrayList<User>();
		
		String query = "SELECT * FROM USERS";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query);
		
		for(Map<String,Object> resultMap : searchResultList) {
			User user = new User();
			
			user.setName((String)resultMap.get("NAME"));
			user.setUserid((Long)resultMap.get("ID"));
			user.setMailaddress((String)resultMap.get("MAIL"));
			user.setAddress((String)resultMap.get("ADDRESS"));
			user.setPassword((String)resultMap.get("PASSWORD"));
			
			allUserList.add(user);
		}
		return allUserList;
	}
	
//	public User userVerify(String mail, String password) {
//	    User loginUser = new User();
//
//	    // パラメータを使ったSQLクエリに修正
//	    String query = "SELECT * FROM USERS WHERE MAIL = ? AND PASSWORD = ?";
//
//	    // パラメータを安全に設定
//	    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, mail, password);
//
//	    // ユーザーが見つかった場合、Userオブジェクトを作成
//	    if (!resultList.isEmpty()) {
//	        Map<String, Object> resultMap = resultList.get(0);  // 最初の結果を取り出す
//	        loginUser.setName((String) resultMap.get("NAME"));
//	        loginUser.setId((int) resultMap.get("ID"));
//	        loginUser.setMail((String) resultMap.get("MAIL"));
//	        loginUser.setAddress((String) resultMap.get("ADDRESS"));
//	        loginUser.setPassword((String) resultMap.get("PASSWORD"));
//	    }
//
//	    return loginUser;
//	}
	
	public User userVerify(String mail, String password) {
	    String query = "SELECT * FROM users WHERE mailaddress = ?";
//	    mail="test@mail";
	    List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, mail);
	    
	    System.out.println(resultList);

	    if (resultList.isEmpty()) {
	    	System.out.println("User is null");
	        return null;  // ユーザーが見つからない場合はnullを返す
	    }

	    Map<String, Object> resultMap = resultList.get(0);
	    String tablePassword = (String) resultMap.get("password");

	    if (password.equals(tablePassword)) {
	        User loginUser = new User();
	        loginUser.setName((String) resultMap.get("name"));
	        loginUser.setUserid((Long) resultMap.get("userid"));
	        loginUser.setMailaddress((String) resultMap.get("mailaddress"));
	        loginUser.setAddress((String) resultMap.get("address"));
	        return loginUser;  // パスワードが一致した場合のみUserオブジェクトを返す
	    }

	    return null;  // パスワードが一致しない場合
	}



}
