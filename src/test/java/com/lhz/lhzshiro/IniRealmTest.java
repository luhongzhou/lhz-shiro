package com.lhz.lhzshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class IniRealmTest {

    /**
     * 使用iniRealm进行认证
     */
    @Test
    public void testAuthentication() {

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        //构建secrityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming","love_xiaohong");
        subject.login(token);

        //提交验证
        System.out.println("认证结果:"+subject.isAuthenticated());
        System.out.println("验证角色");
        subject.checkRole("admin");
        System.out.println("验证权限");
        subject.checkPermission("user:add");
    }
}
