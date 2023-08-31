package com.milanuo.springboot2mybatisforum.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminLoginPage {

    @GetMapping("/login")
    public String loginPage(){
        return "/admin/login";
    }

    @GetMapping("/index")
    public String index(){
        return "/admin/index";
    }

    @GetMapping("/page403")
    public String page403(){
        return "/admin/page403";
    }
}
