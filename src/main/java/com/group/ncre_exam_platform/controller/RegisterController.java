package com.group.ncre_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import com.group.ncre_exam_platform.config.exception.AjaxResponse;
import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.model.User;
import com.group.ncre_exam_platform.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/saveUser")
    public @ResponseBody
    AjaxResponse register(@Valid @RequestBody User user) {
        registerService.saveUser(user);
        return AjaxResponse.success();
    }

    /**
     * 发送邮件
     * @return
     */
    @PostMapping("/sendEmail")
    public @ResponseBody AjaxResponse sendEmail(@RequestBody String str){
        String receiver = JSON.parseObject(str).get("email").toString();
        //检验邮箱是否为空
        if (receiver == null || receiver.equals("")) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱为空");
        }

        //检验邮箱是否被重复注册 若已被注册则报错
        Integer user_id = registerService.getUserIdByEmail(receiver);
        if (user_id != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱已被注册");
        }

        registerService.sendEmail(receiver);
        return AjaxResponse.success();
    }

}
