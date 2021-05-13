package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.*;
import com.group.auto_generating_exam.model.*;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.SubjectService;
import com.group.auto_generating_exam.service.TrainService;
import com.group.auto_generating_exam.util.ToolUtil;
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
public class TrainServiceImpl implements TrainService {
    @Autowired
    SubjectRepository subjectRepository;
    @Autowired
    ExamService examService;
    @Autowired
    ExamRepository examRepository;
    @Autowired
    TrainRepository trainRepository;
    @Autowired
    UserExamQuestionRepository userExamQuestionRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    TrainQuestionRepository trainQuestionRepository;
    @Autowired
    UserSubjectRepository userSubjectRepository;
    @Autowired
    TestCaseRepository testCaseRepository;
    @Autowired
    SubjectService subjectService;


    //获得章节正确率
    @Override
    public double[] getChapterRatio(String sub_id, Integer user_id) {
//        //--获取科目知识点-------------------------------------
//        List<Integer> chapter_count = ToolUtil.String2ListInt(subjectRepository.getChapterCountBySubId(sub_id));
//
//        //获取科目试卷id
//        List<Integer> examIds = examRepository.getExamIdBySubId(sub_id);
//        List<Integer> trainIds = trainRepository.getTrainIdBySubId(sub_id);
//
//        //获取userExamQuestion
//        List<Map> userQuestions = new ArrayList<>();
//        for (Integer id : examIds) {
//            List<UserExamQuestion> userExamQuestions = userExamQuestionRepository.getUserExamQuestion(id, user_id);
//            for (UserExamQuestion userExamQuestion : userExamQuestions) {
//                Integer question_id = userExamQuestion.getQuestion_id();
//
//                Map temp = new HashMap();
//                temp.put("question_id", question_id);
//                temp.put("is_right", userExamQuestion.getIs_right());
//                temp.put("chapter", questionRepository.getChapterById(question_id));
//                temp.put("hard", questionRepository);
//                temp.put("importance", questionRepository);
//                userQuestions.add(temp);
//            }
//        }
//
//        //获取userTrainQuestion
//        for (Integer id : trainIds) {
//            List<TrainQuestion> userTrainQuestions = userTrainQuestionRepository.getUserTrainQuestion(id, user_id);
//            for (TrainQuestion userTrainQuestion : userTrainQuestions) {
//                Integer question_id = userTrainQuestion.getQuestion_id();
//
//                Map temp = new HashMap();
//                temp.put("question_id", (int)question_id);
//                temp.put("is_right", (int)userTrainQuestion.getIs_right());
//                temp.put("chapter", (int)questionRepository.getChapterById(question_id));
//                temp.put("hard", (double)questionRepository.getHardById(question_id));
//                temp.put("importance", (int)questionRepository.getImportanceById(user_id));
//                userQuestions.add(temp);
//            }
//        }
//
//        //算出要取知识点
//        //每个知识点的正确率
//        int[][] chapter_num = new int[chapter_count.size()][2]; //第0个为正确的个数，第1个为全部个数，3为正确率
//        for (Map temp : userQuestions) {
//            chapter_num[(int) temp.get("chapter")][1] +=  (int)temp.get("is_right");
//            if ((int)temp.get("is_right") == 1) {
//                chapter_num[(int) temp.get("chapter")][0] +=  (int)temp.get("is_right");
//            }
//        }
//        double sum = 0;
//        double[] chapter_ratio = new double[chapter_count.size()];
//        for (int i = 0; i < chapter_count.size(); i++) {
//            chapter_ratio[i] = (double)chapter_num[i][0] / (double)chapter_num[i][1];
//            sum = sum + chapter_ratio[i];
//        }
//        for (int i = 0; i < chapter_count.size(); i++) {
//            chapter_ratio[i] = chapter_ratio[i] / sum;
//        }


        //获取科目知识点占比
        List<Integer> origin_chapter_count = ToolUtil.String2ListInt(subjectRepository.getChapterCountBySubId(sub_id));

        //获取学生知识点正确率
        List<Integer> chapter_right_count = ToolUtil.String2ListInt(userSubjectRepository.getChapterRightCount(sub_id, user_id));;

        List<Integer> chapter_count = ToolUtil.String2ListInt(userSubjectRepository.getChapterCount(sub_id, user_id));

        double sum = 0;
        double[] chapter_ratio = new double[origin_chapter_count.size()];
        for (int i = 0; i < chapter_count.size(); i++) {
            chapter_ratio[i] = (double)chapter_right_count.get(i) / (double)chapter_count.get(i);
            sum = sum + chapter_ratio[i];
        }
        for (int i = 0; i < chapter_count.size(); i++) {
//            chapter_ratio[i] = (chapter_ratio[i] / sum) * 0.4 + (double)origin_chapter_count.get(i) * 0.6;
            chapter_ratio[i] = chapter_ratio[i] / sum;
        }

        return chapter_ratio;
    }


