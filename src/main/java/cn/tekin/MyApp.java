package cn.tekin;

import cn.tekin.config.ConfigClass;
import cn.tekin.service.OrderService;
import cn.tekin.service.UserService;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApp {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext ioc = new AnnotationConfigApplicationContext(ConfigClass.class);

        UserService userService = (UserService)ioc.getBean("userService");
        userService.test();

        OrderService orderService = (OrderService) ioc.getBean("orderService");
        String msg = orderService.payOrder();
        System.out.println(msg);

        //ApplicationContext BeanFactory 接口
        // 都提供getBean方法区别？
        // BeanFactory 职责单一, 只负责创建@Bean
        // ApplicationContext 更加智能

//        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
//        defaultListableBeanFactory.registerBeanDefinition("userservice", new RootBeanDefinition(UserService.class));
//        UserService userService = (UserService) defaultListableBeanFactory.getBean("userService");
//

    }
}