package com.obss.jsi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TemplateController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "mylogin";
    }

}
