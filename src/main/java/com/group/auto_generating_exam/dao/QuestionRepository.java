package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Question;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("select s from Question s where s.question_id = ?1")
    Question getQuestionByQuestionId(Integer question_id);

    @Query("select max(question_id) from Question ")
    Integer getMaxQuestionId();

    @Query("select s.kind from Question s where s.question_id = ?1")
    Integer getKindById(Integer id);

    @Query("select s.answer from Question s where s.question_id = ?1")
    String findAnswerByQuestionId(Integer question_id);

    @Query("select s.chapter from Question s where s.question_id = ?1")
    Integer getChapterById(Integer id);

    @Query("select s.importance from Question s where s.question_id = ?1")
    Integer getImportanceById(Integer id);

    @Query("select s.hard from Question s where s.question_id = ?1")
    Double getHardById(Integer id);

    @Query("select s.content from Question s where s.question_id = ?1")
    String getContentByQuestionId(Integer question_id);

    @Query("select s.answer from Question s where s.question_id = ?1")
    String getAnswerByQuestionId(Integer question_id);

    @Modifying
    @Transactional
    @Query("update Question u set u.right_num = :right_num, u.sum_num = :sum_num where u.question_id = :question_id")
    void saveRightNumAndSumNum(@Param("right_num") Integer right_num, @Param("sum_num") Integer sum_num, @Param("question_id") Integer question_id);

    @Query("select s.right_num from Question s where s.question_id = ?1")
    Integer getRightNumById(Integer id);

    @Query("select s.sum_num from Question s where s.question_id = ?1")
    Integer getSumNumById(Integer id);

    @Modifying
    @Transactional
    @Query("update Question u set u.sum_num = :sum_num where u.question_id = :question_id")
    void saveSumNum(@Param("sum_num") Integer sum_num, @Param("question_id") Integer question_id);

}
