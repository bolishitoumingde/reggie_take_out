package com.example.take_out.cotroller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.take_out.cotroller.utils.R;
import com.example.take_out.entity.User;
import com.example.take_out.service.IUserService;
import com.example.take_out.utils.BaseContext;
import com.example.take_out.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.interfaces.PBEKey;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 手机验证码
     *
     * @param user    用户信息
     * @param session 存取session
     * @return 成功或失败
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String phone = user.getPhone();
        if (Strings.isNotEmpty(phone)) {
            session.setAttribute(phone, "1111");
            return R.success("验证码发送成功");
        }
        return R.error("发送失败");
    }

    /**
     * 移动端用户注册
     *
     * @param map     手机号和验证码
     * @param session session
     * @return 成功或失败
     */
    @PostMapping("/login")
    public R<Object> login(@RequestBody Map map, HttpSession session) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        if (Objects.equals(code, session.getAttribute(phone))) {
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getPhone, phone);
            User user = userService.getOne(lqw);
            // 新用户
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                userService.save(user);
            }
            ServletUtil.getSession().setAttribute("user", user.getId());
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone, phone);
        User user = userService.getOne(lqw);
        // 新用户
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            userService.save(user);
        }
        ServletUtil.getSession().setAttribute("user", user.getId());
        return R.success(user);
        // return R.error("登录失败");
    }
}
