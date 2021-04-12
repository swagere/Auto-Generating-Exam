package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Question;
import com.group.auto_generating_exam.model.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Integer> {
    @Query("select s from TestQuestion s where s.id = ?1")
    TestQuestion getTestQuestionByQuestionId(Integer id);

    @Query("select max(id) from TestQuestion ")
    Integer getMaxTestQuestionId();
}
