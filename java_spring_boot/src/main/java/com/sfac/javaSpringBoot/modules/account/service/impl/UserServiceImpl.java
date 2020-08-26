package com.sfac.javaSpringBoot.modules.account.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sfac.javaSpringBoot.config.ResourceConfigBean;
import com.sfac.javaSpringBoot.modules.account.dao.UserDao;
import com.sfac.javaSpringBoot.modules.account.dao.UserRoleDao;
import com.sfac.javaSpringBoot.modules.account.entity.Role;
import com.sfac.javaSpringBoot.modules.account.entity.User;
import com.sfac.javaSpringBoot.modules.account.service.UserService;
import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import com.sfac.javaSpringBoot.utils.MD5Util;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private ResourceConfigBean resourceConfigBean;

    @Override
    @Transactional
    public Result<User> insertUser(User user) {
        User userTemp = userDao.getUserByUserName(user.getUserName());
        if(userTemp != null){
            return new Result<User>(Result.ResultStatus.FAILD.status,"userName is repeat !");
        }
        user.setCreateDate(LocalDateTime.now());
        //密码设置
        user.setPassword(MD5Util.getMD5(user.getPassword()));
        userDao.insertUser(user);

        userRoleDao.deleteUserRoleByUserId(user.getUserId());
        List<Role> roles = user.getRoles();
        if(roles != null && !roles.isEmpty()){
            roles.stream().forEach(item -> {
                userRoleDao.insertUserRole(user.getUserId(), item.getRoleId());
            });
        }
        return new Result<User>(
                Result.ResultStatus.SUCCESS.status,"insert success!",user);
    }

    //使用shiro进行身份验证和资源管理
    @Override
    public Result<User> login(User user) {
        //shiro设置Subject
        Subject subject = SecurityUtils.getSubject();
        //封装令牌类
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(user.getAccountName(),
                        MD5Util.getMD5(user.getPassword()));
        try{
            //调用身份验证器，去验证是否有资格权限（把包装好的令牌类与身份验证器做比对）
            subject.login(usernamePasswordToken);
            //调用资源验证器
            subject.checkRoles();
        }catch(Exception e){
            //没成功登陆
            e.printStackTrace();
            return new Result<>(Result.ResultStatus.FAILD.status,
                    "UserName or Password is error!!");
        }
//        User userTemp = userDao.getUserByUserName(user.getUserName());
//        if(userTemp !=null && userTemp.getPassword().equals(MD5Util.getMD5(user.getPassword()))){
//            return new Result<User>(Result.ResultStatus.SUCCESS.status,"Success!",userTemp);
//        }
        //登陆拿到Session
        Session session = subject.getSession();
        session.setAttribute("user", (User)subject.getPrincipal());
        //成功登陆返回信息
        return new Result<User>(Result.ResultStatus.SUCCESS.status,
                "Login Success!",user);
    }

    @Override
    public PageInfo<User> getUsersBySearchVo (SearchVo searchVo) {
        searchVo.initSearchVo();
        PageHelper.startPage(searchVo.getCurrentPage(),searchVo.getPageSize());
        return new PageInfo<User>(Optional.ofNullable(userDao.getUsersBySearchVo(searchVo))
                .orElse(Collections.emptyList()));
    }

    @Override
    @Transactional
    public Result<User> updateUser(User user) {
        User userTemp = userDao.getUserByUserName(user.getUserName());
        if(userTemp != null && userTemp.getUserId() != user.getUserId()){
            return new Result<User>(
                    Result.ResultStatus.FAILD.status,"userName is repeat !");
        }
        userDao.updateUser(user);

        userRoleDao.deleteUserRoleByUserId(user.getUserId());
        List<Role> roles = user.getRoles();
        if(roles != null && !roles.isEmpty()){
            roles.stream().forEach(item -> {
                userRoleDao.insertUserRole(user.getUserId(), item.getRoleId());
            });
        }

        return new Result<User>(
                Result.ResultStatus.SUCCESS.status,"update Success!",user);
    }

    @Override
    @Transactional
    public Result<Object> deleteUser(int userId) {
        userDao.deleteUser(userId);
        userRoleDao.deleteUserRoleByUserId(userId);
        return new Result<Object>(
                Result.ResultStatus.SUCCESS.status,"delete  success!");
    }

    @Override
    public User getUserByUserId(int userId) {
        return userDao.getUserByUserId(userId);
    }

    @Override
    public Result<String> uploadUserImg(MultipartFile file) {
        if (file.isEmpty()) {
            return new Result<String>(
                    Result.ResultStatus.FAILD.status, "Please select img.");
        }

        String relativePath = "";
        String destFilePath = "";
        try {
            String osName = System.getProperty("os.name");
            if (osName.toLowerCase().startsWith("win")) {
                destFilePath = resourceConfigBean.getLocationPathForWindows() +
                        file.getOriginalFilename();
            } else {
                destFilePath = resourceConfigBean.getLocationPathForLinux()
                        + file.getOriginalFilename();
            }
            relativePath = resourceConfigBean.getRelativePath() +
                    file.getOriginalFilename();
            File destFile = new File(destFilePath);
            file.transferTo(destFile);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result<String>(
                    Result.ResultStatus.FAILD.status, "Upload failed.");
        }

        return new Result<String>(
                Result.ResultStatus.SUCCESS.status, "Upload success.", relativePath);
    }

    @Override
    @Transactional
    public Result<User> updateUserProfile(User user) {
        User userTemp = userDao.getUserByUserName(user.getUserName());
        if (userTemp != null && userTemp.getUserId() != user.getUserId()) {
            return new Result<User>(Result.ResultStatus.FAILD.status, "User name is repeat.");
        }

        userDao.updateUser(user);

        return new Result<User>(Result.ResultStatus.SUCCESS.status, "Edit success.", user);
    }

    @Override
    public User getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    @Override
    public void logput() {
        //shiro设置Subject
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        Session session = subject.getSession();
        session.setAttribute("user",null);
    }
}
