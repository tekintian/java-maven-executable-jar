package cn.tekin;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(ConfigClass.class);

        UserService userService = (UserService)ioc.getBean("user");
        userService.test();

    }
}