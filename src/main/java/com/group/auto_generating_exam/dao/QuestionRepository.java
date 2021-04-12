package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("select s from Question s where s.question_id = ?1")
    Question getQuestionByQuestionId(Integer question_id);

    @Query("select max(question_id) from Question ")
    Integer getMaxQuestionId();
}
