package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.dao.ExamRepository;
import com.group.ncre_exam_platform.dao.QuestionRepository;
import com.group.ncre_exam_platform.model.Exam;
import com.group.ncre_exam_platform.model.Question;
import com.group.ncre_exam_platform.service.ExamService;
import com.group.ncre_exam_platform.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    QuestionRepository questionRepository;

    //获取真题集列表
    @Override
    public ArrayList getRealExamList(String s) {
        Exam.Subject subject = String2Subject(s);
        return examRepository.getExamByExam_typeAndSubject(Exam.ExamType.REAL, subject);
    }

    //string转subject(exam)
    @Override
    public Exam.Subject String2Subject(String s){
        Exam.Subject subject = null;
        switch (s) {
            case "MSOffice":
                subject = Exam.Subject.MSOffice;
                break;
            case "Web":
                subject = Exam.Subject.Web;
                break;
            case "C":
                subject = Exam.Subject.C;
                break;
            case "Cpp":
                subject = Exam.Subject.Cpp;
                break;
            case "Java":
                subject = Exam.Subject.Java;
                break;
            case "VB":
                subject = Exam.Subject.VB;
                break;
            case "Python":
                subject = Exam.Subject.Python;
                break;
            case "MySQL":
                subject = Exam.Subject.MySQL;
                break;
            case "Access":
                subject = Exam.Subject.Access;
                break;

        }
        return subject;
    }

}
