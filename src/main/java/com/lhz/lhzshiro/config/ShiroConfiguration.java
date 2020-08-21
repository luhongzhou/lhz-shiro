package com.lhz.lhzshiro.config;

import com.lhz.lhzshiro.filter.RolesOrFilter;
import com.lhz.lhzshiro.realm.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /*重要，设置自定义拦截器，当访问某些自定义url时，使用这个filter进行验证*/
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        //如果map里面key值为authc,表示所有名为authc的过滤条件使用这个自定义的filter
        //map里面key值为myFilter,表示所有名为myFilter的过滤条件使用这个自定义的filter，具体见下方
        filters.put("rolesOrFilter", new RolesOrFilter());
        shiroFilterFactoryBean.setFilters(filters);
        /*---------------------------------------------------*/

        //拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/logout", "logout");
        //　　anon:所有url都都可以匿名访问;
        //　　authc: 需要认证才能进行访问;
        //　　user:配置记住我或认证通过可以访问；
        //放开静态资源的过滤
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        //放开登录url的过滤
        filterChainDefinitionMap.put("/index", "authc");
        ///
        //对于指定的url，使用自定义filter进行验证
//        filterChainDefinitionMap.put("/targetUrl", "myFilter");
        //可以配置多个filter，用逗号分隔，按顺序过滤，下方表示先通过自定义filter的验证，再通过shiro默认过滤器的验证
        //filterChainDefinitionMap.put("/targetUrl", "myFilter,authc");
        ///
        //过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
        //url从上向下匹配，当条件匹配成功时，就会进入指定filter并return(不会判断后续的条件)，因此这句需要在最下边
        filterChainDefinitionMap.put("/**", "authc");

        //如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/loginSuccess");
        // 未授权界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        return securityManager;
    }

    //配置核心安全事务管理器
    @Bean(name="securityManager")
    public SecurityManager securityManager(@Qualifier("myShiroRealm") CustomRealm shiroRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroRealm);
        return manager;
    }

    //配置自定义的权限登录器
    @Bean
    public CustomRealm myShiroRealm() {
        CustomRealm shiroRealm=new CustomRealm();
        return shiroRealm;
    }

    /**
     * 添加下面2个bean，@RequiresRoles 和 @RequiresPermissions才能生效
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}