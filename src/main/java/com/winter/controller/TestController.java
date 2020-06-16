package com.winter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author:Jingqi Wu
 * @date: 2020/4/15
 */
@Controller
public class TestController {
    @RequestMapping(value = "hello1")
    public String student() {
        return "hello";
    }

}
