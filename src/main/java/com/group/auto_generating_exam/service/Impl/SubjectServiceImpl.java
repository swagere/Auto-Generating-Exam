package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.*;
import com.group.auto_generating_exam.model.*;
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

    //根据sub_id获取老师id
    @Override
    public Integer getUserIdBySubId(String sub_id) {
        return subjectRepository.getUserIdBySubId(sub_id);
    }

    //根据sub_id获取课程名
    @Override
    public String getSubNameBySubId(String sub_id) {
        return subjectRepository.getSubNameBySubId(sub_id);
    }

    @Override
    public List<Subject> getSubjectByUserId(Integer user_id) {
        return subjectRepository.getSubjectByUserId(user_id);
    }

    //根据学生id获取sub_ids
    @Override
    public List<String> getSubIdByUserId(Integer user_id) {
        return userSubjectRepository.getSubIdByUserId(user_id);
    }

    //根据sub_id获取subject
    @Override
    public Subject getSubjectBySubjectId(String sub_id) {
        return subjectRepository.getSubjectBySubId(sub_id);
    }

    //根据sub_id获取chapter
    @Override
    public String getChapterNameBySubId(String sub_id) {
        return subjectRepository.getChapterNameBySubId(sub_id);
    }
}

