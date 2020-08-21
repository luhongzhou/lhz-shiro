package com.lhz.lhzshiro.dao;

import com.lhz.lhzshiro.entity.Permissions;
import com.lhz.lhzshiro.entity.Role;
import com.lhz.lhzshiro.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 模拟数据查询
 */
@Service
public class UserDaoImpl {

    public User getUserByUserName(String userName) {
        return getMapByName(userName);
    }

    private User getMapByName(String userName){
        //共添加两个用户，两个用户都是admin一个角色，
        //wsl有query和add权限，zhangsan只有一个query权限
        Permissions permissions1 = new Permissions("1","query");
        Permissions permissions2 = new Permissions("2","add");
        Set<Permissions> permissionsSet = new HashSet<>();
        permissionsSet.add(permissions1);
        permissionsSet.add(permissions2);
        Role role = new Role("1","admin",permissionsSet);
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);
        User user = new User("1","xiaohong","123456",roleSet);
        Map<String ,User> map = new HashMap<>();
        map.put(user.getName(), user);

        Permissions permissions3 = new Permissions("3","query");
        Set<Permissions> permissionsSet1 = new HashSet<>();
        permissionsSet1.add(permissions3);
        Role role1 = new Role("2","user",permissionsSet1);
        Set<Role> roleSet1 = new HashSet<>();
        roleSet1.add(role1);
        User user1 = new User("2","xiaoming","123456",roleSet1);
        map.put(user1.getName(), user1);
        return map.get(userName);
    }
}
