package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Query("select s.begin_time from Exam s where s.exam_id = ?1")
    Long getBeginTimeByExamId(Integer exam_id);

    @Query("select s.last_time from Exam s where s.exam_id = ?1")
    Long getLastTimeByExamId(Integer exam_id);

    @Query("select s.exam_id from Exam s where s.exam_id = ?1")
    Integer isExamExist(Integer exam_id);

    @Query("select s.sub_id from Exam s where s.exam_id = ?1")
    String getSubIdByExamId(Integer exam_id);
}
