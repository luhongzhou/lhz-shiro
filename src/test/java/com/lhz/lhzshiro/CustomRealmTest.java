package com.lhz.lhzshiro;

import com.lhz.lhzshiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

    /**
     * 使用自定义realm进行认证
     */
    @Test
    public void testAuthentication() {

        CustomRealm customRealm = new CustomRealm();

        //构建secrityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        //加密
        HashedCredentialsMatcher matcher  = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");//加密方法
        matcher.setHashIterations(1);//加密次数
        customRealm.setCredentialsMatcher(matcher);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming","123456");
        subject.login(token);
        System.out.println( subject.isAuthenticated());

        subject.checkRole("admin");
        subject.checkPermissions("user:add","user:delete");

        //提交验证
//        subject.checkRole("11");
    }
}
