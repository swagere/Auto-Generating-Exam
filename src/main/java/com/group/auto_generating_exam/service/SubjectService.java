package com.group.auto_generating_exam.service;

import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface SubjectService {
    Boolean isStuInSub(String sub_id, Integer user_id);
    Integer getUserIdBySubId(String sub_id);
    String getSubNameBySubId(String sub_id);
}
