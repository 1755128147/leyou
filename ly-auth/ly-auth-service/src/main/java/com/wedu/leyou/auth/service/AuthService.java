package com.wedu.leyou.auth.service;

import com.wedu.leyou.auth.client.UserClient;
import com.wedu.leyou.auth.config.JwtProperties;
import com.wedu.leyou.common.enums.ExceptionEnum;
import com.wedu.leyou.common.exception.LyException;
import com.wedu.leyou.auth.pojo.UserInfo;
import com.wedu.leyou.auth.utils.JwtUtils;
import com.wedu.leyou.common.utils.CookieUtils;
import com.wedu.leyou.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private UserClient userClient;

    public void login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 查询用户
            User user = this.userClient.queryUser(username, password);
            if (null == user) {
                throw new LyException(ExceptionEnum.USER_INFO_NOT_FOUND);
            }
            // 生成token
            String token = JwtUtils.generateToken(
                    new UserInfo(user.getId(), user.getUsername()), prop.getPrivateKey(), prop.getExpire());
            // 登录校验
            if (StringUtils.isBlank(token)) {
                throw new LyException(ExceptionEnum.UN_AUTHORIZED);
            }
            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.setCookie(request, response, prop.getCookieName(),
                    token, prop.getCookieMaxAge(), null, true);
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
    }

    public UserInfo verifyUser(String token,HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取token信息
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            // 如果成功，刷新token
            String newToken = JwtUtils.generateToken(userInfo,prop.getPrivateKey(), prop.getExpire());
            // 将token写入cookie,并指定httpOnly为true，防止通过JS获取和修改
            CookieUtils.setCookie(request, response, prop.getCookieName(),
                    newToken, prop.getCookieMaxAge(), null, true);
            return userInfo;
        }catch (Exception e) {
            // 抛出异常，证明token无效，直接返回401
            throw new LyException(ExceptionEnum.UN_AUTHORIZED);
        }
    }
}