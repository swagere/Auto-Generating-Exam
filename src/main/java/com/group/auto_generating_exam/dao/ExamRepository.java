package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Query("select s.begin_time from Exam s where s.exam_id = ?1")
    Long getBeginTimeByExamId(Integer exam_id);

    @Query("select s.last_time from Exam s where s.exam_id = ?1")
    Long getLastTimeByExamId(Integer exam_id);

    @Query("select s.exam_id from Exam s where s.exam_id = ?1")
    Integer isExamExist(Integer exam_id);

    @Query("select s.sub_id from Exam s where s.exam_id = ?1")
    String getSubIdByExamId(Integer exam_id);


    @Transactional
    @Modifying
    @Query("update Exam s set s.last_time = :last_time where s.exam_id = :exam_id")
    void updateLastTime(@Param("last_time") Long last_time, @Param("exam_id") Integer exam_id);

    @Query("select u from Exam u where u.exam_id = ?1")
    Exam getExamByExamId(Integer exam_id);

    @Query("select max(u.exam_id) from Exam u")
    Integer getMaxExamId();

    @Query("select u from Exam u where u.sub_id = ?1 order by u.begin_time desc ")
    List<Exam> getExamBySubId(String sub_id);

    @Query("select u from Exam u where u.exam_id in (:exam_ids) order by u.begin_time desc ")
    List<Exam> getExamsByExamId(List<Integer> exam_ids);

    @Modifying
    @Transactional
    @Query("update Exam u set u.is_judge = :is_judge where u.exam_id = :exam_id")
    void saveIsJudge(@Param("exam_id") Integer exam_id, @Param("is_judge") int is_judge);

    @Query("select u.exam_id from Exam u where u.sub_id = ?1")
    List<Integer> getExamIdBySubId(String sub_id);
}
