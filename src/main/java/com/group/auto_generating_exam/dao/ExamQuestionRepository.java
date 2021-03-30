package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.ExamQuestion;
import com.group.auto_generating_exam.model.ExamQuestionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, ExamQuestionPK> {

    @Query("select s.question_id from ExamQuestion s where s.exam_id = ?1")
    List<Integer> getQuestionIdByExamId(Integer exam_id);


}