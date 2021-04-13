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
public class ExamServiceImpl implements ExamService {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    ExamQuestionRepository examQuestionRepository;
    @Autowired
    UserExamQuestionRepository userExamQuestionRepository;
    @Autowired
    SubjectService subjectService;
    @Autowired
    TestCaseRepository testCaseRepository;

    //获取试卷列表（学生开始答题）
    @Override
    public Map<String, List<GetExamQuestion>> getExamQuestionList(Integer exam_id, Integer user_id) {
        List<Integer> questions = examQuestionRepository.getQuestionIdByExamId(exam_id);

        Map<String, List<GetExamQuestion>> questionList = new HashMap<>();
        List<GetExamQuestion> singleList = new ArrayList<>();

        List<GetExamQuestion> judgeList = new ArrayList<>();

        List<GetExamQuestion> discussionList = new ArrayList<>();

        List<GetExamQuestion> programList = new ArrayList<>();
        if (!questions.isEmpty()) {
            // 如果试卷不为空 在question表里获取题目信息

            for (Integer question_id : questions)
            {
                Question question =  questionRepository.getQuestionByQuestionId(question_id);

                //保存到数据库
                UserExamQuestion userExamQuestion = new UserExamQuestion(user_id, exam_id, question_id,0, null, 0, 0, 0);
                userExamQuestionRepository.save(userExamQuestion);

                //传到前端页面
                GetExamQuestion getQuestion = new GetExamQuestion(question_id, question.getQuestion(), question.getOptions(), question.getQuestion_type(), question.getTip(),null, null);
                if (question.getQuestion_type() == Question.QuestionType.Single) {
                    singleList.add(getQuestion);
                }
                else if (question.getQuestion_type() == Question.QuestionType.Judge) {
                    judgeList.add(getQuestion);
                }
                else if (question.getQuestion_type() == Question.QuestionType.Discussion) {
                    discussionList.add(getQuestion);
                }
                else if (question.getQuestion_type() == Question.QuestionType.Normal_Program || question.getQuestion_type() == Question.QuestionType.SpecialJudge_Program) {
                    TestCase testCase = testCaseRepository.getTestCaseByQuestionId(question_id);
                    String input = testCase.getInput();
                    String output = testCase.getOutput();
                    getQuestion.setInput(input);
                    getQuestion.setOutput(output);
                    programList.add(getQuestion);
                }
            }

            questionList.put("Single", singleList);
            questionList.put("Judge", judgeList);
            questionList.put("Discussion", discussionList);
            questionList.put("Program", programList);
        }
        return questionList;
    }

    //判断考试是否存在
    @Override
    public Boolean isExamExist(Integer exam_id) {
        Integer exam = examRepository.isExamExist(exam_id);
        if (exam == null) {
            return false;
        }
        return true;
    }

    //判断考试是否正在进行
//    @Override
//    public String examIsProgressing(Integer exam_id) {
//        //判断考试时间是否开始 考试是否结束
//        Long exam_time = examRepository.getBeginTimeByExamId(exam_id);
//        Long last_time = examRepository.getLastTimeByExamId(exam_id);
//        Long now_time = System.currentTimeMillis();
//
//        if (exam_time > now_time) {
//            return "will";
//        }
//        else if (exam_time + last_time * 60000 < now_time) {
//            return "over";
//        }
//        return "progressing";
//    }

    //用户是否有该门考试
    @Override
    public Boolean isStuInExam(Integer exam_id, Integer user_id) {
        String sub_id = examRepository.getSubIdByExamId(exam_id);
        return subjectService.isStuInSub(sub_id, user_id);
    }

    //用户是否已交卷
    @Override
    public Boolean isCommit(Integer exam_id, Integer user_id) {
        List<UserExamQuestion> list = userExamQuestionRepository.getUserExamQuestion(exam_id, user_id);
        if (!list.isEmpty()) {
            Integer isCommit = list.get(0).getIs_commit();
            if (isCommit == 1) {
                return true;
            }
        }
        return false;
    }

    //返回考试状态
    @Override
    public Exam.ProgressStatus getExamProgressStatus(Integer exam_id) {
        return examRepository.getProgressStatusByExamId(exam_id);
    }

    //判断是否已经获取试卷（user_exam_question）
    @Override
    public Boolean isGetStuExamQuestion(Integer exam_id, Integer user_id) {
        List<UserExamQuestion> questions = userExamQuestionRepository.getUserExamQuestion(exam_id, user_id);
        if (questions.isEmpty()) {
            return false;
        }
        return true;
    }

    //根据exam_id获取sub_id
    @Override
    public String getSubIdByExamId(Integer exam_id) {
        return examRepository.getSubIdByExamId(exam_id);
    }

    //更新考试持续时间
    @Override
    public void saveLastTime(Long last_time, Integer exam_id) {
        examRepository.updateLastTime(last_time, exam_id);
    }

    //结束考试
    @Override
    public void endExam(Integer exam_id) {
        //获得考试开始时间
        Long begin_time = examRepository.getBeginTimeByExamId(exam_id);
        Long now_time = System.currentTimeMillis();
        Long last_time = now_time - begin_time;
        examRepository.updateLastTime(last_time, exam_id);
        examRepository.updateProgressStatus(exam_id, Exam.ProgressStatus.DONE);
    }

    //返回考试剩余时间 如果考试未开始则返回null
    @Override
    public Long getRestTimeByExamId(Integer exam_id, Long last_time) {
        Long begin_time = examRepository.getBeginTimeByExamId(exam_id);
        Long now_time = System.currentTimeMillis();
        return last_time * 60000 - (now_time - begin_time);
    }

    //返回考试持续时间
    @Override
    public Long getLastTime(Integer exam_id) {
        return examRepository.getLastTimeByExamId(exam_id);
    }
}

