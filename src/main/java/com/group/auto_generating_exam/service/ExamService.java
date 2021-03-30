package com.group.auto_generating_exam.service;

import java.util.Map;

public interface ExamService {
    Map getExamQuestionList(Integer exam_id, Integer user_id);
    String examIsProgressing(Integer exam_id);
    Boolean isExamExist(Integer exam_id);
    Boolean isStuInExam(Integer exam_id, Integer user_id);
}
