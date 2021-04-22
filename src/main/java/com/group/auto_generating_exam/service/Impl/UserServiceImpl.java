package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.UserRepository;
import com.group.auto_generating_exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @Author KVE
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    //获取用户名
    @Override
    public String getUserNameByUserId(Integer user_id) {
        return userRepository.getNameByUserId(user_id);
    }

}

