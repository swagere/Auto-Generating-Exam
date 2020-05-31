package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.dao.UserRepository;
import com.group.ncre_exam_platform.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void login(String username, String pwd) {

        if (pwd.equals(userRepository.getPasswordByName(username))) {
            log.info("用户登陆成功");
        }
        else {
            log.info("用户名/密码错误");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
        }
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        return userRepository.getUserIdByName(username);
    }
}