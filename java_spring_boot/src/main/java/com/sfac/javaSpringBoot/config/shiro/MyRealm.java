package com.sfac.javaSpringBoot.config.shiro;

import com.sfac.javaSpringBoot.modules.account.entity.Resource;
import com.sfac.javaSpringBoot.modules.account.entity.Role;
import com.sfac.javaSpringBoot.modules.account.entity.User;
import com.sfac.javaSpringBoot.modules.account.service.ResourceService;
import com.sfac.javaSpringBoot.modules.account.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private ResourceService resourceService;

    //资源授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //拿到用户
        User user =  (User)principals.getPrimaryPrincipal();
        //将角色信息封装到资源授权器
        List<Role> roles = user.getRoles();
        if(roles != null && !roles.isEmpty()){
            roles.stream().forEach(item ->{
                simpleAuthorizationInfo.addRole(item.getRoleName());
                //通过roleId得到角色permission
                List<Resource> resources =
                        resourceService.getResourcesByRoleId(item.getRoleId());
                if(resources != null && !resources.isEmpty()){
                    resources.stream().forEach(resource -> {
                        //家权限
                        simpleAuthorizationInfo.addStringPermission(resource.getPermission());
                    });
                }
            });
        }
        return simpleAuthorizationInfo;
    }

    //身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String userName = (String)token.getPrincipal();
        User user = userService.getUserByUserName(userName);
        if(user == null){
            throw new UnknownAccountException("This user is not exist");
        }
        return new SimpleAuthenticationInfo(user,user.getPassword(),getName());
    }
}
