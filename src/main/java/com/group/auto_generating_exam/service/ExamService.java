package com.group.auto_generating_exam.service;

import com.group.auto_generating_exam.model.Exam;

import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface ExamService {
    Map getExamQuestionList(Integer exam_id, Integer user_id);
//    String examIsProgressing(Integer exam_id);
    Boolean isExamExist(Integer exam_id);
    Boolean isStuInExam(Integer exam_id, Integer user_id);
    Boolean isCommit(Integer exam_id, Integer user_id);
    Exam.ProgressStatus getExamProgressStatus(Integer Exam);
    Boolean isGetStuExamQuestion(Integer exam_id, Integer user_id);
    String getSubIdByExamId(Integer exam_id);
    void saveLastTime(Long last_time, Integer exam_id);
    void endExam(Integer exam_id);
}
