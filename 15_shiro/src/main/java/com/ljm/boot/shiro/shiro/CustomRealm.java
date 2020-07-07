package com.ljm.boot.shiro.shiro;

import com.ljm.boot.shiro.model.Permission;
import com.ljm.boot.shiro.model.Role;
import com.ljm.boot.shiro.model.User;
import com.ljm.boot.shiro.repository.UserRepository;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

   private final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Set<String> permissionList = new HashSet<>();
        Set<String> roleNameList = new HashSet<>();
        for(Role role: user.getRoles()){
            for(Permission permission : role.getPermissions()){
                //添加权限
                permissionList.add(permission.getPermissionName());
            }
            //添加角色
            roleNameList.add(role.getRoleName());
        }
        info.setStringPermissions(permissionList);
        info.setRoles(roleNameList);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upt = (UsernamePasswordToken) authenticationToken;
        User user=userRepository.findByUserName(upt.getUsername());
        if(user==null){
            //帐号不存在
            logger.info("account does not exist!");
            throw  new UnknownAccountException();
        }
        logger.info("account exist ,Certification...!");
        //把数据库的密码和(当前登录输入的密码+盐 后加密的密码比对)
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(), ByteSource.Util.bytes(user.getSalt()),getName());
        return authenticationInfo;
    }

}


