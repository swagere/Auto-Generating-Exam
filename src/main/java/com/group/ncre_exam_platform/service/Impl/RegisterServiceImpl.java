package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.config.exception.CustomException;
import com.group.ncre_exam_platform.config.exception.CustomExceptionType;
import com.group.ncre_exam_platform.dao.UserRepository;
import com.group.ncre_exam_platform.model.User;
import com.group.ncre_exam_platform.service.RegisterService;
import com.group.ncre_exam_platform.service.SendMailService;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    SendMailService sendMailService;

    @Override
    public void saveUser(User user) {
        //检验姓名是否重复
        checkNameRepeat(user.getName());

        //检验邮箱是否重复
        checkEmailRepeat(user.getEmail());

        //检查验证码是否正确


        //密码加密存储
        try {
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
        }catch (Exception e) {
            throw new CustomException(CustomExceptionType.SYSTEM_ERROR,"密码加密失败");
        }

        //存入数据库
        userRepository.save(user);
    }

    @Override
    public void sendEmail(String receiver) {
        //如果邮箱格式不正确（正则表达式验证）
        if (!(receiver.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+"))) {
            log.info("邮箱格式不正确");
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱格式不正确");
        }

        //向该邮箱发送验证码邮件
        sendMailService.sendEmail(receiver);
    }

    @Override
    public void checkEmailRepeat(String receiver) {
        Integer user_id = userRepository.getUserIdByEmail(receiver);
        if (user_id != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"邮箱已被注册");
        }
    }

    @Override
    public void checkNameRepeat(String name) {
        Integer user_id = userRepository.getUserIdByName(name);
        if (user_id != null) {
            throw new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户名已被注册");
        }
    }
}
