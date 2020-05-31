package com.group.ncre_exam_platform.service;

import org.springframework.mail.MailSendException;

public interface SendMailService {

    void sendEmail(String receiver) throws MailSendException;

    Boolean verification(String email, String unverifiedCode);
}
