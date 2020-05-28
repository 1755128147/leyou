package com.wedu.leyou.user.web;

import com.wedu.leyou.user.pojo.User;
import com.wedu.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 校验数据
     * @param data 校验数据
     * @param type 校验类型
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data") String data,@PathVariable("type") Integer type) {
        return ResponseEntity.ok(userService.checkData(data,type));
    }

    /**
     * 发送手机验证码
     * @param phone
     */
    @PostMapping("code")
    public void sendCode(@RequestParam("phone") String phone) {
        userService.sendCode(phone);
        ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 用户注册
     * @param user 用户信息
     * @param code 短信验证码
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 根据参数中的用户名和密码查询指定用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,@RequestParam("password") String password) {
        return ResponseEntity.ok(userService.queryUser(username,password));
    }
}
