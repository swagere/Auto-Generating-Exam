package com.group.auto_generating_exam.service.Impl;

import com.group.auto_generating_exam.dao.TestQuestionRepository;
import com.group.auto_generating_exam.model.TestQuestion;
import com.group.auto_generating_exam.service.GenerateTrainService;
import com.group.auto_generating_exam.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 *
 * @Author KVE
 */

@Slf4j
@Service
public class GenerateTrainServiceImpl implements GenerateTrainService {
    @Autowired
    TestQuestionRepository testQuestionRepository;

    //组卷
    @Override
    public void generateTrain() {
        int maxNumber = 10000;  //题库最大容量
        int questionNumber = 0; //题库实际容量
        List<TestQuestion> questions = new ArrayList<TestQuestion>(); //试题数组

        int maxQuestionID = 0;

        int availableClusterNumber = 0;  //试题类别数目
        TestQuestion[] availableCluster = new TestQuestion[maxNumber]; //试题类
        int[] availableKindRange = new int[20]; //题型范围

        Date date=new Date();
        Random myRand = new Random(5 * (int) TimeUtils.getSecondTimestamp(date));

        //组卷参数设置
//        SetPaperAttribute(score, diff, kind, hard, chap, impo);

        //初始化questions
        questions = getDatabaseForTest(questions, myRand);

//        questions = PreOperation(questionNumber, questions); // 预处理数据：将试题聚类，并且得到每个类的信息



    }

    //--数据库及初始化操作----------------------------------------------------------
    //初始化questions[]
    public List<TestQuestion> getDatabaseForTest(List<TestQuestion> questions, Random myRand) {
        int questionNumber = 5000;
        for (int i = 0; i < questionNumber; i++) {
            TestQuestion question = new TestQuestion();
            question.setId(i);

            question.setKind(myRand.nextInt(10));
            question.setHard(Math.abs(myRand.nextInt(10)/10.0 - 0.01));
            question.setDiff(Math.abs(myRand.nextInt(10)/10.0 - 0.01));
            question.setScore(myRand.nextInt(19) + 1);
            question.setChapter(myRand.nextInt(20));
            question.setImportance(myRand.nextInt(3));

            questions.add(question);
        }
        return questions;
    }

    //从数据库中得到最大的题目号（设定：题目不能删除）
    @Override
    public int GetMaxTestQuestion() {
        return testQuestionRepository.getMaxTestQuestionId();
    }


    //--遗传算法预处理-------------------------------------------------------
    // 排序 具体的试题
    public List<TestQuestion> SortQuestionByAttribute(int questionNumber, List<TestQuestion> questions){
        for (int i = 0; i < questionNumber; i++) {
            for (int j = 0; j < questionNumber - i - 1; j++) {
                if (questions.get(j).Compare(questions.get(j + 1)) == 2) {
                    TestQuestion t = questions.get(j);
                    questions.set(j, questions.get(j + 1));
                    questions.set(j + 1, t);
                }
            }
        }
        return questions;
    }

    // 试题预操作
//    public void PreOperation(int questionNumber, List<TestQuestion> questions, ) {
//        SortQuestionByAttribute(questionNumber, questions);
//
//        // 初始化
//        for (int i = 0; i < 20; i++) {
//            availableKindRange[i] = 0;
//        }
//
//        availableClusterNumber = 0;
//
//
//        int firstTime = 0;
//
//        for (int i = 0; i < questionNumber; i++) {
//            if (firstTime == 0) {
//                // 如果是第一个 则初始化为一个类
//                availableCluster[availableClusterNumber] = questions[i];
//                availableCluster[availableClusterNumber].start = i;
//                availableCluster[availableClusterNumber].count = 1;
//
//                firstTime = 1;
//            }
//            else {
//                //比较当前的题是不是属于当前类
//                if (availableCluster[availableClusterNumber].Compare(questions[i]) == 0) {
//                    //如果是，则更新当前类
////                        TestQuestion q = availableCluster[availableClusterNumber];
//                    availableCluster[availableClusterNumber].count++;
//                }
//                else {
//                    // 如果不是，则添加新类
//                    availableClusterNumber++;
//                    availableCluster[availableClusterNumber] = questions[i];
//
//                    availableCluster[availableClusterNumber].start = i;
//                    availableCluster[availableClusterNumber].count = 1;
//                }
//            }
//
//            int nKind = availableCluster[availableClusterNumber].kind;
//            availableKindRange[nKind + 1] = availableClusterNumber;
//            for (int n = nKind + 1; n < 20; n++) {
//                availableKindRange[n] = availableKindRange[nKind + 1];
//            }
//        }
//    }

}

