package com.lhz.lhzshiro;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {

    DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://47.99.199.51:3306/xingxing");
        dataSource.setUsername("xingxing");
        dataSource.setPassword("XINGX-xingxing");
    }
    /**
     * 使用JdbcRealm进行认证
     */
    @Test
    public void testAuthentication() {

        JdbcRealm jdbcRealm = new JdbcRealm();
        jdbcRealm.setDataSource(dataSource);
//        jdbcRealm.setPermissionsLookupEnabled(true);//设置权限查询开启,没有权限表,不要开启

        //自定义语句
        String sql = "select PASSWORD from user_sys where ACCOUNT = ?";
        jdbcRealm.setAuthenticationQuery(sql);

        String roleSql = "select ROLEID from user_sys where ACCOUNT = ?";
        jdbcRealm.setUserRolesQuery(roleSql);

        //构建secrityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        //主体提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("fangfang","1b1cf7a6ad19601e2e9ec20154053a7c");
        subject.login(token);
        subject.isAuthenticated();

        //提交验证
        subject.checkRole("11");
    }
}
