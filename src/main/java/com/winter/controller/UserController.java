package com.winter.controller;

import com.winter.model.Homework;
import com.winter.model.StudentHomework;
import com.winter.model.User;
import com.winter.service.HomeworkService;
import com.winter.service.StudentHomeworkService;
import com.winter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private HomeworkService homeworkService;
    @Autowired
    private StudentHomeworkService studentHomeworkService;

    private User user;
    private ModelAndView mv = new ModelAndView();
    private Date date = new Date();

    /**
     * 用户注册
     * @return
     */
    @RequestMapping(value = "/register", produces = {"application/json;charset=UTF-8"})
    public String register() {
        return "register";
    }

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"})
    public String login() {
        return "login";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    //@ResponseBody
    @RequestMapping(value = "/add", produces = {"application/json;charset=UTF-8"})
    public String addUser(User user) {
        userService.addUser(user);
        return "redirect:/user/login";
    }

    /**
     * 登录验证
     * @param account
     * @param password
     * @return
     */
    @RequestMapping(value = "/verify", produces = {"application/json;charset=UTF-8"})
    public String selectUser(@RequestParam("account") String account,
                             @RequestParam("password") String password) {
        user = userService.selectUser(account);
        System.out.println(user.getPassword().equals(password));
        if (user.getPassword().equals(password)) {
            if (user.getType() == 0) {
                mv.setViewName("student");
                return "redirect:student";
            } else if (user.getType() == 1) {
                return "redirect:teacher";
            }
        }
        return "redirect:login";

    }

    /**
     * 教师版首页显示教师发布的作业
     * @return
     */
    @RequestMapping(value = "/teacher", produces = {"application/json;charset=UTF-8"})
    public ModelAndView teacher() {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectByTeacherId(user.getAccount());
        mv.setViewName("teacher");
        mv.addObject("name",user.getName());
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 学生版首页显示所有教师发布的作业
     * @return
     */
    @RequestMapping(value = "/student", produces = {"application/json;charset=UTF-8"})
    public ModelAndView student() {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectAll();
        mv.setViewName("student");
        mv.addObject("name",user.getName());
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 教师查看学生提交的作业列表
     * @param hid
     * @return
     */
    @RequestMapping(value = "/selectsh", produces = {"application/json;charset=UTF-8"})
    public ModelAndView selectsh(@RequestParam("homeworkId") Long hid) {
        List<StudentHomework> list = new ArrayList<>();
        list = studentHomeworkService.selectByHomeworkId(hid);
        mv.setViewName("studentHomeworks");
        mv.addObject("name",user.getName());
        mv.addObject("homeworkId",hid);
        mv.addObject("list", list);
        return mv;
    }

    /**
     * 学生选择作业
     * @param studentHomework
     * @return
     */
    @RequestMapping(value = "/selectmh", produces = {"application/json;charset=UTF-8"})
    public ModelAndView selectmh(StudentHomework studentHomework) {
        studentHomework.setStudentId(user.getAccount());
        System.out.println(studentHomework.getStudentId());
        StudentHomework sh = studentHomeworkService.selectByDoublekey(studentHomework);

        Homework homework = homeworkService.selectByPK(studentHomework.getHomeworkId());
        List<StudentHomework> list = new ArrayList<>();
        if(sh != null) {
            list.add(sh);
        }
        mv.addObject("homework",homework);
        mv.setViewName("addStudentHomework");
        mv.addObject("studentId", user.getAccount());
        mv.addObject("name",user.getName());
        mv.addObject("homeworkId", studentHomework.getHomeworkId());

        mv.addObject("list", list);
        return mv;
    }
    /**
     * 根据教师姓名查询作业
     * @param teacherName
     * @return
     */
    @RequestMapping(value = "/searchHomework", produces = {"application/json;charset=UTF-8"})
    public ModelAndView searchHomework(@RequestParam("teacherName") String teacherName) {
        List<Homework> list = new ArrayList<>();
        list = homeworkService.selectByTeacherName(teacherName);
        mv.setViewName("student");
        mv.addObject("list", list);
        mv.addObject("name",user.getName());
        return mv;
    }
    /**
     * 跳转到学生修改作业页
     * @param id
     * @return
     */
    @RequestMapping(value = "/modify", produces = {"application/json;charset=UTF-8"})
    public ModelAndView modify_mh(@RequestParam("id") Long id) {

        StudentHomework studentHomework = studentHomeworkService.selectByP(id);

        mv.setViewName("modify");
        mv.addObject("studentHomework", studentHomework);
        mv.addObject("name",user.getName());


        return mv;
    }

    /**
     * 学生提交作业
     * @param studentHomework
     * @return
     */
    @RequestMapping(value = "/addStudentHomework", produces = {"application/json;charset=UTF-8"})
    public String addStudentHomework(StudentHomework studentHomework) {
        studentHomework.setCreateTime(date);
        studentHomeworkService.addStudentHomework(studentHomework);
        return "redirect:student";
    }

    /**
     * 学生修改已提交的作业
     * @param studentHomework
     * @return
     */
    @RequestMapping(value = "/updateStudentHomework", produces = {"application/json;charset=UTF-8"})
    public String updateStudentHomework(StudentHomework studentHomework) {
        studentHomework.setUpdateTime(date);
        studentHomeworkService.updateStudentHomework(studentHomework);
        return "redirect:student";
    }

    /**
     * 教师发布作业
     * @param homework
     * @return
     */
    @RequestMapping(value = "/addHomework", produces = {"application/json;charset=UTF-8"})
    public String addHomework(Homework homework) {
        homework.setTecherid(user.getAccount());
        homework.setTeacherName(user.getName());
        homework.setCreateTime(date);
        homeworkService.addHomework(homework);
        return "redirect:teacher";
    }


    /**
     * 教师根据studentId和homeworkId查询学生作业
     * @param studentHomework
     * @return
     */
    @RequestMapping(value = "/searchStudentHomework", produces = {"application/json;charset=UTF-8"})
    public ModelAndView searchsh(StudentHomework studentHomework) {
        StudentHomework list = studentHomeworkService.selectByDoublekey(studentHomework);
        mv.setViewName("studentHomeworks");

        mv.addObject("list", list);
        mv.addObject("name",user.getName());
        return mv;
    }

    /**
     * 跳转到教师批改业，显示学生作业详情
     * @param id
     * @return
     */
    @RequestMapping(value = "/review", produces = {"application/json;charset=UTF-8"})
    public ModelAndView review(@RequestParam("id") Long id) {
        StudentHomework list = studentHomeworkService.selectByP(id);
        mv.setViewName("review");
        mv.addObject("studentHomework", list);
        mv.addObject("name",user.getName());
        return mv;
    }
    /**
     * 教师提交评价
     * @param studentHomework
     * @return
     */
    @RequestMapping(value = "/addreview", produces = {"application/json;charset=UTF-8"})
    public String addReview(StudentHomework studentHomework) {
        studentHomeworkService.updateStudentHomework(studentHomework);
        return "redirect:selectsh?homeworkId="+studentHomework.getHomeworkId();
    }
}

