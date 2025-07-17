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
	
	
	//top.htmlに繊維
	@GetMapping("/top")
	public String top(Model model){
		return "/top";
	}
	
	//login.html
	@GetMapping("/login")
	public String login(Model model, User user) {
	    return "user/login";  // login.htmlに遷移
	}
	
	// ログイン処理とセッション管理を一貫して行うメソッド
	@PostMapping("/top")
	public String submitForm(@ModelAttribute("user") @Valid User user, BindingResult result, HttpSession session, Model model) {
	    if (result.hasErrors()) {
	        return "user/login";  // バリデーションエラーがあればログインページに戻る
	    }

	    // userVerifyメソッドでユーザー認証
	    User verifiedUser = userService.userVerify(user.getMailaddress(), user.getPassword());
	    if (verifiedUser != null) {
	        // 認証成功した場合、セッションにユーザー情報を保存
	        session.setAttribute("userid", verifiedUser.getMailaddress());

	        // 認証後、トップページに遷移
	        model.addAttribute("user", verifiedUser);
	        return "top";  // 認証成功後、トップページに遷移
	    } else {
	        // 認証失敗した場合、エラーメッセージを表示して再度ログインページへ
	        model.addAttribute("error", "再度入力してください");
	        return "user/login";
	    }
	}


	@PostMapping("/signup")
	public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
	    if (result.hasErrors()) {
	        return "user/signup";  // バリデーションエラーがあればフォームに戻る
	    }
	    userService.addUser(user);  // ユーザー登録処理
	    return "redirect:/login";  // 登録後にログインページにリダイレクト
	}

	
	@GetMapping("/signup")
	public String signup(Model model, User user) {
	    model.addAttribute("user", new User());  // 新しいUserオブジェクトをフォームにバインディング
	    return "user/signup";  // signup.htmlに遷移
	}
	
	
	@GetMapping("/top-session")
    public String top(HttpSession session) {
        // セッションからメールアドレスを取得
        String mailaddress = (String) session.getAttribute("mailaddress");

        if (mailaddress == null) {
            return "redirect:/login"; // ユーザーがログインしていなければ、ログインページにリダイレクト
        }

        // ログインしていれば、ホームページを表示
        return "top";
    }
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();  // セッションを無効化
	    return "redirect:user/login";  // ログアウト後にログインページへ遷移
	}

	
//	ログアウトボタンが押されたらトップページへ遷移

}
