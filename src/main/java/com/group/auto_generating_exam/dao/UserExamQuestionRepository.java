package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamQuestionRepository extends JpaRepository<UserExamQuestion, UserExamQuestionPK> {

    @Query("select s from UserExamQuestion s where s.exam_id = ?1 and s.user_id = ?2")
    List<UserExamQuestion> getUserExamQuestion(Integer exam_id, Integer user_id);
}
