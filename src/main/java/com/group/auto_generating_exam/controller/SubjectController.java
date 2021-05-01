package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.exception.CustomException;
import com.group.auto_generating_exam.config.exception.CustomExceptionType;
import com.group.auto_generating_exam.config.gene.BasicGene;
import com.group.auto_generating_exam.model.*;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.JudgeService;
import com.group.auto_generating_exam.service.SubjectService;
import com.group.auto_generating_exam.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
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
@RequestMapping("/subject")
public class SubjectController {
    @Autowired
    SubjectService subjectService;

    /**
     * 获取一个老师的某个课程
     * @param str
     * @param httpServletRequest
     * @return
     */
//    @PostMapping("/getSubject")
//    public @ResponseBody AjaxResponse getTeaSubjectExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
//
//        String sub_id = JSON.parseObject(str).get("sub_id").toString();
//        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());
//
//        Integer tea_id = subjectService.getUserIdBySubId(sub_id);
//
//        //该课程是否存在
//        if (tea_id == null) {
//            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该课程不存在"));
//        }
//
//        //老师是否教这个课程
//        if (!user_id.equals(tea_id)) {
//            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR, "该老师无权查看该课程的考试"));
//        }
//
//        return AjaxResponse.success();
//    }

    /**
     * 获得一个老师的全部课程
     * @param str
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/getTeaAllSubject")
    public @ResponseBody AjaxResponse getTeaSubjectExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());

        List<Subject> subjects = subjectService.getSubjectByUserId(user_id);

        List<Map> res = new ArrayList<>();

        for (Subject subject : subjects) {
            Map map = new HashMap();
            map.put("sub_id", subject.getSub_id());
            map.put("sub_name", subject.getSub_name());
            map.put("description", subject.getDescription());
            map.put("stu_num", subject.getStu_num());
            res.add(map);
        }
        return AjaxResponse.success(res);
    }

    /**
     * 获得一个学生的全部课程
     * @param str
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/getStuAllSubject")
    public @ResponseBody AjaxResponse getStuSubjectExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());

        List<String> sub_ids = subjectService.getSubIdByUserId(user_id);

        List<Map> res = new ArrayList<>();

        for (String sub_id : sub_ids) {
            Subject subject = subjectService.getSubjectBySubjectId(sub_id);
            Map map = new HashMap();
            map.put("sub_id", subject.getSub_id());
            map.put("sub_name", subject.getSub_name());
            map.put("description", subject.getDescription());
            res.add(map);
        }
        return AjaxResponse.success(res);
    }

}
