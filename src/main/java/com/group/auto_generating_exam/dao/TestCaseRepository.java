package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer> {
    @Query("select s from TestCase s where s.question_id = ?1")
    TestCase getTestCaseByQuestionId(Integer question_id);
}
