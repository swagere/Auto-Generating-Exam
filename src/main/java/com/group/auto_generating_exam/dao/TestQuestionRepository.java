package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.TestQuestion;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TestQuestionRepository extends JpaRepository<TestQuestion, Integer> {
    @Query("select s from TestQuestion s where s.id = ?1")
    TestQuestion getTestQuestionByQuestionId(Integer id);

    @Query("select max(id) from TestQuestion ")
    Integer getMaxTestQuestionId();

    @Query("select s.kind from TestQuestion s where s.id = ?1")
    Integer getKindById(Integer id);
}
