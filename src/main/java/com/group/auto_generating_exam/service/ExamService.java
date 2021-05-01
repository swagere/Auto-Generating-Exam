package com.group.auto_generating_exam.service;

import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.model.ExamQuestion;
import com.group.auto_generating_exam.model.GetQuestion;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface ExamService {
    Map getExamQuestionList(Integer exam_id, Integer user_id);
    String examIsProgressing(Integer exam_id);
    Boolean isExamExist(Integer exam_id);
    Boolean isStuInExam(Integer exam_id, Integer user_id);
    Boolean isCommit(Integer exam_id, Integer user_id);
    List<Integer> getStuExamQuestionIds(Integer exam_id, Integer user_id);
    String getSubIdByExamId(Integer exam_id);
    void saveLastTime(Long last_time, Integer exam_id);
    void endExam(Integer exam_id);
    Long getRestTimeByExamId(Integer exam_id, Long last_time);
    Long getLastTime(Integer exam_id);
    void saveAnswerAndScore(String answer, Integer score, Integer question_id, Integer exam_id, Integer user_id);
    Exam getExamByExamId(Integer exam_id);
    List<Exam> getExamsByExamId(List<Integer> exam_ids);
    Boolean isExamDoneOverOne(Integer exam_id);
    void saveIsCommit(Integer is_commit, Integer question_id, Integer exam_id, Integer user_id);
    Integer saveExam(Exam exam);
    List<Exam> getExamBySubId(String sub_id);
    List<Integer> getExamIdsByUserId(Integer user_id);
    Integer getExamScore(Integer exam_id, Integer user_id);
    Integer getMaxQuestionId();
    long saveQuestion(GetQuestion getQuestion)  throws Exception;
    void saveIsJudge(Integer exam_id, int flag);
    void saveUserExamQuestionScore(int score, int question_id, int exam_id, int user_id);
    void saveUserExamQuestionIsJudge(Integer exam_id, int flag);
    void judgeGeneralQuestion(Integer exam_id);
    void saveIsRight(Integer is_right, Integer exam_id, Integer question_id, Integer user_id);
    Integer getExamQuestionScore(Integer question_id, Integer exam_id);
    void saveExamQuestion(ExamQuestion examQuestion);
    Map getDiscussion(Integer exam_id);
    Integer getKindByQuestionId(Integer question_id);
}
