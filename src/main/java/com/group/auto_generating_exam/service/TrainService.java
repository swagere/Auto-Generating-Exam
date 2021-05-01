package com.group.auto_generating_exam.service;

import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface TrainService {
    double[] getChapterRatio(String sub_id, Integer user_id);
    double[] getHardRatio(String sub_id, Integer user_id);
    double[] getImportanceRatio(String sub_id, Integer user_id);
}
