package cn.tekin.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"cn.tekin.service"}) // 组件扫描基础包路径配置,默认扫描路径为当前包以及子包中的所有组件
public class ConfigClass {
    // 这里也可以手动通过 创建一个方法返回自己想要注入的对象,然后添加@Bean注解 注入到spring容器中.
}
