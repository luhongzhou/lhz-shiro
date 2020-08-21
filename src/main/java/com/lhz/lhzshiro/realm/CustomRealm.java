package com.lhz.lhzshiro.realm;

import com.lhz.lhzshiro.dao.UserDaoImpl;
import com.lhz.lhzshiro.entity.Permissions;
import com.lhz.lhzshiro.entity.Role;
import com.lhz.lhzshiro.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomRealm  extends AuthorizingRealm {
    @Autowired
    UserDaoImpl userDao;


    /**
     * 授权 : 根据用户名从数据中查询角色和权限放入AuthorizationInfo
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //从数据库获取用户
        User user = userDao.getUserByUserName(userName);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
            //添加权限
            for (Permissions permissions : role.getPermissions()) {
                simpleAuthorizationInfo.addStringPermission(permissions.getPermissionsName());
            }
        }
        return simpleAuthorizationInfo;
    }


    /**
     * 验证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        //1.从主体传过来的认证信息中获取用户名
        String userName = (String) authenticationToken.getPrincipal();
        //2.通过用户名到数据库查询凭证
        User user = userDao.getUserByUserName(userName);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
        //设置盐值
//        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("lhz"));
        return authenticationInfo;
    }

    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("123456", "lhz");
        System.out.println(md5Hash.toString());

    }

}
