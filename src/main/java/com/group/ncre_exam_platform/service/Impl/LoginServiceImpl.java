package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

    @Override
    public void login(String username, String pwd) {
        if (username.equals("admin") && pwd.equals("admin")) {
            log.info("用户登陆成功");
        }
        else {
            log.info("用户名/密码错误");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
        }
    }
}
