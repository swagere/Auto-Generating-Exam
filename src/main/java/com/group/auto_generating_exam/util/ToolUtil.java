package com.group.auto_generating_exam.util;

import org.springframework.boot.configurationprocessor.json.JSONArray;

import java.util.ArrayList;

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
}
