package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.config.gene.TestQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Integer> {
    @Query("select s from TestQuestion s where s.id = ?1")
    TestQuestion getTestQuestionByQuestionId(Integer id);

    @Query("select max(id) from TestQuestion ")
    Integer getMaxTestQuestionId();

    @Query(value = "select * from TestQuestion",nativeQuery = true)
    List<TestQuestion> findTestQuestion();
}
