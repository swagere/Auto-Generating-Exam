package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.model.Question;
import com.group.auto_generating_exam.model.Subject;
import com.group.auto_generating_exam.model.Train;
import com.group.auto_generating_exam.model.UserSubject;
import com.group.auto_generating_exam.service.ExamService;
import com.group.auto_generating_exam.service.SubjectService;
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
@RequestMapping("/view")
public class ViewController {
    @Autowired
    ExamService examService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    TrainService trainService;

    /**
     * 获得一门课程知识点正确率
     *
     * */
    @PostMapping("/teaSubjectChapter")
    public @ResponseBody AjaxResponse getTeaSubjectExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        String sub_id = JSON.parseObject(str).get("sub_id").toString();
        Map res = new HashMap();
        //获得这门课所有的user_subject
        List chapter = new ArrayList();
        List<UserSubject> userSubjects = subjectService.getUserSubject(sub_id);

        //chapter_id chapter_ratio chapter_name
        List chapter_name = ToolUtil.String2List(subjectService.getChapterNameBySubId(sub_id));
        int[] chapter_count_all = new int[chapter_name.size()];
        int[] chapter_right_count_all = new int[chapter_name.size()];

        for (UserSubject userSubject : userSubjects) {
            List<Integer> chapter_count = ToolUtil.String2ListInt(userSubject.getChapter_count());
            List<Integer> chapter_right_count = ToolUtil.String2ListInt(userSubject.getChapter_right_count());
            for (int i = 0; i < chapter_count.size(); i++) {
                chapter_count_all[i] += chapter_count.get(i);
                chapter_right_count_all[i] += chapter_right_count.get(i);
            }
        }

        double[] chapter_ratio = new double[chapter_name.size()];
        for (int i = 0; i < chapter_name.size(); i++) {
            Map temp = new HashMap();
            chapter_ratio[i] = (double) chapter_right_count_all[i] / (double) chapter_count_all[i];
            temp.put("chapter_id", i);
            temp.put("chapter_name", chapter_name.get(i));
            temp.put("chapter_ratio", chapter_ratio[i]);

            chapter.add(temp); 
        }

        res.put("chapter", chapter);

        //hard_id hard_name hard_ratio
        List<Question> questions = examService.getQuestionBySubId(sub_id);
        int[][] hard = new int[chapter.size()][10];

        for (int i = 0; i < questions.size(); i++) {
            hard[questions.get(i).getChapter()][questions.get(i).HardN()] += questions.get(i).getRight_num(); //正确数
            hard[questions.get(i).getChapter()][questions.get(i).HardN() + 5] += questions.get(i).getSum_num(); //总数
        }

        List res_1 = new ArrayList();
        for (int i = 0; i < chapter.size(); i++) {
            Map temp = new HashMap();
            temp.put("chapter_id", i);
            temp.put("chapter_name", chapter_name.get(i));

            List hard_ratio = new ArrayList();
            for (int j = 0; j < 5; j++) {
                Map r = new HashMap();
                r.put("hard_id", j);
                r.put("hard_name", "难度" + j);
                r.put("hard_ratio", (double)hard[i][j] /(double)hard[i][j + 5]);
                hard_ratio.add(r);
            }
            temp.put("ratio", hard_ratio);
            res_1.add(temp);
        }

        res.put("chapter_hard", res_1);

        //importance_id importance_name importance_ratio
        int[][] importance = new int[chapter.size()][6];

        for (int i = 0; i < questions.size(); i++) {
            importance[questions.get(i).getChapter()][questions.get(i).ImportanceN()] += questions.get(i).getRight_num(); //正确数
            importance[questions.get(i).getChapter()][questions.get(i).ImportanceN() + 3] += questions.get(i).getSum_num(); //总数
        }

        List res_2 = new ArrayList();
        for (int i = 0; i < chapter.size(); i++) {
            Map temp = new HashMap();
            temp.put("chapter_id", i);
            temp.put("chapter_name", chapter_name.get(i));

            List importance_ratio = new ArrayList();
            for (int j = 0; j < 3; j++) {
                Map r = new HashMap();
                r.put("importance_id", j);
                if (j == 0) {
                    r.put("importance_name", "了解");
                }
                else if (j == 1) {
                    r.put("importance_name", "理解");
                }
                else if (j == 2) {
                    r.put("importance_name", "掌握");
                }
                r.put("importance_ratio", (double)importance[i][j] /(double)importance[i][j + 3]);
                importance_ratio.add(r);
            }
            temp.put("ratio", importance_ratio);
            res_2.add(temp);
        }

        res.put("chapter_importance", res_2);
        return AjaxResponse.success(res);
    }



    /**
     * 获得学生一门检测的所有
     *
     * */
    @PostMapping("/stuExam")
    public @ResponseBody AjaxResponse getStuExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer train_id = Integer.parseInt(JSON.parseObject(str).get("train_id").toString());

        Train train = trainService.getTrainByTrainId(train_id);

        Map res = new HashMap();
        res.put("hard_distribute", train.getHard_distribute());
        res.put("chapter_distribute", train.getChapter_distribute());
        res.put("importance_distribute", train.getImportance_distribute());

        return AjaxResponse.success(res);
    }
}
