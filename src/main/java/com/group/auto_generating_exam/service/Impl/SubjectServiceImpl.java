package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.*;
import com.group.auto_generating_exam.model.GetExamQuestion;
import com.group.auto_generating_exam.model.Question;
import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserSubject;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.SubjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

@Slf4j
@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    UserSubjectRepository userSubjectRepository;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //获取试卷列表（学生开始答题）
    @Override
    public Boolean isStuInSub(String sub_id, Integer user_id) {
        UserSubject userSubject = userSubjectRepository.getOneBySubIdAndUserId(sub_id, user_id);
        if (userSubject != null) {
            return true;
        }
        return false;
    }

    @Override
    public Integer getUserIdBySubId(String sub_id) {
        return subjectRepository.getUserIdBySubId(sub_id);
    }
}

