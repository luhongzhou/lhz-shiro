package com.lhz.lhzshiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public  class LhzShiroApplicationTests {
	SimpleAccountRealm realm = new SimpleAccountRealm();

	@Before
	public void addUser(){
	realm.addAccount("xiaoming","love_xiaohong","admin");
	}

	@Test
	public void testAuthentication() {
		//构建secrityManager环境
		DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
		defaultSecurityManager.setRealm(realm);

		//主体提交认证请求
		SecurityUtils.setSecurityManager(defaultSecurityManager);
		Subject subject = SecurityUtils.getSubject();

		UsernamePasswordToken token = new UsernamePasswordToken("xiaoming","love_xiaohong");
		subject.login(token);

		//提交验证
		System.out.println("认证结果:"+subject.isAuthenticated());
		//验证角色
		System.out.println("是否包含admin角色:");
		subject.checkRole("admin");
	}

}
