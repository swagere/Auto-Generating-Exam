package com.group.auto_generating_exam.service;

import com.group.auto_generating_exam.model.TestQuestion;

import java.util.List;
import java.util.Random;

/**
 *
 * @Author KVE
 */

public interface GenerateTrainService {
    void generateTrain();
    List<TestQuestion> getDatabaseForTest(List<TestQuestion> questions, Random myRand);
    int GetMaxTestQuestion();

}
