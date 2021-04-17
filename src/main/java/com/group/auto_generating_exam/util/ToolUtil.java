package com.group.auto_generating_exam.util;

import com.group.auto_generating_exam.model.Question;
import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.ArrayList;

/**
 *
 * @Author KVE
 */

public class ToolUtil {

    //list转string
    public static String List2String(ArrayList<String> list){
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<list.size();++i){
            try{
                jsonArray.put(list.get(i));
            }catch (Exception e){
                //这里处理异常
                break;
            }
        }
        return jsonArray.toString();
    }

    //string转list
    public static ArrayList<String> String2List(String s){
        ArrayList<String> list = new ArrayList<>();
        try{
            JSONArray jsonArray = new JSONArray(s);
            for(int i=0;i<jsonArray.length();++i){
                list.add(jsonArray.getString(i));
            }
        }catch (Exception e){
            //这里处理异常
        }
        return list;
    }

    //string转Question.Type
    public static Question.QuestionType String2QuestionType(String s) {
        switch (s) {
            case "Single":
                return Question.QuestionType.Single;
            case "Discussion":
                return Question.QuestionType.Discussion;
            case "Judge":
                return Question.QuestionType.Judge;
            case "Normal_Program":
                return Question.QuestionType.Normal_Program;
        }
        return Question.QuestionType.SpecialJudge_Program;
    }
}
