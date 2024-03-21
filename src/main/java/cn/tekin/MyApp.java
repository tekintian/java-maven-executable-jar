package cn.tekin;

import cn.tekin.config.ConfigClass;
import cn.tekin.service.OrderService;
import cn.tekin.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApp {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(ConfigClass.class);

        UserService userService = (UserService)ioc.getBean("userService");
        userService.test();

        OrderService orderService = (OrderService) ioc.getBean("orderService");
        String msg = orderService.payOrder();
        System.out.println(msg);

    }
}