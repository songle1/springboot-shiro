package com.songle.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.github.pagehelper.util.StringUtil;
import com.songle.model.Resources;
import com.songle.service.ResourcesService;
import com.songle.shiro.MyShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author songle
 * @create 2019-05-09 上午 11:29
 */
@Configuration
public class shiroConfig {

    @Autowired
    private ResourcesService resourcesService;

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

 /*   @Bean
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }*/

    @Bean
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
     Filter Chain定义说明
     1、一个URL可以配置多个Filter，使用逗号分隔
     2、当设置多个过滤器时，全部验证通过，才视为通过
     3、部分过滤器可指定参数，如perms，roles
     *
     */
     @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
         ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
         //必须设置securityManager
         shiroFilterFactoryBean.setSecurityManager(securityManager);
         //设置登录认证的地址
         shiroFilterFactoryBean.setLoginUrl("/login");
         //登录成功后要跳转的页面
         shiroFilterFactoryBean.setSuccessUrl("/usersPage");
         //未授权界面
         shiroFilterFactoryBean.setUnauthorizedUrl("/403");
         //设置拦截器
         Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
         filterChainDefinitionMap.put("/logout","logout");
         filterChainDefinitionMap.put("/js/**","anon");
         filterChainDefinitionMap.put("/css/**","anon");
         filterChainDefinitionMap.put("/img/**","anon");
         filterChainDefinitionMap.put("/font-awesome","anon");
         //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
         //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
         //自定义加载权限资源关系'
         List<Resources> resourcesList = resourcesService.queryAll();
         for(Resources resources:resourcesList){
             if(StringUtil.isNotEmpty(resources.getResurl())){
                 String permission = "perms["+resources.getResurl()+"]";
                 filterChainDefinitionMap.put(resources.getResurl(),permission);
             }
         }
         filterChainDefinitionMap.put("/**","authc");
         shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
         return shiroFilterFactoryBean;
     }



     @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm
         securityManager.setRealm(myShiroRealm());
         // 自定义缓存实现 使用redis
         //securityManager.setCacheManager(cacheManager());
         // 自定义session管理 使用redis
         securityManager.setSessionManager(sessionManager());
         return securityManager;
     }

     @Bean
    public MyShiroRealm myShiroRealm(){
         MyShiroRealm myShiroRealm = new MyShiroRealm();
         myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
         return myShiroRealm;
     }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     *  所以我们需要修改下doGetAuthenticationInfo中的代码;
     * ）
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法，使用MD5加密；
        hashedCredentialsMatcher.setHashIterations(2);//散列次数
        return hashedCredentialsMatcher;
    }

    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     * @return
     */

    @Bean
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setExpire(1800);
        redisManager.setTimeout(timeout);
        return redisManager;
    }


    /**
     * 配置redis缓存
     * 使用的是shiro-redis开源插件
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }



}
