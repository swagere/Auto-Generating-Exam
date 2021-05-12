package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.gene.GeneOP_o;
import com.group.auto_generating_exam.dao.QuestionRepository;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.TrainService;
import com.group.auto_generating_exam.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * @Author KVE
 */

@Controller
@Slf4j
@RequestMapping("/train")
public class TrainController {

    @Autowired
    GeneOP_o geneOP;
    @Autowired
    TrainService trainService;
    @Autowired
    ExamService examService;


    /**
     * 强化训练
     * 组卷
     */
    @RequestMapping("/IntensiveTrain")
    public @ResponseBody AjaxResponse GenerateIntensiveTrain(@RequestBody String str, HttpServletRequest httpServletRequest) {
//        BasicGene.IntelligentTestSystem intelligentTestSystem = new BasicGene.IntelligentTestSystem();
//        intelligentTestSystem.Initial();
//
//        for (int epoch = 0; epoch < 1000; epoch++) {
//            intelligentTestSystem.CalculateFitness();
//            intelligentTestSystem.Sort();
//            intelligentTestSystem.Generate();
//        }

        String sub_id = JSON.parseObject(str).get("sub_id").toString();
        Integer user_id = Integer.parseInt(JSON.parseObject(str).get("user_id").toString());
        Long last_time = Long.parseLong(JSON.parseObject(str).get("train_time").toString());

        //输入参数
        int score = Integer.parseInt(JSON.parseObject(str).get("score").toString());
        List kind_origin = ToolUtil.String2List(JSON.parseObject(str).get("kind").toString());
        double diff = Double.parseDouble(JSON.parseObject(str).get("diff").toString());

        //生成参数
        double[] hard_origin = trainService.getHardRatio(sub_id, user_id);
        double[] chap_origin = trainService.getChapterRatio(sub_id, user_id);
        double[] impo_origin = trainService.getImportanceRatio(sub_id, user_id);


        //设置出题初始参数
        int[] kind = new int[50];
        int count = 0;
        for (int i = 0; i < kind_origin.size(); i++) {
            kind[i] = Integer.parseInt((String) kind_origin.get(i));
            count += kind[i];
        }

        int[] hard = new int[50];
        for (int i = 0; i < hard_origin.length; i++) {
            hard[i] = (int)(hard_origin[i] * 100);
        }

        int[] chap = new int[50];
        for (int i = 0; i < chap_origin.length; i++) {
            chap[i] = (int)(chap_origin[i] * 100);
        }

        int[] impo = new int[50];
        for (int i = 0; i < impo_origin.length; i++) {
            impo[i] = (int)(impo_origin[i] * 100);
        }

//        int[] kinds = {10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] hards = {20,20,20,30,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] chaps = {20,20,20,20,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] impos = {30,50,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


//        score, diff, kind, hard, chap, impo
        Integer train_id = geneOP.generateTest(score, diff, kind, hard, chap, impo);


        //--将user_id train_time(分钟) sub_id保存到train中-------------
        trainService.saveUserIdSubIdAndTrainTimeByTrainId(user_id, sub_id, last_time*1000*60, train_id);

        return AjaxResponse.success(train_id);
    }

    /*
     * 初始化数据库
     */
    @RequestMapping("/GenerateQuestion")
    public @ResponseBody AjaxResponse GenerateQuestion() {
        geneOP.generateQuestion();
        return AjaxResponse.success();
    }

    /**
     * 获得历史组卷信息
     *
     */
    @RequestMapping("/getAllTrain")
    public @ResponseBody AjaxResponse getAllTrain(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer user_id = Integer.parseInt(JSON.parseObject(str).get("user_id").toString());
        return AjaxResponse.success(trainService.getAllTrain(user_id));
    }

    /**
     * 学生开始考试时
     * 获得试卷列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getTrainQuestionList")
    public @ResponseBody AjaxResponse getTrainQuestionList (@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer train_id = Integer.parseInt(JSON.parseObject(str).get("train_id").toString());

        //得到question列表返回给前端
        List<Integer> question_ids = trainService.getQuestionIdByTrainId(train_id);

        //获得传给前端的数据结构
        Map questionList = trainService.getTrainQuestionList(question_ids);

        return AjaxResponse.success(questionList);
    }
}
