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
	    String query = "INSERT INTO users (name, mailaddress, address, password) VALUES (?, ?, ?, ?)";
	    jdbcTemplate.update(query, user.getName(), user.getMailaddress(), user.getAddress(), user.getPassword());
	}
	
	public void dellUser(int userid) {
	    String query = "DELETE FROM users WHERE id = ?";
	    jdbcTemplate.update(query, userid);
	}
	
	public void editUser(User user) {
	    String query = "UPDATE users SET name = ?, mailaddress = ?, address = ?, password = ? WHERE userid = ?";
	    jdbcTemplate.update(query, user.getName(), user.getMailaddress(), user.getAddress(), user.getPassword(), user.getUserid());
	}
	
	public User getOneUser(Long id) {
		String query = "SELECT * FROM users WHERE userid = ? LIMIT 1";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query, id);
		Map<String, Object> resultMap = searchResultList.get(0);
		System.out.println(resultMap+"UserRepositoryのgetOneUser");
		
		User loginUser = new User();
        loginUser.setName((String) resultMap.get("name"));
        loginUser.setUserid((Long) resultMap.get("userid"));
        loginUser.setMailaddress((String) resultMap.get("mailaddress"));
        loginUser.setAddress((String) resultMap.get("address"));
        loginUser.setPassword((String)resultMap.get("password"));
        return loginUser;  
		
	}
	
	public List<User> getAllUser(){
		List<User> allUserList = new ArrayList<User>();
		
		String query = "SELECT * FROM users";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query);
		
		for(Map<String,Object> resultMap : searchResultList) {
			User user = new User();
			
			user.setName((String)resultMap.get("name"));
			user.setUserid((Long)resultMap.get("userid"));
			user.setMailaddress((String)resultMap.get("mailaddress"));
			user.setAddress((String)resultMap.get("address"));
			user.setPassword((String)resultMap.get("password"));
			
			allUserList.add(user);
		}
		return allUserList;
	}
	
	
	public User userVerify(String mail, String password) {
		System.out.println("リポジトリあたま");
	    String query = "SELECT * FROM users WHERE mailaddress = ?";
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
	
	public Map<String, Object> userDetail(Long userid){
		
		String query = "SELECT * FROM users WHERE userid = ? LIMIT 1";
		
		List<Map<String, Object>> searchResultList = jdbcTemplate.queryForList(query,userid);
		 Map<String, Object> resultMap = searchResultList.get(0);
			
		return resultMap;
	}
	
	public String getAddressFromUser(Long userId) {
		String query = "SELECT address FROM users WHERE userid = ?";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(query, userId);
		return (String) resultList.get(0).get("address");
	}
}
