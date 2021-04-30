package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.gene.GeneOP;
import com.group.auto_generating_exam.config.gene.GeneOP_Origin;
import com.group.auto_generating_exam.dao.TestQuestionRepository;
import com.group.auto_generating_exam.model.Subject;
import com.group.auto_generating_exam.model.TestQuestion;
import com.group.auto_generating_exam.service.SubjectService;
import com.group.auto_generating_exam.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

@Controller
@Slf4j
@RequestMapping("/train")
public class TrainController {

    @Autowired
    GeneOP geneOP;
    @Autowired
    TestQuestionRepository testQuestionRepository;


    /**
     * 组卷
     * @return
     */
    @RequestMapping("/generateTest")
    public @ResponseBody AjaxResponse generateTest(@RequestBody String str, HttpServletRequest httpServletRequest) {
//        BasicGene.IntelligentTestSystem intelligentTestSystem = new BasicGene.IntelligentTestSystem();
//        intelligentTestSystem.Initial();
//
//        for (int epoch = 0; epoch < 1000; epoch++) {
//            intelligentTestSystem.CalculateFitness();
//            intelligentTestSystem.Sort();
//            intelligentTestSystem.Generate();
//        }

        int score = Integer.parseInt(JSON.parseObject(str).get("score").toString());
        double diff = Double.parseDouble(JSON.parseObject(str).get("diff").toString());

        List kind_origin = ToolUtil.String2List(JSON.parseObject(str).get("kind").toString());
        List hard_origin = ToolUtil.String2List(JSON.parseObject(str).get("hard").toString());
        List chap_origin = ToolUtil.String2List(JSON.parseObject(str).get("chapter").toString());
        List impo_origin = ToolUtil.String2List(JSON.parseObject(str).get("importance").toString());


        //设置出题初始参数

        int[] kind = new int[50];
        for (int i = 0; i < kind_origin.size(); i++) {
            kind[i] = Integer.parseInt((String) kind_origin.get(i));
        }

        int[] hard = new int[50];
        for (int i = 0; i < hard_origin.size(); i++) {
            hard[i] = Integer.parseInt((String) hard_origin.get(i));
        }

        int[] chap = new int[50];
        for (int i = 0; i < chap_origin.size(); i++) {
            chap[i] = Integer.parseInt((String) chap_origin.get(i));
        }

        int[] impo = new int[50];
        for (int i = 0; i < impo_origin.size(); i++) {
            impo[i] = Integer.parseInt((String) impo_origin.get(i));
        }

//        int[] kind = {10,10,10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] hard = {20,20,20,30,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] chap = {20,20,20,20,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
//        int[] impo = {30,50,10,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


//        score, diff, kind, hard, chap, impo
        int[] questions = geneOP.generateTest(score, diff, kind, hard, chap, impo);


        //处理结果
        Map res = new HashMap();
        List r_0 = new ArrayList();
        List r_1 = new ArrayList();
        List r_2 = new ArrayList();
        List r_3 = new ArrayList();
        List r_4 = new ArrayList();

        for (int i = 0; i < questions.length; i++) {
            int id = questions[i];
            if (id != 0) {
                Integer k = testQuestionRepository.getKindById(id);
                if (k.equals(0)) {
                    r_0.add(id);
                }
                else if (k.equals(1)) {
                    r_1.add(i);
                }
                else if (k.equals(2)) {
                    r_2.add(id);
                }
                else if (k.equals(3)) {
                    r_3.add(id);
                }
                else if (k.equals(4)) {
                    r_4.add(id);
                }
            }
        }
        res.put("0",r_0);
        res.put("1",r_1);
        res.put("2",r_2);
        res.put("3",r_3);
        res.put("4",r_4);

//        geneOP.generateQuestion();
        return AjaxResponse.success(res);
    }

}
