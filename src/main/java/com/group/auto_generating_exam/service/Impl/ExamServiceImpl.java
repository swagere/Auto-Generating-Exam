package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.*;
import com.group.auto_generating_exam.model.*;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.SubjectService;
import com.group.auto_generating_exam.util.RedisUtils;
import com.group.auto_generating_exam.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

@Slf4j
@EnableAsync
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
    @Autowired
    RedisUtils redisUtils;
    @Resource
    private Mapper dozerMapper;


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
                    getQuestion.setInput(ToolUtil.String2List(input).get(0));
                    getQuestion.setOutput(ToolUtil.String2List(output).get(0));
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

    //查询学生已获取试卷的题号列表（user_exam_question）
    @Override
    public List<Integer> getStuExamQuestionIds(Integer exam_id, Integer user_id) {
        return userExamQuestionRepository.getUserExamQuestionIds(exam_id, user_id);
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

    //存储学生答案
    @Override
    public void saveAnswerAndScore(String answer, Integer score, Integer question_id, Integer exam_id, Integer user_id) {
        userExamQuestionRepository.saveAnswerAndScore(answer, score, question_id, exam_id, user_id);
    }

    //返回考试信息
    @Override
    public Exam getExamByExamId(Integer exam_id) {
        return examRepository.getExamByExamId(exam_id);
    }

    //返回考试信息
    @Override
    public List<Exam> getExamsByExamId(List<Integer> exam_ids) {
        return examRepository.getExamsByExamId(exam_ids);
    }

    //判断考试是否结束超过一分钟(前提：考试已结束)
    @Override
    public Boolean isExamDoneOverOne(Integer exam_id) {
        //判断考试是否结束超过一分钟
        Long exam_time = examRepository.getBeginTimeByExamId(exam_id);
        Long last_time = examRepository.getLastTimeByExamId(exam_id);
        Long now_time = System.currentTimeMillis();

        if (now_time - exam_time - last_time > 60 * 1000) {
            return true;
        }
        return false;
    }

    //存储是否交卷字段
    @Override
    public void saveIsCommit(Integer is_commit, Integer question_id, Integer exam_id, Integer user_id) {
        userExamQuestionRepository.saveIsCommit(is_commit, question_id, exam_id, user_id);
    }

    //修改/新增考试
    @Override
    public Integer saveExam(Exam exam) {
        //编辑试卷信息
        if (exam.getExam_id() != null) {
            //如果exam_id为空
            exam.setProgress_status(Exam.ProgressStatus.WILL);
            examRepository.save(exam);
            return exam.getExam_id();
        } else {
            //如果exam_id不为空

            Integer exam_id = redisUtils.incr("exam_id");
            //判断redis的exam_id值是否为目前数据库最大
            Integer max = examRepository.getMaxExamId();
            if (max != null && max >= exam_id) {
                exam_id = max + 1;
                redisUtils.set("exam_id", max + 1);
            }
            exam.setProgress_status(Exam.ProgressStatus.WILL);
            System.out.println(exam_id);
            exam.setExam_id(exam_id);
            examRepository.save(exam);
            return exam_id;
        }
    }

    //根据sub_id获得所有考试
    @Override
    public List<Exam> getExamBySubId(String sub_id) {
        return examRepository.getExamBySubId(sub_id);
    }

    //获取一个学生的所有考试
    @Override
    public List<Integer> getExamIdsByUserId(Integer user_id) {
        return userExamQuestionRepository.getExamIdsByUserId(user_id);
    }

    //获得一个考试的成绩
    @Override
    public Integer getExamScore(Integer exam_id, Integer user_id) {
        List<Integer> scores = userExamQuestionRepository.getScoresByExamIdAndUserId(exam_id, user_id);
        int total = 0;
        for (Integer score : scores) {
            if (score != null) {
                total += score;
            }
        }
        return total;
    }

    //获得最大的试题号
    @Override
    public Integer getMaxQuestionId() {
        return questionRepository.getMaxQuestionId();
    }

    //保存一道试题
    @Override
    public long saveQuestion(GetQuestion getQuestion) throws Exception {
        //将getQuestion类转化为question类
        Question question = dozerMapper.map(getQuestion, Question.class);
        questionRepository.save(question);
        return question.getQuestion_id();
    }

    //老师完成评分
    @Override
    public void saveIsJudge(Integer exam_id, int flag) {
        examRepository.saveIsJudge(exam_id, flag);
    }
}

