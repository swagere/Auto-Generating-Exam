package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface UserExamQuestionRepository extends JpaRepository<UserExamQuestion, UserExamQuestionPK> {

    @Query("select s from UserExamQuestion s where s.exam_id = ?1 and s.user_id = ?2")
    List<UserExamQuestion> getUserExamQuestion(Integer exam_id, Integer user_id);

    @Query("select s.question_id from UserExamQuestion s where s.exam_id = ?1 and s.user_id = ?2")
    List<Integer> getUserExamQuestionIds(Integer exam_id, Integer user_id);

    @Query("select s.score from UserExamQuestion s where s.exam_id = ?1 and s.user_id = ?2 and s.question_id = ?3")
    Integer getScoreByIds(Integer exam_id, Integer user_id, Integer question_id);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.score = :score where u.question_id = :question_id and u.exam_id = :exam_id and u.user_id = :user_id")
    void saveScore(@Param("score") Integer score, @Param("question_id") Integer question_id, @Param("exam_id") Integer exam_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.answer = :answer where u.question_id = :question_id and u.exam_id = :exam_id and u.user_id = :user_id")
    void saveAnswer(@Param("answer") String answer, @Param("question_id") Integer question_id, @Param("exam_id") Integer exam_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.answer = :answer,u.score = :score where u.question_id = :question_id and u.exam_id = :exam_id and u.user_id = :user_id")
    void saveAnswerAndScore(@Param("answer") String answer, @Param("score") Integer score, @Param("question_id") Integer question_id, @Param("exam_id") Integer exam_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.is_commit = :is_commit where u.question_id = :question_id and u.exam_id = :exam_id and u.user_id = :user_id")
    void saveIsCommit(@Param("is_commit") Integer is_commit, @Param("question_id") Integer question_id, @Param("exam_id") Integer exam_id, @Param("user_id") Integer user_id);

    @Query("select distinct u.exam_id from UserExamQuestion u where u.user_id = ?1")
    List<Integer> getExamIdsByUserId(Integer user_id);

    @Query("select u.score from UserExamQuestion u where u.exam_id = ?1 and u.user_id = ?2")
    List<Integer> getScoresByExamIdAndUserId(Integer exam_id, Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.is_judge = :is_judge where u.exam_id = :exam_id")
    void saveIsJudge(@Param("exam_id") Integer exam_id, @Param("is_judge") int is_judge);

    @Modifying
    @Transactional
    @Query("update UserExamQuestion u set u.is_right = :is_right where u.question_id = :question_id and u.exam_id = :exam_id and u.user_id = :user_id")
    void saveIsRight(@Param("is_right") Integer is_right, @Param("question_id") Integer question_id, @Param("exam_id") Integer exam_id, @Param("user_id") Integer user_id);

    @Query("select u.user_id from UserExamQuestion u where u.question_id = ?1 and u.exam_id = ?2")
    List<Integer> getUserIdByQuestionIdAndExamId(Integer question_id, Integer exam_id);

    @Query("select u.answer from UserExamQuestion u where u.question_id = ?1 and u.exam_id = ?2 and u.user_id = ?3")
    String getAnswerById(Integer question_id, Integer exam_id, Integer user_id);

}
