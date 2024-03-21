package cn.tekin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"cn.tekin.service"}) // 组件扫描,默认扫描路径为当前包以及子包中的所有组件
public class ConfigClass {

}
