package com.winter.controller;

import com.winter.model.Homework;
import com.winter.model.User;
import com.winter.service.HomeworkService;
import com.winter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Created by Administrator on 2017/8/16.
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HomeworkService homeworkService;
    private User user;
    private ModelAndView mv = new ModelAndView();

    @RequestMapping(value = "/register", produces = {"application/json;charset=UTF-8"})
    public String register() {
        return "register";
    }

    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
    public String login() {
        return "login";
    }

    //@ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8"})
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/user/login";
    }

    @RequestMapping(value = "/verify", produces = {"application/json;charset=UTF-8"})
    public String selectUser(@RequestParam("account") String account,
                             @RequestParam("password") String password,
                             @RequestParam("type") String type) {
        user = userService.selectUser(account);
        System.out.println(user.getPassword().equals(password));
        if (user.getPassword().equals(password)) {
            if (user.getType() == 0) {
                mv.setViewName("student");
                return "student";
            } else if (user.getType() == 1) {
                return "redirect:teacher";
            }
        }
        return "redirect:login";

    }

    @RequestMapping(value = "/teacher", produces = {"application/json;charset=UTF-8"})
    public ModelAndView teacher() {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectAll();
        mv.setViewName("teacher");
        mv.addObject("list", list);
        return mv;
    }
    @RequestMapping(value = "/student", produces = {"application/json;charset=UTF-8"})
    public ModelAndView student() {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectAll();
        mv.setViewName("teacher");
        mv.addObject("list", list);
        return mv;
    }
    @RequestMapping(value = "/selectsh", produces = {"application/json;charset=UTF-8"})
    public ModelAndView selectsh(@RequestParam("hid") long hid) {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectAll();
        mv.setViewName("teacher");
        mv.addObject("list", list);
        return mv;
    }
}

