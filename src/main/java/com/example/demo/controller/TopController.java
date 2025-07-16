package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TopController {
	@GetMapping(value="/")
	public String index(Model model) {
		model.addAttribute("message","Hello World");
		return "index";
	}

}