    //获得难度正确率
    @Override
    public double[] getHardRatio(String sub_id, Integer user_id) {
        //获取学生难度正确率
        List<Integer> hard_right_count = ToolUtil.String2ListInt(userSubjectRepository.getHardRightCount(sub_id, user_id));;

        List<Integer> hard_count = ToolUtil.String2ListInt(userSubjectRepository.getHardCount(sub_id, user_id));

        double sum = 0;
        double[] hard_ratio = new double[hard_right_count.size()];
        for (int i = 0; i < hard_right_count.size(); i++) {
            hard_ratio[i] = (double)hard_right_count.get(i) / (double)hard_count.get(i);
            sum = sum + hard_ratio[i];
    }
        for (int i = 0; i < hard_right_count.size(); i++) {
            hard_ratio[i] = hard_ratio[i] / sum;
    }

        return hard_ratio;
    }

    //获得重要性正确率
    @Override
    public double[] getImportanceRatio(String sub_id, Integer user_id) {
        //获取学生重要性正确率
        List<Integer> impo_right_count = ToolUtil.String2ListInt(userSubjectRepository.getImportanceRightCount(sub_id, user_id));;

        List<Integer> impo_count = ToolUtil.String2ListInt(userSubjectRepository.getImportanceCount(sub_id, user_id));

        double sum = 0;
        double[] impo_ratio = new double[impo_right_count.size()];
        for (int i = 0; i < impo_right_count.size(); i++) {
            impo_ratio[i] = (double)impo_right_count.get(i) / (double)impo_count.get(i);
            sum = sum + impo_ratio[i];
        }
        for (int i = 0; i < impo_right_count.size(); i++) {
            impo_ratio[i] = impo_ratio[i] / sum;
        }

        return impo_ratio;
    }

    //更新sub_id user_id train_time
    @Override
    public void saveUserIdSubIdAndTrainTimeByTrainId (Integer user_id, String sub_id, Long train_time, Integer train_id) {
        trainRepository.updateUserIdSubIdAndTrainTimeByTrainId(user_id, sub_id, train_time, train_id);
    }

    //根train_id获得question_ids
    @Override
    public List<Integer> getQuestionIdByTrainId(Integer train_id) {
        return trainQuestionRepository.getQuestionIdByTrainId(train_id);
    }

    //获得一个用户所有train试卷
    @Override
    public List<Train> getAllTrain(Integer user_id) {
        List<Train> trains = trainRepository.getTrainByUserId(user_id);
        List res = new ArrayList();
        for (Train train : trains) {
            Map m = new HashMap();
            m.put("train_id", train.getTrain_id());
            m.put("train_name", train.getTrain_name());
            m.put("train_type", train.getTrain_type());
            m.put("last_time", train.getLast_time() / 1000);

            res.add(m);
        }
        return res;
    }

    //根据question_id获取question
    @Override
    public Map getTrainQuestionList(List<Integer> questions) {

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

                //传到前端页面
                GetExamQuestion getQuestion = new GetExamQuestion(question_id, question.getContent(), question.getOptions(), question.getKind(), question.getTip(),null, null);
                if (question.getKind().equals(0)) {
                    singleList.add(getQuestion);
                }
                else if (question.getKind().equals(1)) {
                    judgeList.add(getQuestion);
                }
                else if (question.getKind().equals(2)) {
                    discussionList.add(getQuestion);
                }
                else if (question.getKind().equals(3) || question.getKind().equals(4)) {
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

    //判断检测是否存在
    @Override
    public Boolean isTrainExist(Integer train_id) {
        Integer train = trainRepository.isTrainExist(train_id);
        if (train == null) {
            return false;
        }
        return true;
    }

    //用户是否有该门检测
    @Override
    public Boolean isStuInTrain(Integer train_id, Integer user_id) {
        String sub_id = trainRepository.getSubIdByTrainId(train_id);
        return subjectService.isStuInSub(sub_id, user_id);
    }

    //判断检测是否正在进行
    @Override
    public String trainIsProgressing(Integer train_id) {
        //判断考试时间是否开始 考试是否结束
        Long train_time = trainRepository.getBeginTimeByTrainId(train_id);
        Long last_time = trainRepository.getLastTimeByTrainId(train_id);
        Long now_time = System.currentTimeMillis();

        if (train_time > now_time) {
            return "will";
        }
        else if (train_time + last_time < now_time) {
            return "over";
        }
        return "ing";
    }

    //判断考试是否结束超过一分钟(前提：考试已结束)
    @Override
    public Boolean isTrainDoneOverOne(Integer train_id) {
        //判断考试是否结束超过一分钟
        Long exam_time = trainRepository.getBeginTimeByTrainId(train_id);
        Long last_time = trainRepository.getLastTimeByTrainId(train_id);
        Long now_time = System.currentTimeMillis();

        if (now_time - exam_time - last_time > 60 * 1000) {
            return true;
        }
        return false;
    }

    //查询学生已获取试卷的题号列表（train_question）
    @Override
    public List<Integer> getTrainQuestionIds(Integer train_id) {
        return trainQuestionRepository.getQuestionIdByTrainId(train_id);
    }

    //用户是否已交卷
    @Override
    public Boolean isCommit(Integer train_id) {
        List<TrainQuestion> list = trainQuestionRepository.getTrainQuestion(train_id);
        if (!list.isEmpty()) {
            Integer isCommit = list.get(0).getIs_commit();
            if (isCommit == 1) {
                return true;
            }
        }
        return false;
    }

    //存储是否交卷字段
    @Override
    public void saveIsCommit(Integer is_commit, Integer question_id, Integer train_id) {
        trainQuestionRepository.saveIsCommit(is_commit, question_id, train_id);
    }

}

