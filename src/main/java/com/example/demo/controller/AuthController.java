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
import com.example.demo.service.ItemService;
import com.example.demo.service.UserService;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    private ItemService itemService;

    // ログイン画面へ遷移
    @GetMapping("/login")
    public String login(Model model, User user) {
        return "user/login";
    }

    // ログイン処理
    @PostMapping("/logged")
    public String submitForm(@ModelAttribute("user") @Valid User user, BindingResult result, HttpSession session,
            Model model) {
        if (result.hasErrors()) {
            return "user/login";
        }

        User verifiedUser = userService.userVerify(user.getMailaddress(), user.getPassword());
        if (verifiedUser != null) {
            session.setAttribute("userid", verifiedUser.getUserid());

            Long userid = (Long) session.getAttribute("userid");

            if (userid == 1) {
                model.addAttribute("you", verifiedUser);
                model.addAttribute("allItem", itemService.getAllItem());
                model.addAttribute("allUser", userService.getAllUser());
                return "admin/manageItemsAndUsers";
            } else {
                model.addAttribute("mail", "セッション情報⇒" + userid);
                System.out.println("ユーザーのメールアドレス: " + userid);
                model.addAttribute("you", verifiedUser);
                return "redirect:/";
            }
        } else {
            model.addAttribute("mail", "ログインしていません");
            System.out.println("ユーザーはログインしていません");
            return "redirect:/login";
        }
    }

    // ユーザー登録処理
    @PostMapping("/signup")
    public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/signup";
        }
        userService.addUser(user);
        return "redirect:/login";
    }

    // ユーザー登録画面へ
    @GetMapping("/signup")
    public String signup(Model model, User user) {
        model.addAttribute("user", new User());
        return "user/signup";
    }

    // ログアウト処理
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
//
//package com.example.demo.controller;
//
//import jakarta.servlet.http.HttpSession;
//import jakarta.validation.Valid;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import com.example.demo.entity.User;
//import com.example.demo.service.ItemService;
//import com.example.demo.service.UserService;
//
//@Controller
//public class AuthController {
//	
//	//test0724
//	
//	@Autowired
//	UserService userService;
//	
//	@Autowired
//    private ItemService itemService;
//	
//	
//	//login.htmlへ遷移
//	@GetMapping("/login")
//	public String login(Model model, User user) {
//	    return "user/login";
//	}
//	
//	// ログイン処理とセッション管理
//	@PostMapping("/logged")
//	public String submitForm(@ModelAttribute("user") @Valid User user, BindingResult result, HttpSession session, Model model) {
//	    if (result.hasErrors()) {
//	        return "user/login"; 
//	    }
//
//	    // userVerifyメソッドでユーザー認証
//	    User verifiedUser = userService.userVerify(user.getMailaddress(), user.getPassword());
//	    if (verifiedUser != null) {
//	        // 認証成功した場合、セッションにユーザー情報を保存
//	        session.setAttribute("userid", verifiedUser.getUserid());
//	        
//		    // セッションから 'userid' を取得
//		    Long userid = (Long) session.getAttribute("userid");
//		    
//		    if(userid == 1) {
//		    	model.addAttribute("you", verifiedUser);
//				model.addAttribute("allItem", itemService.getAllItem());
//				model.addAttribute("allUser", userService.getAllUser());
//				return "admin/manageItemsAndUsers";
//		    }else {
//		    	model.addAttribute("mail", "セッション情報⇒" + userid);
//		        System.out.println("ユーザーのメールアドレス: " + userid);
//		        model.addAttribute("you", verifiedUser);
//		        return "redirect:/";
//		        
//		    }
//		    // セッションに 'userid' が存在するか確認
//	        
//		 }else {
//		    model.addAttribute("mail", "ログインしていません");
//	        System.out.println("ユーザーはログインしていません");
//	        return "redirect:/login";
//		 }
//
//	    
//	}
//
//	@PostMapping("/signup")
//	public String register(@ModelAttribute("user") @Valid User user, BindingResult result, Model model) {
//	    if (result.hasErrors()) {
//	        return "user/signup";  // バリデーションエラーがあればフォームに戻る
//	    }
//	    userService.addUser(user);  // ユーザー登録処理
//	    return "redirect:/login";  // 登録後にログインページにリダイレクト
//	}
//
//	@GetMapping("/signup")
//	public String signup(Model model, User user) {
//	    model.addAttribute("user", new User());  // 新しいUserオブジェクトをフォームにバインディング
//	    return "user/signup";  // signup.htmlに遷移
//	}
//	
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//	    session.invalidate();  // セッションを無効化
//	    return "redirect:user/login";  // ログアウト後にログインページへ遷移
//	}
//}
