package com.mavriksc.controller;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class IndexController {
	@RequestMapping("/")
    String String (Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "index";
    }
	
	@RequestMapping("/o")
    String other(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "other";
    }
	
	@RequestMapping("/c")
    String cover(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "cover";
    }
}
