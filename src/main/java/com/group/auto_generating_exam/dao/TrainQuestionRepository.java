package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.TrainQuestion;
import com.group.auto_generating_exam.model.TrainQuestionPK;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface TrainQuestionRepository extends JpaRepository<TrainQuestion, TrainQuestionPK> {

    @Query("select s from TrainQuestion s where s.train_id = ?1")
    List<TrainQuestion> getTrainQuestion(Integer train_id);


    @Query("select u.question_id from TrainQuestion u where u.train_id = ?1")
    List<Integer> getQuestionIdByTrainId(Integer train_id);
}
