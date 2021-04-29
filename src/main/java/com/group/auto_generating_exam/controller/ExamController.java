package com.group.auto_generating_exam.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.group.auto_generating_exam.config.exception.AjaxResponse;
import com.group.auto_generating_exam.config.exception.CustomException;
import com.group.auto_generating_exam.config.exception.CustomExceptionType;
import com.group.auto_generating_exam.config.gene.GeneOP;
import com.group.auto_generating_exam.model.*;
import com.group.auto_generating_exam.service.*;
import com.group.auto_generating_exam.util.RedisUtils;
import com.group.auto_generating_exam.util.TimeUtils;
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
import java.util.*;
import java.util.concurrent.Future;

/**
 *
 * @Author KVE
 */

@Controller
@Slf4j
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    ExamService examService;
    @Autowired
    SubjectService subjectService;
    @Autowired
    JudgeService judgeService;
    @Autowired
    UserService userService;
    @Autowired
    GeneOP geneOP;
    @Autowired
    RedisUtils redisUtils;



    /**
     * 学生开始考试时
     * 获得试卷列表
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("/getQuestionList")
    public @ResponseBody
    AjaxResponse getQuestionList (@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //考试是否存在
        if (!examService.isExamExist(exam_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试不存在"));
        }

        //用户是否选择这门课程 即用户是否能参与这个考试
        Boolean isStuInExam = examService.isStuInExam(exam_id, user_id);
        if (!isStuInExam) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户没有选择该课程，不能参与考试"));
        }


        //考试是否正在进行
        String progress_status = examService.examIsProgressing(exam_id);
        if (progress_status.equals("will")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试还未开始"));
        }
        if (progress_status.equals("over")) {
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
    @RequestMapping("/generateTest")
    public @ResponseBody AjaxResponse generateTest() {
//        BasicGene.IntelligentTestSystem intelligentTestSystem = new BasicGene.IntelligentTestSystem();
//        intelligentTestSystem.Initial();
//
//        for (int epoch = 0; epoch < 1000; epoch++) {
//            intelligentTestSystem.CalculateFitness();
//            intelligentTestSystem.Sort();
//            intelligentTestSystem.Generate();
//        }


        //设置出题初始参数
        int score = 100;
        double diff = 0.5;
        int[] kind = {10,10,10,10,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] hard = {20,20,20,30,10,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] chap = {20,20,20,20,20,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] impo = {30,50,10,5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

//        score, diff, kind, hard, chap, impo
        int i = geneOP.generateTest(score, diff, kind, hard, chap, impo);

        return AjaxResponse.success(i);
    }


    /**
     * 老师更改考试时间
     * 若考试时间改变，则通知前端新的持续时间
     */
    @RequestMapping("/changeExamTime")
    public @ResponseBody AjaxResponse changeExamTime(@RequestBody String str, HttpServletRequest httpServletRequest) throws IOException {
        Long last_time = Long.valueOf(JSON.parseObject(str).get("last_time").toString());
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //判断是否是老师

        //考试是否存在
        if (!examService.isExamExist(exam_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试不存在"));
        }

        //判断该老师是否是该考试发起者
        String sub_id = examService.getSubIdByExamId(exam_id);
        Integer tea_id = subjectService.getUserIdBySubId(sub_id);
        if (!user_id.equals(tea_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权更改此考试时间"));
        }

        //判断该考试是否已经结束 若结束则无法修改
        String progress_status = examService.examIsProgressing(exam_id);
        if (progress_status.equals("over")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束，无法修改考试时间"));
        }

        //判断修改时间是否小于已经进行的考试时间
        Long rest_time = examService.getRestTimeByExamId(exam_id, last_time);
        if (rest_time < 0) {
            //如果修改时间小于已经考试的时间
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"不能小于考试已持续的时间"));
        }

        //修改考试时间
        examService.saveLastTime(last_time, exam_id);


        // 如果考试未开始则不用通知前端
        if (rest_time > last_time * 60000) {
            System.out.println("1");
            return AjaxResponse.success();
        }
        else {
            //通知所有在考试的前端
            System.out.println("2");
            WebSocketServer.socketChangExamTime(rest_time);
        }

        return AjaxResponse.success();
    }


    /**
     * 老师提前结束考试
     * 若考试提前结束，则通知前端
     */
    @RequestMapping("/endExam")
    public @ResponseBody AjaxResponse endExam(@RequestBody String str, HttpServletRequest httpServletRequest) throws IOException {
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id

        //判断是否是老师

        //判断该老师是否是该考试发起者
        String sub_id = examService.getSubIdByExamId(exam_id);
        Integer tea_id = subjectService.getUserIdBySubId(sub_id);
        if (!user_id.equals(tea_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权提前结束该考试"));
        }


        //判断该考试是否已经结束 若结束则无法提前终止
        String progress_status = examService.examIsProgressing(exam_id);
        if (progress_status.equals("will")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试还未开始，无法提前结束"));
        }
        if (progress_status.equals("over")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束"));
        }



        //结束考试
        examService.endExam(exam_id);

        //通知所有在考试的前端
        WebSocketServer.socketEndExam(exam_id);

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
        JudgeResult judgeResult = judgeService.transformToResult(json, user_id, getProgram.getCode(), getProgram.getLanguage(), getProgram.getQuestion_id(), getProgram.getExam_id());
        return AjaxResponse.success(judgeResult);
    }

    /**
     * 老师获取考试信息
     */
    @PostMapping("/getExam")
    public @ResponseBody AjaxResponse getExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());

        Exam exam = examService.getExamByExamId(exam_id);
        if (exam == null) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"没有该试卷信息"));
        }

        Map result = JSONObject.parseObject(JSONObject.toJSONString(exam), Map.class);
        result.put("sub_name", subjectService.getSubNameBySubId(exam.getSub_id()));


        return AjaxResponse.success(result);
    }

    /**
     * 学生交卷
     */
    @PostMapping("/handInExam")
    public @ResponseBody AjaxResponse handInExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        //停止该考生的考试：查找出该考生的所有考题 并设置is_commit字段

        Integer exam_id = Integer.valueOf(JSON.parseObject(str).get("exam_id").toString());
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString()); //后期改成从登陆状态中获取用户user_id


        //考试是否存在
        if (!examService.isExamExist(exam_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试不存在"));
//            result.put("message","用户没有选择该课程，该考试不存在");
        }
        //用户是否选择这门课程 即用户是否能参与这个考试
        Boolean isStuInExam = examService.isStuInExam(exam_id, user_id);
        if (!isStuInExam) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户没有选择该课程，不能参与考试"));
//            result.put("message","用户没有选择该课程，不能参与考试");
        }
        //检测是否超过考试时间(//判断是否为该考试结束一分钟之后交试卷)/还未开始考试 若超过考试时间则不能考试
        String progress_status = examService.examIsProgressing(exam_id);
        if (progress_status.equals("will")) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试还未开始"));
        }
        if (progress_status.equals("over")) {
            //判断是否为该考试结束一分钟之后交卷
            if (examService.isExamDoneOverOne(exam_id)) {
                return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该考试已结束，不能再提交试卷"));
//                result.put("message","该考试已结束，不能再提交试卷");
            }
        }


        //检测是否已经分发给他试卷 若没有分发试卷则判定无法交卷
        List<Integer> questionIds = examService.getStuExamQuestionIds(exam_id, user_id);
        if (questionIds.isEmpty()) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该学生还未得到试卷"));
