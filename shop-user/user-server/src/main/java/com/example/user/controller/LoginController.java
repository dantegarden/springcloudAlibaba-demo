package com.example.user.controller;

import cn.hutool.system.UserInfo;
import com.alibaba.fastjson.JSON;
import com.example.bean.Result;
import com.example.user.domain.User;
import com.example.user.dto.UserInfoDTO;
import com.example.user.properties.UserProperties;
import com.example.user.service.UserService;
import com.example.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: lij
 * @create: 2020-02-26 22:39
 */
@RestController
@RequestMapping("/")
public class LoginController {

    @Autowired
    private UserProperties userProperties;

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    public Result authenticate(@RequestParam String username, @RequestParam String password, HttpServletResponse response){
        User loginUser = userService.findByUsernameAndPassword(username, password);
        if(loginUser == null){
            return Result.fail("用户名或密码错误");
        }

        //redis设置 key=token_UUID, value=userInfo
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(loginUser, userInfoDTO);
        userInfoDTO.setRole(1); //这里先写死，一般要再查一步角色
        String uuid = setToken2Redis(userInfoDTO);
        //设置cookie x-token=UUID
        CookieUtil.set(response, userProperties.getTokenKey(), uuid, userProperties.getExpire());
        return Result.ok(uuid); //回给他token
    }

    /**统一登陆**/
    @GetMapping("/userInfo")
    public Result userLogin(@RequestParam("token") String token, HttpServletRequest request, HttpServletResponse response){
        Cookie cookie = CookieUtil.get(request, userProperties.getTokenKey());
        if(cookie==null){
            return Result.fail("token无效或过期");
        }
        String userInfoJson = stringRedisTemplate.opsForValue().get(cookie.getValue());
        if(StringUtils.isBlank(userInfoJson)){
            return Result.fail("token无效或过期");
        }
        return Result.ok(JSON.parseObject(userInfoJson, UserInfoDTO.class));
    }

    private String setToken2Redis(UserInfoDTO userInfoDTO){
        String uuid = UUID.randomUUID().toString();
        String key = String.format("token_%s", uuid);
        Integer expire = userProperties.getExpire();
        stringRedisTemplate.opsForValue().set(key, JSON.toJSONString(userInfoDTO), expire, TimeUnit.SECONDS);
        return key;
    }
}
