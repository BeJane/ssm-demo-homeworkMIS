
@[toc]
# 需求分析
## 用例分析
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618184125863.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
# 数据库设计

一共设计了3张表

s_user: 存储用户信息

s_homework: 存储教师发布的作业

s_student_homework: 存储学生提交的作业

> 为了查询方便，没有设置外键约束

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618184454352.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
# 开发技术
开发平台：windows 10
开发工具：Intellij IDEA 2019.3.3
JDK：Java 8
Maven：maven-3.6.3
服务器：tomcat 7.0
数据库：MySQL 8.0
数据源：Druid1.1.6
Spring Boot：1.5.6.RELEASE

ORM框架：MyBatis+通用Mapper
# 实现过程
- 项目目录
<img src="https://img-blog.csdnimg.cn/2020061818520822.png" width= 58%>
### 搭建IDEA+springboot项目

- File --> New --> Project
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200619183338296.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

- Spring Initializer --> 选择SDK-->Next
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200619183909867.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

- 创建项目的文件结构以及jdk的版本 --> Next
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020061918400728.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

- 选择项目所需要的依赖

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwODE2MTQwNTE1MDA4?x-oss-process=image/format,png)

![这里写图片描述](https://imgconvert.csdnimg.cn/aHR0cDovL2ltZy5ibG9nLmNzZG4ubmV0LzIwMTcwODE2MTQwNTU4NzM1?x-oss-process=image/format,png)



### 使用mysql创建数据库

sql语句如下：
```sql
CREATE DATABASE `school` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

CREATE TABLE `s_user` (
  `account` char(8) NOT NULL,
  `password` char(18) NOT NULL,
  `name` varchar(20) NOT NULL,
  `type` int NOT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `s_homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `content` text,
  `score` int NOT NULL,
  `techerid` char(8) NOT NULL,
  `create_time` timestamp NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  `teacher_name` char(8) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `s_student_homework` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` char(8) DEFAULT NULL,
  `homework_id` bigint NOT NULL,
  `homework_title` varchar(45) NOT NULL,
  `homework_content` text,
  `score` int DEFAULT NULL,
  `comment` char(150) DEFAULT NULL,
  `create_time` timestamp NOT NULL,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

```
### 使用mybatis generator 自动生成代码：

- 创建application.yml配置文件，配置datasource和mybatis
```yml

server:
  port: 8080

spring:
  datasource:
    name: test
    url: jdbc:mysql://127.0.0.1:3306/school
    #serverTimezone: UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    username: root
    password: 123456
    # 使用druid数据源
    #type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    filters: stat
    maxActive: 20
    initialSize: 1
    maxWait: 60000
    minIdle: 1
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: select 'x'
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true
    maxOpenPreparedStatements: 20

## 该配置节点为独立的节点，有很多同学容易将这个配置放在spring的节点下，导致配置无法被识别
mybatis:
  mapper-locations: classpath:mapping/*.xml  #注意：一定要对应mapper映射xml文件的所在路径
  type-aliases-package: com.winter.model  # 注意：对应实体类的路径

```
- 在pom.xml中添加generator 插件
```xml
<!-- mybatis generator 自动生成代码插件 -->
			<plugin>
				<groupId>org.mybatis.generator</groupId>
				<artifactId>mybatis-generator-maven-plugin</artifactId>
				<version>1.3.2</version>
				<configuration>
					<configurationFile>${basedir}/src/main/resources/generator/generatorConfig.xml</configurationFile>
					<overwrite>true</overwrite>
					<verbose>true</verbose>
				</configuration>
			</plugin>
```
- 创建pom.xml中generator 插件所对应的配置文件 generatorConfig.xml

  设置数据库驱动

  设置数据库连接

  设置生成模型的包名和位置

  设置生成映射文件的包名和位置

  设置DAO的包名和位置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <!-- 数据库驱动:选择你的本地硬盘上面的数据库驱动包-->
    <classPathEntry  location="E:\developer\mybatis-generator-core-1.3.2\lib\mysql-connector-java-5.1.25-bin.jar"/>
    <context id="DB2Tables"  targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <!--数据库链接URL，用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver" connectionURL="jdbc:mysql://127.0.0.1/mytest" userId="root" password="root">
        </jdbcConnection>
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="com.winter.model" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="mapping" targetProject="src/main/resources">
            <property name="enableSubPackages" value="true"/>
        </sqlMapGenerator>
        <!-- 生成DAO的包名和位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.winter.mapper" targetProject="src/main/java">
            <property name="enableSubPackages" value="true"/>
        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
   <table tableName="s_user" domainObjectName="User" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false"></table>

<table tableName="s_student_homework" domainObjectName="StudentHomework" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false"></table>
<table tableName="s_homework" domainObjectName="Homework" enableCountByExample="false" enableUpdateByExample="false" enableDeleteByExample="false" enableSelectByExample="false" selectByExampleQueryId="false"></table>
    </context>
</generatorConfiguration>
```
- 点击run-Edit Configurations
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618212924874.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 添加配置
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618213535525.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 运行generator
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618213614601.png)
对应generatorConfig.xml中的配置，每张表对应生成模型、映射文件、DAO
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200619184128898.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

- 在springboot启动类中吧mapper的路径加进来
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200619183628254.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
> **@MapperScan** : mapper接口扫描注解


>重复运行时模型和DAO文件会重写，映射文件文件不会，将导致项目运行时会出错
>解决办法：重新运行之前先删除mapper.xml文件

我们还需要一些特殊的sql语句，可以自己添加

比如实现：select * from s_homework

HomeworkMapper.java 中添加如下代码

```java
List<Homework> selectAll();
```



HomeworkMapper.xml

```xml

  <select id="selectAll" resultMap="ResultMapWithBLOBs" parameterType="java.lang.Long" >
    select
    <include refid="Base_Column_List" />
    ,
    <include refid="Blob_Column_List" />
    from s_homework
      order by create_time DESC
  </select>

```



### 编写Service

遵循单一责任原则和接口分离原则

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618192734241.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

- UserService

  ```java
  package com.winter.service;
  import com.winter.model.User;
  /**
   * Created by wjq
   */
  public interface UserService {
      int addUser(User user);
      User selectUser(String account);
  
  }
  ```

- UserHomeworkServiceImpl

  ```java
  package com.winter.service;
  
  import com.winter.mapper.UserMapper;
  import com.winter.model.User;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  
  @Service(value = "userService")
  public class UserServiceImpl implements UserService {
  
      @Autowired
      private UserMapper userMapper;//这里会报错，但是并不会影响
  
      @Override
      public User selectUser(String account) {
          return userMapper.selectByPrimaryKey(account);
      }
  
      @Override
      public int addUser(User user) {
  
          return userMapper.insertSelective(user);
      }
  
  }
  ```

- HomeworkService

  ```java
  package com.winter.service;
  
  import com.winter.model.Homework;
  
  import java.util.List;
  
  /**
   * @author:Jingqi Wu
   * @date: 2020/4/23
   */
  public interface HomeworkService {
  
  
      /**
       * 老师发布作业
       * @param homework
       * @return
       */
      int addHomework(Homework homework);
      /**
       *  This is the method to be used to list down all homework
       * @return
       */
      List<Homework> selectAll();
      List<Homework> selectByTeacherId(String teacherId);
      List<Homework> selectByTeacherName(String teacherName);
      Homework selectByPK(Long id);
  }
  ```

- HomeworkServiceImpl

  ```java
  package com.winter.service;
  
  import com.winter.mapper.HomeworkMapper;
  import com.winter.model.Homework;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  
  import java.util.List;
  
  /**
   * @author:Jingqi Wu
   * @date: 2020/6/16
   */
  @Service(value = "homeworkService")
  public class HomeworkServiceImpl implements HomeworkService {
      @Autowired
      HomeworkMapper homeworkMapper;
  
      /**
       * 老师发布作业
       *
       * @param homework
       * @return
       */
      @Override
      public int addHomework(Homework homework) {
          return homeworkMapper.insert(homework);
      }
  
      /**
       * This is the method to be used to list down all homework
       *
       * @return
       */
      @Override
      public List<Homework> selectAll() {
          return homeworkMapper.selectAll();
      }
  
      @Override
      public List<Homework> selectByTeacherId(String teacherId) {
  
          return homeworkMapper.selectByTeacherId(teacherId);
      }
  
      @Override
      public List<Homework> selectByTeacherName(String teacherName) {
          return homeworkMapper.selectByTeacherName(teacherName);
      }
  
      @Override
      public Homework selectByPK(Long id) {
          return homeworkMapper.selectByPrimaryKey(id);
      }
  }
  ```

- StudentHomeworkService

  ```java
  package com.winter.service;
  import com.winter.model.StudentHomework;
  
  import java.util.List;
  
  
  /**
   * @author:Jingqi Wu
   * @date: 2020/6/16
   */
  public interface StudentHomeworkService {
  
  
      /**
       * 学生提交作业
       * @param studentHomework
       * @return
       */
      int addStudentHomework(StudentHomework studentHomework);
  
      /**
       * @return
       */
      int updateStudentHomework(StudentHomework studentHomework);
      /**
       *  This is the method to be used to list down all
       *  students' homework
       * @param homeworkId
       * @return
       */
      List<StudentHomework> selectByHomeworkId(Long homeworkId);
  
      /**
       * This is the method to be used to search the student's homework
       *
       * @param id
       * @return
       */
      StudentHomework selectByP(Long id);
  
      /**
       * 目前是通过studentid,homeworkid查询
       * @param studentHomework
       * @return
       */
      StudentHomework selectByDoublekey(StudentHomework studentHomework);
  }
  
  ```

- StudentHomeworkServiceImpl

  ```java
  package com.winter.service;
  
  import com.winter.mapper.StudentHomeworkMapper;
  import com.winter.model.StudentHomework;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;
  
  import java.util.List;
  
  /**
   * @author:Jingqi Wu
   * @date: 2020/6/16
   */
  @Service(value = "studentHomeworkService")
  public class StudentHomeworkServiceImpl implements StudentHomeworkService {
  
      @Autowired
      private StudentHomeworkMapper studentHomeworkMapper;
  
      /**
       * 学生提交作业
       *
       * @param studentHomework
       * @return
       */
      @Override
      public int addStudentHomework(StudentHomework studentHomework) {
          return studentHomeworkMapper.insert(studentHomework);
      }
  
      /**
       * This is the method to be used to list down all
       * students' homework
       *
       * @param homeworkId
       * @return
       */
      @Override
      public List<StudentHomework> selectByHomeworkId(Long homeworkId) {
          return studentHomeworkMapper.selectByHomeworkId(homeworkId) ;
      }
  
      /**
       * 目前是通过studentid,homeworkid查询
       *
       * @param studentHomework
       * @return
       */
      @Override
      public StudentHomework  selectByDoublekey(StudentHomework studentHomework) {
          return studentHomeworkMapper.selectByDoublekey(studentHomework);
      }
  
      /**
       * This is the method to be used to search the student's homework
       *
       * @return
       */
      @Override
      public  StudentHomework selectByP(Long id) {
          return studentHomeworkMapper.selectByPrimaryKey(id);
      }
  
      /**
       * @param studentHomework
       * @return
       */
      @Override
      public int updateStudentHomework(StudentHomework studentHomework) {
          return studentHomeworkMapper.updateByPrimaryKeySelective(studentHomework);
      }
  }
  ```

  

### 编写Controller

![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618192503440.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)

UserController

```java
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
```



# 交互设计及运行截图
- 教师/学生注册：http://localhost:8080/user/register
老师进行注册
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618215610565.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
同学进行注册
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200617225857715.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 教师/学生登录: http://localhost:8080/user/login
注册成功跳转到登录页面，李四老师使用刚注册的账号进行登录
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618220344713.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
学生进行登录
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200617230007381.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 教师发布作业：http://localhost:8080/user/teacher
登录成功后进入教师首页，可以看到教师自己以往发布的作业，也可以发布新的作业，发布新 作业可以设置作业题目、作业内容、作业分值
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618221831567.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
发布成功后可以在已发布作业列表的第一行找到新发布的作业
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618221917156.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
点击列表最后一列的查看，可以看到学生提交作业情况
- 老师查看/查找学生作业：http://localhost:8080/user/selectsh?homeworkId=1
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618222159201.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
在输入框输入学号后点击查询，可以查找特定学生的学号
比如：输入12345
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618222352459.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618222640883.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
点击查看可以进行作业批改
- 教师批改作业：http://localhost:8080/user/review?id=1
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618222557680.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 点击每个页面左上角的“注销”退出登录
- 学生查看/查找教师发布的作业：http://localhost:8080/user/student
学生登录成功进入学生版首页，可以看到所有教师发布的作业，越晚发布排在前面
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020061822314344.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
在输入框输入教师姓名，然后点击查询可以查找该老师发布的作业
比如输入李德之
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618223455646.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
- 学生提交作业
点击查看，如果学生未提交该作业，则可进行提交
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618224531176.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
如果学生已提交过该作业，则可以查看相应作业的提交情况以及老师的评分和评语
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618223842102.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)
点击修改可以修改作业内容，修改时间会被记录
- 学生修改作业：http://localhost:8080/user/modify?id=1
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200618225001953.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzM5NzUzNzc4,size_16,color_FFFFFF,t_70)




# 知识点总结
## Spring的3个阶段
第一阶段：XML配置，在Spring1.x时代，使用Spring开发满眼都是xml配置的Bean，随着项目的扩大，我们需要把xml配置文件分放到不同的配置文件里，那时候需要频繁的在开发的类和配置文件之间切换。

第二阶段：注解配置，在Spring2.x时代，Spring提供声明Bean的注解，大大减少了配置量。应用的基本配置用xml，业务配置用注解。

第三阶段：Java配置，从Spring3.x到现在，Spring提供了Java配置，使用Java配置可以让你更理解你所配置的Bean。

Spring Boot：使用“习惯优于配置”的理念让你的项目快速运行起来。使用Spring Boot很容易创建一个独立运行、准生产级别的基于Spring框架的项目，使用Spring Boot你可以不用或者只需要很少的Spring配置。

下面就来使用Spring Boot一步步搭建一个前后端分离的应用开发框架，并且以后不断的去完善这个框架，往里面添加功能。后面以实战为主，不会介绍太多概念，取而代之的是详细的操作。

## springboot工作原理

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190919211100304.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl80MTkyNDg3OQ==,size_16,color_FFFFFF,t_70)

一个典型的springboot应用

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);
}
```



#### @SpringBootApplication

@SpringBootApplication是一个复合annotation。

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {
        @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
        @Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
    ...
}
```



其中最重要的三个annotation是@SpringBootConfiguration，@EnableAutoConfiguration和@ComponentScan。
所以以下 的springboot启动类也可以启动。

```java
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

#### @SpringBootConfiguration和@Configuration

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Configuration
public @interface SpringBootConfiguration 
```


@SpringBootConfiguration本质上是一个@Configuration。
启动类标注了@SpringBootConfiguration之后，本身其实也是一个ioc容器的配置类。

#### @EnableAutoConfiguration

spring框架提供了各种名字以@Enable开头的annotation定义，比如@EnableScheduling、@EnableCaching、@EnableMBeanExport等，@EnableAutoConfiguration就是借助@Import的支持，收集和注册特定场景相关的bean定义：
@EnableScheduling是通过@Import将spring调度框架相关的bean定义都加载到ioc容器。
@EnableMBeanExport是通过@Import将JMX相关的bean定义都加载到ioc容器

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration 
```


@EnableAutoConfiguration作为一个复合annotation，其中最关键的要属@Import(AutoConfigurationImportSelector.class)，借助AutoConfigurationImportSelector，@EnableAutoConfiguration可以帮助springboot应用将所有符合条件的@Configuration配置都加载到当前springboot创建并使用的ioc容器，就跟一只八爪鱼一样。

AutoConfigurationImportSelector借助spring框架原有的一个工具类SpringFactoriesLoader的支持，@EnableAutoConfiguration可以智能地完成自动配置。

SpringFactoriesLoader主要功能是从指定的配置文件META-INF/spring.factories加载配置，spring.factories是一个典型的Java properties文件，配置的格式为key=value形式，只不过key和value都是Java类型的完整类型。

SpringFactoriesLoader在@EnableAutoConfiguration的场景中，更多的是提供了一种配置查找的功能支持，即根据@EnableAutoConfiguration的完整类名org.springframework.boot.autoconfigure.EnableAutoConfiguration作为查找的key，获取对应的一组@Configuration类：

```xml
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\

```


所以，@EnableAutoConfiguration的自动配置就是从classpath中搜寻所有META-INF/spring.factories配置文件，并将其中org.springframework.boot.autoconfigure.EnableAutoConfiguration对应的配置项通过反射实例化为对应的标注了@Configuration的JavaConfig形式的ioc容器配置类，然后汇总为一个并加载到ioc容器。

#### @ComponentScan

@ComponentScan的功能其实就是自动扫描并加载符合条件的组件或bean定义，最终将这些bean定义加载到容器中。

## Mybatis工作原理

![img](https://img-blog.csdn.net/20180624002302854?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTQ3NDUwNjk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)


mybatis应用程序通过SqlSessionFactoryBuilder从mybatis-config.xml配置文件（也可以用Java文件配置的方式，需要添加@Configuration）来构建SqlSessionFactory（SqlSessionFactory是线程安全的）；

然后，SqlSessionFactory的实例直接开启一个SqlSession，再通过SqlSession实例获得Mapper对象并运行Mapper映射的SQL语句，完成对数据库的CRUD和事务提交，之后关闭SqlSession。

说明：SqlSession是单线程对象，因为它是非线程安全的，是持久化操作的独享对象，类似jdbc中的Connection，底层就封装了jdbc连接。

**详细流程如下：**

1、加载mybatis全局配置文件（数据源、mapper映射文件等），解析配置文件，MyBatis基于XML配置文件生成Configuration，和一个个MappedStatement（包括了参数映射配置、动态SQL语句、结果映射配置），其对应着<select | update | delete | insert>标签项。

2、SqlSessionFactoryBuilder通过Configuration对象生成SqlSessionFactory，用来开启SqlSession。

3、SqlSession对象完成和数据库的交互：
	a、用户程序调用mybatis接口层api（即Mapper接口中的方法）
	b、SqlSession通过调用api的Statement ID找到对应的MappedStatement对象
	c、通过Executor（负责动态SQL的生成和查询缓存的维护）将MappedStatement对象进行解析，sql参数转化、动态sql拼接，生成jdbc Statement对象
	d、JDBC执行sql。

​	e、借助MappedStatement中的结果映射关系，将返回结果转化成HashMap、JavaBean等存储结构并返回。





## Thymeleaf

Thymeleaf是一种用于Web和独立环境的现代服务器端的Java模板引擎

#### 常见用法

- herf

  ```html
  <a th:href="@{'review?id='+${entries['id']}}" >查看</a>
  ```

- value

  ```html
  <input type="hidden" name="homeworkId"  value="" th:value="*{homeworkId}">
  ```

- 列表遍历

  ```html
   <tr th:each="entries:${list}" th:style="'background-color:'+'#F2F2F2'" >
          <td th:text="${entries['id']}"></td>
          <td th:text="${entries['studentId']}"></td>
  
   </tr>
  ```

  

- 格式化日期时间

```html
 <td th:text="${#dates.format(studentHomework['updateTime'], 'yyyy-MM-dd HH:mm')}"></td>
```
- 判断列表是否为空
```html
<div th:if="${#lists.isEmpty(list)}">
</div>
```
```html
<div th:if="${not #lists.isEmpty(list)}">
</div>
```
# 参考资料

spring boot整合mybatis: https://blog.csdn.net/Winter_chen001/article/details/77249029

springboot工作原理：https://blog.csdn.net/Chen_Victor/article/details/81262233

Mybatis工作原理：https://blog.csdn.net/u014745069/article/details/80788127