//            result.put("message","该学生还未得到试卷");
        }


        //检测是否已经交卷 若已交卷则不能再次交卷
        if (examService.isCommit(exam_id, user_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"用户已交卷"));
//            result.put("message","用户已交卷");
        }

        //可以停止该考生考试
        for (Integer question_id : questionIds) {
            System.out.println(question_id);
            examService.saveIsCommit(1, question_id, exam_id, user_id);
        }

        return AjaxResponse.success();
    }

    /**
     * 老师添加试卷/修改试卷
     */
    @PostMapping("/saveExam")
    public @ResponseBody AjaxResponse saveExam(@RequestBody GetExam getExam, HttpServletRequest httpServletRequest) {

        //该课程是否存在
        String sub_id = getExam.getSub_id();
        Integer user_id = subjectService.getUserIdBySubId(sub_id);
        if (user_id == null) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该课程不存在"));
        }

        //老师是否能创建这个课程的考试
        if (!user_id.equals(getExam.getUser_id())) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权创建该课程的考试"));
        }

        //存入数据库
        Exam exam = new Exam();
        exam.setExam_name(getExam.getExam_name());
        exam.setBegin_time(getExam.getBegin_time());
        exam.setLast_time(getExam.getLast_time() * 60 * 1000);
        exam.setSub_id(sub_id);
        exam.setIs_judge(0);
        Integer exam_id = examService.saveExam(exam);

        return AjaxResponse.success(exam_id);
    }

    /**
     * 老师添加试卷/修改试题
     */
