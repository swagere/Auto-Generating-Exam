package com.group.auto_generating_exam.service;

import com.alibaba.fastjson.JSONObject;
import com.group.auto_generating_exam.model.GetQuestion;
import com.group.auto_generating_exam.model.JudgeResult;

import java.util.ArrayList;

public interface JudgeService {

    JSONObject judge(String src, String language, Integer testCaseId);

    void addTestCase(GetQuestion getQuestion);

    //Future<String> writeFile(Long question_id, int type, GetQuestion getQuestion) throws InterruptedException;

    void saveProgramQuestionFile(Integer question_id, int type, GetQuestion getQuestion);

//    void addToDocker(String path, Long question_id, ArrayList<String> fileNames);

    void deleteFile(Integer question_id);

    String doPost(String URL, String jsonStr);

    String transformToMd5(String output);

    JudgeResult transformToExamResult(JSONObject json, Integer user_id, String code, String language, Integer question_id, Integer exam_id);

    ArrayList<String> getFileNames(Integer question_id);

    String getJudgeResult(int result);

    JudgeResult transformToTrainResult(JSONObject json, Integer user_id, String code, String language, Integer question_id, Integer exam_id);

}
