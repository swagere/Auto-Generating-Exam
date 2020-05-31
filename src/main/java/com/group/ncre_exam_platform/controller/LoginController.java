package com.group.ncre_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import com.group.ncre_exam_platform.config.exception.AjaxResponse;
import com.group.ncre_exam_platform.model.Login;
import com.group.ncre_exam_platform.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;

    /**
     * 用户登陆
     * @param login
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/login")
    public @ResponseBody
    AjaxResponse login(@Valid @RequestBody Login login, HttpServletRequest httpServletRequest) {
//        String username = (String) JSON.parseObject(str).get("username");
//        String pwd = (String) JSON.parseObject(str).get("password");
        String username = login.getUsername();
        String password = login.getPassword();
        loginService.login(username, password);

        Map<String, Object> user = new HashMap<>();
        user.put("user_id", loginService.getUserIdByUsername(username));
        user.put("username", username);
        user.put("session", httpServletRequest.getSession().getId());

        //添加数据到session
        httpServletRequest.getSession().setAttribute("userInfo", user);

        return AjaxResponse.success(user);
    }

}
