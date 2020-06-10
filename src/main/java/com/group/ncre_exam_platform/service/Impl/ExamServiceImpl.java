package com.group.ncre_exam_platform.service.Impl;

import com.group.ncre_exam_platform.dao.ExamRepository;
import com.group.ncre_exam_platform.dao.QuestionRepository;
import com.group.ncre_exam_platform.dao.RealExamQuestionRepository;
import com.group.ncre_exam_platform.dao.StuRealExamQuestionRepository;
import com.group.ncre_exam_platform.model.Exam;
import com.group.ncre_exam_platform.model.Question;
import com.group.ncre_exam_platform.model.RealExamQuestion;
import com.group.ncre_exam_platform.model.StuRealExamQuestion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExamServiceImpl implements com.group.ncre_exam_platform.service.ExamService {
    @Autowired
    ExamRepository examRepository;
    @Autowired
    RealExamQuestionRepository realExamQuestionRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    StuRealExamQuestionRepository stuRealExamQuestionRepository;

    //获取真题集列表
    @Override
    public ArrayList getRealExamList(String s) {
        Exam.Subject subject = String2Subject(s);
        return examRepository.getExamByExam_typeAndSubject(Exam.ExamType.REAL, subject);
    }

    //获取真题套题并分发试卷
    @Override
    public ArrayList getRealExam(Integer exam_id, Integer user_id) {
        //获得题目列表集
        ArrayList<Map> res = new ArrayList<>();
        ArrayList<RealExamQuestion> questionList = realExamQuestionRepository.getQuestionListByExam_id(exam_id);

        for (RealExamQuestion question : questionList) {
            Map<String, Object> map = new HashMap<>();
            map.put("question_id", question.getQuestion_id());
            map.put("num", question.getNum());

            //获取题目到输出结果
            Question question1 = questionRepository.getOne(question.getQuestion_id());
            map.put("question",question1.getQuestion());
            map.put("question_type", question1.getQuestion_type());
            if (question1.getQuestion_type() == Question.QuestionType.Single) {
                map.put("options",question1.getOptions());
            }
            map.put("score", question1.getScore());

            res.add(map);

            //分发试卷到用户
            StuRealExamQuestion stuRealExamQuestion = new StuRealExamQuestion();
            stuRealExamQuestion.setExam_id(exam_id);
            stuRealExamQuestion.setQuestion_id(question.getQuestion_id());
            stuRealExamQuestion.setUser_id(user_id);
            stuRealExamQuestion.setTag(question1.getTag());
            stuRealExamQuestion.setSubject(question1.getSubject());
            //存入数据库
            stuRealExamQuestionRepository.save(stuRealExamQuestion);

        }
        return res;

    }

    //list转string
    @Override
    public String List2String(ArrayList<String> list){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<list.size();++i){
            try{
                jsonArray.put(list.get(i));
            }catch (Exception e){
                //这里处理异常
                break;
            }
        }
        return jsonArray.toString();
    }

    //string转list
    @Override
    public ArrayList<String> String2List(String s){
        ArrayList<String> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for(int i=0;i<jsonArray.length();++i){
                list.add(jsonArray.getString(i));
            }
        }catch (Exception e){
            //这里处理异常
        }
        return list;
    }

    //string转subject
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
