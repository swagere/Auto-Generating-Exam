package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.*;
import com.group.auto_generating_exam.model.Question;
import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserTrainQuestion;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.TrainService;
import com.group.auto_generating_exam.service.UserService;
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
    UserTrainQuestionRepository userTrainQuestionRepository;
    @Autowired
    UserSubjectRepository userSubjectRepository;


    //生成组卷参数
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
//            List<UserTrainQuestion> userTrainQuestions = userTrainQuestionRepository.getUserTrainQuestion(id, user_id);
//            for (UserTrainQuestion userTrainQuestion : userTrainQuestions) {
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
            chapter_ratio[i] = (chapter_ratio[i] / sum) * 0.4 + (double)origin_chapter_count.get(i) * 0.6;
        }

        return chapter_ratio;
    }

}

