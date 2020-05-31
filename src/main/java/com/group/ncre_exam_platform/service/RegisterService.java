package com.group.ncre_exam_platform.service;

import com.group.ncre_exam_platform.model.User;

public interface RegisterService {
    void saveUser(User user);
    void sendEmail(String receiver);
    void checkEmailRepeat(String receiver);
    void checkNameRepeat(String name);
}
