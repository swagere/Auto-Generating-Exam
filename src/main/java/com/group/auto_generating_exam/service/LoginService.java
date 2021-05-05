package com.group.auto_generating_exam.service;


import com.group.auto_generating_exam.model.Login;

public interface LoginService {

    //手机号加密码
    Login loginPhone(Login login);
}
