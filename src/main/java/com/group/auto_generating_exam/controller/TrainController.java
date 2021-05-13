package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.exception.CustomException;
import com.group.auto_generating_exam.config.exception.CustomExceptionType;
import com.group.auto_generating_exam.config.gene.GeneOP_o;
import com.group.auto_generating_exam.dao.QuestionRepository;
import com.group.auto_generating_exam.model.GetProgram;
import com.group.auto_generating_exam.model.JudgeResult;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.JudgeService;
import com.group.auto_generating_exam.service.TrainService;
import com.group.auto_generating_exam.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Autowired
    JudgeService judgeService;


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
        Long last_time = Long.parseLong(JSON.parseObject(str).get("last_time").toString());

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
     * 学生开始检测时
     * 获得检测试卷列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getTrainQuestionList")
    public @ResponseBody AjaxResponse getTrainQuestionList (@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer train_id = Integer.parseInt(JSON.parseObject(str).get("train_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //考试是否存在
        if (!trainService.isTrainExist(train_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该考试不存在"));
        }

        //用户是否选择这门课程 即用户是否能参与这个考试
        Boolean isStuInExam = trainService.isStuInTrain(train_id, user_id);
        if (!isStuInExam) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户没有选择该课程，不能参与考试"));
        }

        //得到question列表返回给前端
        List<Integer> question_ids = trainService.getQuestionIdByTrainId(train_id);

        //获得传给前端的数据结构
        Map questionList = trainService.getTrainQuestionList(question_ids);

        return AjaxResponse.success(questionList);
    }

    /**
     * 学生交卷
     */
    @PostMapping("/handInTrain")
    public @ResponseBody AjaxResponse handInTrain(@RequestBody String str, HttpServletRequest httpServletRequest) {
        //停止该考生的考试：查找出该考生的所有考题 并设置is_commit字段
        Integer train_id = Integer.valueOf(JSON.parseObject(str).get("train_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id


        //考试是否存在
        if (!trainService.isTrainExist(train_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该检测不存在"));
//            result.put("message","用户没有选择该课程，该考试不存在");
        }
        //用户是否选择这门课程 即用户是否能参与这个考试
        Boolean isStuInTrain = trainService.isStuInTrain(train_id, user_id);
        if (!isStuInTrain) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "用户没有选择该课程，不能参与该检测"));
//            result.put("message","用户没有选择该课程，不能参与考试");
        }
        //检测是否超过考试时间(//判断是否为该考试结束一分钟之后交试卷)/还未开始考试 若超过考试时间则不能考试
        String progress_status = trainService.trainIsProgressing(train_id);
        if (progress_status.equals("will")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该检测还未开始"));
        }
        if (progress_status.equals("over")) {
            //判断是否为该考试结束一分钟之后交卷
            if (trainService.isTrainDoneOverOne(train_id)) {
                return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该检测已结束，不能再提交检测"));
//                result.put("message","该考试已结束，不能再提交试卷");
            }
        }

        //检测是否已经分发给他试卷 若没有分发试卷则判定无法交卷
        List<Integer> questionIds = trainService.getTrainQuestionIds(train_id);
        if (questionIds.isEmpty()) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该学生还未得到检测"));
//            result.put("message","该学生还未得到试卷");
        }


        //检测是否已经交卷 若已交卷则不能再次交卷
        if (trainService.isCommit(train_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已交卷"));
//            result.put("message","用户已交卷");
        }

        //可以停止该考生考试
        for (Integer question_id : questionIds) {
            trainService.saveIsCommit(1, question_id, train_id);
        }

        return AjaxResponse.success();

    }

    /**
     * 学生编程题判题
     * @param getProgram
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/judgeProgram")
    public @ResponseBody
    AjaxResponse judge(@Valid @RequestBody GetProgram getProgram, HttpServletRequest request, HttpServletRequest httpServletRequest) throws Exception {
//        authorityCheckService.checkStudentAuthority(httpServletRequest.getSession().getAttribute("userInfo"));

//        Map userInfo = (Map) request.getSession().getAttribute("userInfo");
//        Integer stu_id = (Integer) userInfo.get("id");

        Integer user_id = getProgram.getUser_id();
        JSONObject json = judgeService.judge(getProgram.getCode(), getProgram.getLanguage(), getProgram.getQuestion_id());
        log.info("判题成功");
        JudgeResult judgeResult = judgeService.transformToTrainResult(json, user_id, getProgram.getCode(), getProgram.getLanguage(), getProgram.getQuestion_id(), getProgram.getExam_id());
        return AjaxResponse.success(judgeResult);
    }
}
