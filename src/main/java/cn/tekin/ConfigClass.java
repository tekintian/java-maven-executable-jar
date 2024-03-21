package cn.tekin;

import org.springframework.context.annotation.Bean;

public class ConfigClass {

    /**
     * 将UserService放入到Spring容器中, 默认名称是方法名 userService1
     * @return
     */
    @Bean(value = "user")
    public UserService userService(){
        return new UserService();
    }


}
