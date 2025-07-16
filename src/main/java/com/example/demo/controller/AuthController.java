package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@Controller
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/login")
	public String login(Model model, User user) {
	    // もしログイン時にユーザー情報を表示する必要がなければ削除可能
	    // List<User> allUser = userService.getAllUser();
//		model.addAttribute("user", new User());
	    return "user/login";  // login.htmlに遷移
	}

	@PostMapping("/index")
	public String submitForm(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "/";  // エラーがあればフォームに戻る
	    }

	    // userVerifyメソッドを使って認証
	    User verifiedUser = userService.userVerify(user.getMailaddress(), user.getPassword());
	    if (verifiedUser != null) {
	        model.addAttribute("user", verifiedUser);
	        return "user/logged";  // 認証成功後、トップページへ遷移
	    } else {
	        model.addAttribute("error", "再度入力してください");
	        return "user/login";  // 認証失敗時に再度ログインページ
	    }
	}

	
	@GetMapping("/signup")
	public String register(Model model) {
	    model.addAttribute("user", new User());  // 新しいUserオブジェクトをフォームにバインディング
	    return "register";  // register.htmlに遷移
	}

	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();  // セッションを無効化
	    return "redirect:user/login";  // ログアウト後にログインページへ遷移
	}

	
//	ログアウトボタンが押されたらトップページへ遷移

}
