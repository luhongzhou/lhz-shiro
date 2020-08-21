package com.lhz.lhzshiro.controller;

import com.lhz.lhzshiro.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

@RestController("/index")
public class IndexController {

    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public String login(@RequestBody User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getName(), user.getPassword());
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "账号或密码错误！";
        }
        return "登录成功";
    }

    /**
     * @RequiresRoles : 角色验证，该方法需要“admin”权限。
     * @return
     */
    @RequiresRoles("admin")
    @PostMapping(value = "/testRole")
    public String testRole(){
        return "success";
    }

    /**
     * @RequiresRoles : 权限验证，该方法需要“add”权限。
     * @return
     */
    @RequiresPermissions({"add","query"})
    @GetMapping(value = "/testPermissions")
    @ResponseBody
    public String testPermissions(){
        return "success";
    }

}
