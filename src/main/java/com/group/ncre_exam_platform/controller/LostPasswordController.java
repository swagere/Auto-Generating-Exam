package com.group.ncre_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import com.group.ncre_exam_platform.config.exception.AjaxResponse;
import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.dao.UserRepository;
import com.group.ncre_exam_platform.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/lostPassword")
public class LostPasswordController {
    @Autowired
    RegisterService registerService;
    @Autowired
    UserRepository userRepository;

    /**
     * 忘记密码时 发送邮件验证码
     * @param string 邮箱号
     * @return
     */
    @PostMapping("/sendEmail")
    public @ResponseBody
    AjaxResponse sendEmail(@RequestBody String string) {
        String receiver = JSON.parseObject(string).get("email").toString();
        //检验邮箱是否为空
        if (receiver == null || receiver.equals("")) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱为空");
        }

        //检验邮箱是否被注册 若未被注册则报错
        Integer user_id = registerService.getUserIdByEmail(receiver);
        if (user_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱未被注册");
        }

        registerService.sendEmail(receiver);
        return AjaxResponse.success();
    }

    /**
     * 重置密码
     * @param string 邮箱、密码、验证码
     * @return
     */
    @PostMapping("/resetPassword")
    public @ResponseBody AjaxResponse savePassword(@RequestBody String string) {
        String email = JSON.parseObject(string).get("email").toString();
        String password = JSON.parseObject(string).get("password").toString();
        String code = JSON.parseObject(string).get("code").toString();

        //邮箱验证
        //检验邮箱是否为空
        if (email == null || email.equals("")) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱为空");
        }
        //检验邮箱是否被注册 若未被注册则报错
        Integer user_id = registerService.getUserIdByEmail(email);
        if (user_id == null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱未被注册");
        }

        //验证码验证 redis


        //密码加密保存到数据库
        try {
            userRepository.savePassword(email, BCrypt.hashpw(password,BCrypt.gensalt()));
        }catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"密码加密失败");
        }

        return AjaxResponse.success();
    }
}