//    @PostMapping("/saveQuestionToExam")
//        public @ResponseBody AjaxResponse saveQuestionToExam(@RequestBody GetQuestion getQuestion, HttpServletRequest httpServletRequest) {
//
//    }

    /**
     * 获取一个老师的某个科目下的所有试卷
     * @param str
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/getTeaSubjectExam")
    public @ResponseBody AjaxResponse getTeaSubjectExam(@RequestBody String str, HttpServletRequest httpServletRequest) {

        String sub_id = JSON.parseObject(str).get("sub_id").toString();
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());

        Integer tea_id = subjectService.getUserIdBySubId(sub_id);

        //该课程是否存在
        if (tea_id == null) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该课程不存在"));
        }

        //老师是否教这个课程
        if (!user_id.equals(tea_id)) {
            return AjaxResponse.error(new CustomException(CustomExceptionType.USER_INPUT_ERROR,"该老师无权查看该课程的考试"));
        }


        List<Exam> exams_origin = examService.getExamBySubId(sub_id);
        List exams = new ArrayList();
        for (Exam exam_origin : exams_origin) {
            Map exam = new HashMap();
            exam.put("exam_id", exam_origin.getExam_id());
            exam.put("exam_name", exam_origin.getExam_name());
            exam.put("begin_time", exam_origin.getBegin_time());
            exam.put("last_time", exam_origin.getLast_time() / 1000 / 60);

            //exam_status
            String progress_status = examService.examIsProgressing(exam_origin.getExam_id());
            if (progress_status.equals("will")) {
                exam.put("exam_status", 0);
            }
            else if (progress_status.equals("ing")) {
                exam.put("exam_status", 1);
            }
            else if (progress_status.equals("over") &&  exam_origin.getIs_judge().equals(0)) {
                exam.put("exam_status", 2);
            }
            else if (progress_status.equals("over") &&  exam_origin.getIs_judge().equals(1)) {
                exam.put("exam_status", 3);
            }


            exams.add(exam);
        }

        return AjaxResponse.success(exams);
    }

    /**
     * 获取一个学生的所有试卷
     * @param str
     * @param httpServletRequest
     * @return
     * status 0 未开始
     * status 1 正在进行
     * status 2 未评分
     * status 3 已评分
     */
    @PostMapping("/getStuAllExam")
    public @ResponseBody AjaxResponse getStuAllExam(@RequestBody String str, HttpServletRequest httpServletRequest) {
        Integer user_id = Integer.valueOf(JSON.parseObject(str).get("user_id").toString());

        List<Integer> exam_ids = examService.getExamIdsByUserId(user_id);
        if (exam_ids.isEmpty()) {
            return AjaxResponse.success();
        }
        List<Exam> exams_origin = examService.getExamsByExamId(exam_ids);

        List<Map<String, Object>> ret = new ArrayList<>();

        for (Exam exam_origin : exams_origin) {
            Map exam = new HashMap();
            exam.put("exam_id", exam_origin.getExam_id());
            exam.put("exam_name", exam_origin.getExam_name());
            exam.put("begin_time", exam_origin.getBegin_time());
            exam.put("last_time", exam_origin.getLast_time() / 1000 / 60);
            exam.put("co_name", subjectService.getSubNameBySubId(exam_origin.getSub_id()));
            exam.put("tea_name", userService.getUserNameByUserId(user_id));

            //exam_status
            String progress_status = examService.examIsProgressing(exam_origin.getExam_id());
            if (progress_status.equals("will")) {
                exam.put("exam_status", 0);
            }
            else if (progress_status.equals("ing")) {
                exam.put("exam_status", 1);
            }
            else if (progress_status.equals("over") &&  exam_origin.getIs_judge().equals(0)) {
                exam.put("exam_status", 2);
            }
            else if (progress_status.equals("over") &&  exam_origin.getIs_judge().equals(1)) {
                exam.put("exam_status", 3);

                //已评分 返回分数
                exam.put("score", examService.getExamScore(exam_origin.getExam_id(), user_id));
            }



            ret.add(exam);
        }

        return AjaxResponse.success(ret);
    }

    /**
     * 老师出题
     */
    @PostMapping("/saveQuestion")
    public @ResponseBody AjaxResponse getStuAllExam(@Valid @RequestBody GetQuestion getQuestion, HttpServletRequest httpServletRequest) throws Exception {
        //先判断是否为添加题目
        Integer question_id = getQuestion.getQuestion_id();
        Future<String> future = null;
        if (question_id == null) {
            question_id = redisUtils.incr("question_id");   //添加题目 id不存在 就新建一个question_id
            //判断redis的question_id值是否为目前数据库最大
            Integer max = examService.getMaxQuestionId();
            if (max != null && max >= question_id) {
                question_id = max + 1;
                redisUtils.set("question_id", max + 1);
            }

            getQuestion.setQuestion_id(question_id);


            //如果是编程题
            if (getQuestion.getQuestion_type() == (GetQuestion.Type.Normal_Program) || getQuestion.getQuestion_type() == (GetQuestion.Type.SpecialJudge_Program)) {
                //去question类中找到type
                int type = 0; //类型1:normal;类型2：special judge
                if (getQuestion.getQuestion_type() == GetQuestion.Type.Normal_Program) {   //判断编程题目类型
                    type = 1;
                } else {
                    type = 2;
                }
                judgeService.saveProgramQuestionFile(question_id, type, getQuestion);
            }
        }
        else {
            //如果为修改 而且是编程题 删除之前的文件并重新创建
            if (getQuestion.getQuestion_type() == (GetQuestion.Type.Normal_Program) || getQuestion.getQuestion_type() == (GetQuestion.Type.SpecialJudge_Program)) {
                //删除
                judgeService.deleteFile(getQuestion.getQuestion_id());

                //再创建 去question类中找到type
                int type = 0; //类型1:normal;类型2：special judge
                if (getQuestion.getQuestion_type() == GetQuestion.Type.Normal_Program) {   //判断编程题目类型
                    type = 1;
                } else {
                    type = 2;
                }
                judgeService.saveProgramQuestionFile(question_id, type, getQuestion);
            }
        }

        examService.saveQuestion(getQuestion);  //保存到question表

        if (getQuestion.getQuestion_type() == (GetQuestion.Type.Normal_Program) || getQuestion.getQuestion_type() == (GetQuestion.Type.SpecialJudge_Program)) {
            judgeService.addTestCase(getQuestion);   //保存到test_case表
        }
        return AjaxResponse.success(question_id);
    }

    /**
     *教师完成评分
     * @param str
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/completeJudge")
    public @ResponseBody AjaxResponse completeJudge(@RequestBody String str, HttpServletRequest httpServletRequest) {
//        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        Integer exam_id = Integer.parseInt(JSON.parseObject(str).get("exam_id").toString());
        examService.saveIsJudge(exam_id, 1);
        return AjaxResponse.success();
    }

    /**
     * 教师判题提交分数
     * @param req
     * @return
     */
    @PostMapping("/handInScore")
    public @ResponseBody AjaxResponse handInScore(@RequestBody GetHandInScore req, HttpServletRequest httpServletRequest) {
//        authorityCheckService.checkTeacherAuthority(httpServletRequest.getSession().getAttribute("userInfo"));
        try {
            Integer stu_id = req.getStu_id();
            Integer exam_id = req.getExam_id();
            for (UserExamQuestion stuExam: req.getScoreList()) {
                examService.saveUserExamQuestionScore(stuExam.getScore(), stuExam.getQuestion_id(), exam_id, stu_id);
            }

            examService.saveUserExamQuestionIsJudge(exam_id, 1);
            return AjaxResponse.success();
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResponse.error(new CustomException(CustomExceptionType.SYSTEM_ERROR, e.getMessage()));
        }
    }
}
