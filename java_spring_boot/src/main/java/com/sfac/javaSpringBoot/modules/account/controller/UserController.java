package com.sfac.javaSpringBoot.modules.account.controller;

import com.github.pagehelper.PageInfo;
import com.sfac.javaSpringBoot.modules.account.entity.User;
import com.sfac.javaSpringBoot.modules.account.service.UserService;
import com.sfac.javaSpringBoot.modules.common.vo.Result;
import com.sfac.javaSpringBoot.modules.common.vo.SearchVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     *127.0.0.1/api/user --- post
     * {"userName":"admin","password":"111111"}
     */
    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<User> insertUser(@RequestBody User user){
        return userService.insertUser(user);
    }

    /*
     *127.0.0.1/api/login
     * {"userName":"admin","password":"111111"}
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<User> login(@RequestBody User user){
        return userService.login(user);
    }

    /**
     * 127.0.0.1/api/users ---- post
     * {"currentPage":"1","pageSize":"5","keyword":"Li"}
     */
    @PostMapping(value = "/users",consumes = MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<User> getUsersBySearchVo(@RequestBody SearchVo searchVo) {
        return userService.getUsersBySearchVo(searchVo);
    }

    /*
     *127.0.0.1/api/user  ----  put
     * {"userName":"LiLei6","userImg":"/aaa.jpg","userId":"8"}
     */
    @PutMapping(value = "/user",consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<User> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for(int i = 0;i <100;i++){
            for(int j=0;j<1000;j++){
                System.out.println("===========");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("time= "+(end-start));
    }

    /*
     *127.0.0.1/api/user/1 --- get
     */
    @GetMapping("/user/{userId}")
    public User getUserByUserId(@PathVariable int userId){
        return userService.getUserByUserId(userId);
    }

    /*
     *127.0.0.1/api/userImg ---- post
     */
    @PostMapping(value = "/userImg", consumes = "multipart/form-data")
    //上传文件的参数应该写（）
    public Result<String> uploadFile(@RequestParam MultipartFile file){
        return userService.uploadUserImg(file);
    }

    /*
     *127.0.0.1/api/user/9 --- delete
     */
    @DeleteMapping("/user/{userId}")
    @RequiresPermissions(value = "/api/user")       //shiro的资源权限管理，只有admin才能调用该操作
    public Result<Object> deleteUser(@PathVariable int userId){
        return userService.deleteUser(userId);
    }

//    /*
//     *127.0.0.1/api/logout --- get
//     */
//    @GetMapping("/logout")
//    public void logout(){
//        userService.logput();
//    }

    /**
     * 127.0.0.1/api/profile ---- put
     */
    @PutMapping(value = "/profile", consumes = "application/json")
    public Result<User> updateUserProfile(@RequestBody User user) {
        return userService.updateUserProfile(user);
    }
}
