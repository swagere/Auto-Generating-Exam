package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import com.group.auto_generating_exam.model.UserTrainQuestion;
import com.group.auto_generating_exam.model.UserTrainQuestionPK;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserTrainQuestionRepository extends JpaRepository<UserTrainQuestion, UserTrainQuestionPK> {

    @Query("select s from UserTrainQuestion s where s.train_id = ?1 and s.user_id = ?2")
    List<UserTrainQuestion> getUserTrainQuestion(Integer train_id, Integer user_id);

}
