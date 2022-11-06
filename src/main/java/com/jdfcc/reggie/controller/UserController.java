package com.jdfcc.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jdfcc.reggie.common.BaseContext;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.User;
import com.jdfcc.reggie.service.UserService;
import com.jdfcc.reggie.utils.SMSUtils;
import com.jdfcc.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (StringUtils.isEmpty(phone))
            return R.error("Empty phone number");
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("Verification:{}", code);
//        发送验证码
//        SMSUtils.sendMessage("瑞吉外卖","empty",phone,code);
//        session.setAttribute(phone, code);

        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);//将验证码存储进redis，设置过期时间为5分钟

        return R.success("Verification code sent successfully");
    }

    /**
     * 登录
     *
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

//        String codeInSession = (String) session.getAttribute(phone);
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);//从redis里获取验证码

        if (code != null && code.equals(codeInSession)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }

            session.setAttribute("user", user.getId());
            //登录成功，删除缓存里面的验证码
            redisTemplate.delete(phone);
            return R.success(user);
        } else return R.error("Failed login");
    }

    /**
     * 登出
     *
     * @param request
     * @return
     */
    @PostMapping("/loginout")
    public R<String> logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("Successful logOut");
    }

}
