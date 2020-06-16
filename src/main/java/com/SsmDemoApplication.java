package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

@MapperScan("com.winter.mapper")//将项目中对应的mapper类的路径加进来就可以了
public class SsmDemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(SsmDemoApplication.class, args);
    }
/**/
}
