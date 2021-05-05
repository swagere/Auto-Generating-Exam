package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.config.exception.CustomException;
import com.group.auto_generating_exam.config.exception.CustomExceptionType;
import com.group.auto_generating_exam.dao.UserRepository;
import com.group.auto_generating_exam.model.Login;
import com.group.auto_generating_exam.model.User;
import com.group.auto_generating_exam.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    UserRepository userRepository;

    //手机号加密码
    @Override
    public Login loginPhone(Login login) {
        User user = userRepository.getUserByTelephone(login.getTelephone());
        if (user != null) {      //如果数据库里面已存在
            if (BCrypt.checkpw(login.getPassword(), user.getPassword())) {

                log.info("学生登录验证成功");
                login.setId(user.getUser_id());
                login.setAuthority(user.getAuthority());
                return login;

            } else {
                log.info("用户名/密码错误");
                throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户名/密码错误");     //密码错误
            }

        }
        log.info("用户不存在");
        throw new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户不存在/密码错误");      //用户不存在
    }

}
