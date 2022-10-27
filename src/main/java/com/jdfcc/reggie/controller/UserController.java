package com.jdfcc.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jdfcc.reggie.common.R;
import com.jdfcc.reggie.entity.User;
import com.jdfcc.reggie.service.UserService;
import com.jdfcc.reggie.utils.SMSUtils;
import com.jdfcc.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
        log.info("Code:{}", code);
//        发送验证码
//        SMSUtils.sendMessage("瑞吉外卖","empty",phone,code);
        session.setAttribute(phone, code);
        return R.success("Verification code sent successfully");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");
        String codeInSession = (String) session.getAttribute(phone);
        if (code != null && code.equals(codeInSession)) {
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getPhone, phone);
            User user = userService.getOne(wrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            return R.success(user);
        } else return R.error("Failed login");
    }

}
