package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.SubjectRepository;
import com.group.auto_generating_exam.dao.UserRepository;
import com.group.auto_generating_exam.service.TrainService;
import com.group.auto_generating_exam.service.UserService;
import com.group.auto_generating_exam.util.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

@Slf4j
@Service
public class TrainServiceImpl implements TrainService {
    @Autowired
    SubjectRepository subjectRepository;


    //生成组卷参数
    @Override
    public Map generateTrain(String sub_id, Integer user_id) {
        Map res = new HashMap();
        //获取科目知识点
        List<Integer> chapter = ToolUtil.String2ListInt(subjectRepository.getChapterBySubId(sub_id));
        List<Integer> chapter_count = ToolUtil.String2ListInt(subjectRepository.getChapterCountBySubId(sub_id));

        //获取科目试卷id

        //获取userExamQuestion

        //获取trainExamQuestion

        return res;
    }

}

