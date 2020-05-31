package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.dao.UserRepository;
import com.group.ncre_exam_platform.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void login(String name, String pwd) {
        try {
            String dbPwd = userRepository.getPasswordByName(name);
            //如果密码正确
            if (BCrypt.checkpw(pwd, dbPwd)) {
                log.info("用户登陆成功");
            }
            else {
                log.info("用户名/密码错误");
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
            }
        }catch (Exception e) {
            //如果找不到该用户对应的密码
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //用户不存在
        }
    }

    @Override
    public Integer getUserIdByUsername(String username) {
        return userRepository.getUserIdByName(username);
    }
}
