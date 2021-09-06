package com.group.auto_generating_exam.service;

import com.group.auto_generating_exam.model.Train;

import java.util.List;
import java.util.Map;

/**
 *
 * @Author KVE
 */

public interface TrainService {
    double[] getChapterRatio(String sub_id, Integer user_id);
    double[] getHardRatio(String sub_id, Integer user_id);
    double[] getImportanceRatio(String sub_id, Integer user_id);
    void saveUserIdSubIdAndTrainTimeByTrainId (Integer user_id, String sub_id, Long train_time, Integer train_id);
    List<Integer> getQuestionIdByTrainId(Integer train_id);
    List<Train> getAllTrain(Integer user_id);
    Map getTrainQuestionList(List<Integer> questions);
    Boolean isTrainExist(Integer train_id);
    Boolean isStuInTrain(Integer train_id, Integer user_id);
    String trainIsProgressing(Integer train_id);
    Boolean isTrainDoneOverOne(Integer train_id);
    List<Integer> getTrainQuestionIds(Integer train_id);
    Boolean isCommit(Integer train_id);
    void saveIsCommit(Integer is_commit, Integer question_id, Integer train_id);
    Long getLastTime(Integer train_id);
    Long getRestTimeByTrainId(Integer train_id, Long last_time);
    void saveAnswer(String answer, Integer question_id, Integer train_id);
    void judgeGeneralQuestion(Integer train_id);
    Train getTrainByTrainId(Integer train_id);
}
