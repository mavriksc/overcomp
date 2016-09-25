package com.mavriksc;

import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class IndexController {
	@RequestMapping("/")
    String index(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "index";
    }
	
	@RequestMapping("/ding")
    String other(Model model) {
        model.addAttribute("now", LocalDateTime.now());
        return "other";
    }
}
