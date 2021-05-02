package com.group.auto_generating_exam.service;

import com.group.auto_generating_exam.model.Subject;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface SubjectService {
    Boolean isStuInSub(String sub_id, Integer user_id);
    Integer getUserIdBySubId(String sub_id);
    String getSubNameBySubId(String sub_id);
    List<Subject> getSubjectByUserId(Integer user_id);
    List<String> getSubIdByUserId(Integer user_id);
    Subject getSubjectBySubjectId(String sub_id);
    String getChapterNameBySubId(String sub_id);
    String getChapterBySubId(String sub_id);
}
