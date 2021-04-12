package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.config.gene.BasicGene;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.exception.CustomException;
import com.group.auto_generating_exam.config.exception.CustomExceptionType;
import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @Author KVE
 */

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    ExamService examService;
    @Autowired
    SubjectService subjectService;

    /**
     * 获得试卷列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getQuestionList")
    public @ResponseBody
    AjaxResponse getQuestionList (@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //用户是否选择这门课程 即用户是否能参与这个考试
        Boolean isStuInExam = examService.isStuInExam(exam_id, user_id);
        if (!isStuInExam) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户没有选择该课程，不能参与考试"));
        }

        //考试是否存在
        if (!examService.isExamExist(exam_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试不存在"));
        }

        //考试是否正在进行
//        String examIsProgressing = examService.examIsProgressing(exam_id);
//        if (examIsProgressing.equals("will")) {
//            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试还未开始"));
//        }
//        if (examIsProgressing.equals("over")) {
//            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束"));
//        }
        Exam.ProgressStatus progress_status = examService.getExamProgressStatus(exam_id);
        if (progress_status.equals(Exam.ProgressStatus.WILL)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试还未开始"));
        }
        if (progress_status.equals(Exam.ProgressStatus.DONE)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束"));
        }

        //考试是否已交卷
        if (examService.isCommit(exam_id, user_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已交卷"));
        }

        //获取该试卷题目列表
        Map questionList = examService.getExamQuestionList(exam_id, user_id);
        return AjaxResponse.success(questionList);
    }


    /**
     * 组卷
     * @return
     */
    @RequestMapping("/getExam")
    public @ResponseBody AjaxResponse getExam() {
        BasicGene.IntelligentTestSystem intelligentTestSystem = new BasicGene.IntelligentTestSystem();
        intelligentTestSystem.Initial();

        for (int epoch = 0; epoch < 1000; epoch++) {
            intelligentTestSystem.CalculateFitness();
            intelligentTestSystem.Sort();
            intelligentTestSystem.Generate();
        }

        return AjaxResponse.success();
    }


    /**
     * 更改考试时间
     * 若考试时间改变，则通知前端新的持续时间
     */
    @RequestMapping("/changeExamTime")
    public @ResponseBody AjaxResponse changeExamTime(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Long last_time = Long.valueOf(JSON.parseObject(str).get("last_time").toString());
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //判断是否是老师

        //判断该老师是否是该考试发起者
        String sub_id = examService.getSubIdByExamId(exam_id);
        Integer tea_id = subjectService.getUserIdBySubId(sub_id);
        if (!user_id.equals(tea_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权更改此考试时间"));
        }

        //判断该考试是否已经结束 若结束则无法修改
        Exam.ProgressStatus progress_status = examService.getExamProgressStatus(exam_id);
        if (progress_status.equals(Exam.ProgressStatus.DONE)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束，无法修改考试时间"));
        }

        //修改考试时间
        examService.saveLastTime(last_time, exam_id);

        //通知前端？

        return AjaxResponse.success();
    }


    /**
     * 若考试提前结束，则通知前端
     */
    @RequestMapping("/endExam")
    public @ResponseBody AjaxResponse endExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //判断是否是老师

        //判断该老师是否是该考试发起者
        String sub_id = examService.getSubIdByExamId(exam_id);
        Integer tea_id = subjectService.getUserIdBySubId(sub_id);
        if (!user_id.equals(tea_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权更改此考试时间"));
        }

        //判断该考试是否已经结束 若结束则无法修改
        Exam.ProgressStatus progress_status = examService.getExamProgressStatus(exam_id);
        if (progress_status.equals(Exam.ProgressStatus.DONE)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束，无法修改考试时间"));
        }

        //结束考试
        examService.endExam(exam_id);

        //通知前端？

        return AjaxResponse.success();
    }

}
