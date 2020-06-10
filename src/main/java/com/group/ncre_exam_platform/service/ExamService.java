package com.group.ncre_exam_platform.service;

import com.group.ncre_exam_platform.model.Exam;

import java.util.ArrayList;

public interface ExamService {
    ArrayList getRealExamList(String s);
    ArrayList getRealExam(Integer exam_id, Integer user_id);

    String List2String(ArrayList<String> list);
    ArrayList<String> String2List(String s);
    Exam.Subject String2Subject(String s);
}
