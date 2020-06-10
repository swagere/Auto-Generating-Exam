package com.group.ncre_exam_platform.controller;

import com.alibaba.fastjson.JSON;
import com.group.ncre_exam_platform.config.exception.AjaxResponse;
import com.group.ncre_exam_platform.model.Exam;
import com.group.ncre_exam_platform.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Controller
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    ExamService examService;

    /**
     * 获得真题集列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getRealExamList")
    public @ResponseBody
    AjaxResponse getRealExamList (@RequestBody String str, HttpServletRequest httpServletRequest) {
        String subject = JSON.parseObject(str).get("subject").toString();
        ArrayList realExamList = examService.getRealExamList(subject);

        return AjaxResponse.success(realExamList);
    }

    /**
     * 用户获得真题
     * 分发试卷到个人表中 向前端传回试卷
     * @param str
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getRealExam")
    public @ResponseBody
    AjaxResponse getRealExam (@RequestBody String str, HttpServletRequest httpServletRequest) {

        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());

        ArrayList realExam = examService.getRealExam(exam_id, user_id);

        return AjaxResponse.success(realExam);
    }



    /**
     * 随机生成模拟题
     * @param str
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getSimulationExam")
    public @ResponseBody
    AjaxResponse getSimulationExam (@RequestBody String str, HttpServletRequest httpServletRequest) {


        return AjaxResponse.success();
    }
}
