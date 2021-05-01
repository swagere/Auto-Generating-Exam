package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.gene.GeneOP_o;
import com.group.auto_generating_exam.dao.QuestionRepository;
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
    QuestionRepository questionRepository;
    @Autowired
    TrainService trainService;

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
        Long train_time = Long.parseLong(JSON.parseObject(str).get("train_time").toString());

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
        trainService.saveUserIdSubIdAndTrainTimeByTrainId(user_id, sub_id, train_time*1000*60, train_id);

        //得到question列表返回给前端
        List<Integer> question_ids = trainService.getQuestionIdByTrainId(train_id);


        Map res = new HashMap();
        List r_0 = new ArrayList();
        List r_1 = new ArrayList();
        List r_2 = new ArrayList();
        List r_3 = new ArrayList();
        List r_4 = new ArrayList();

        for (Integer question_id : question_ids) {
            Integer k = questionRepository.getKindById(question_id);
            if (k.equals(0)) {
                r_0.add(question_id);
            }
            else if (k.equals(1)) {
                r_1.add(question_id);
            }
            else if (k.equals(2)) {
                r_2.add(question_id);
            }
            else if (k.equals(3)) {
                r_3.add(question_id);
            }
            else if (k.equals(4)) {
                r_4.add(question_id);
            }
        }
        res.put("0",r_0);
        res.put("1",r_1);
        res.put("2",r_2);
        res.put("3",r_3);
        res.put("4",r_4);

        return AjaxResponse.success(res);
    }

    /*
     * 初始化数据库
     */
    @RequestMapping("/GenerateQuestion")
    public @ResponseBody AjaxResponse GenerateQuestion() {
        geneOP.generateQuestion();
        return AjaxResponse.success();
    }

}
