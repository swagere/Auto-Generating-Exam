package com.group.ncre_exam_platform.service;

import com.group.ncre_exam_platform.model.Exam;

import java.util.ArrayList;

public interface ExamService {
    ArrayList getRealExamList(String s);
    Exam.Subject String2Subject(String s);
}
