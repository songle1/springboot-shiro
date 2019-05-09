package com.songle.config;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author songle
 * @create 2019-05-09 上午 10:24
 */
@Configuration
public class DruidConfig {

     @Bean
    public ServletRegistrationBean druidServlet(){
         ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
         //设置登录查看信息的账号和密码；
         servletRegistrationBean.addInitParameter("loginUsername","admin");
         servletRegistrationBean.addInitParameter("loginPassword","123456");
         return servletRegistrationBean;

     }

     @Bean
    public FilterRegistrationBean filterRegistrationBean(){
         FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
         filterRegistrationBean.setFilter(new WebStatFilter());
         filterRegistrationBean.addUrlPatterns("/*");
         filterRegistrationBean.addInitParameter("exclusions","*.js,*.jpg,*.gif,*.png,*.css,*.ico,/druid/*");
         return filterRegistrationBean;
     }
}
