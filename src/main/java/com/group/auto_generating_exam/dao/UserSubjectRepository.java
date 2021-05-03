package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import com.group.auto_generating_exam.model.UserSubject;
import com.group.auto_generating_exam.model.UserSubjectPK;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, UserSubjectPK> {
    @Query("select s from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    UserSubject getOneBySubIdAndUserId(String Sub_id, Integer user_id);

    @Query("select s.chapter_right_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getChapterRightCount(String Sub_id, Integer user_id);

    @Query("select s.chapter_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getChapterCount(String Sub_id, Integer user_id);

    @Query("select s.hard_right_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getHardRightCount(String Sub_id, Integer user_id);

    @Query("select s.hard_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getHardCount(String Sub_id, Integer user_id);

    @Query("select s.importance_right_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getImportanceRightCount(String Sub_id, Integer user_id);

    @Query("select s.importance_count from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    String getImportanceCount(String Sub_id, Integer user_id);

    @Query("select s.sub_id from UserSubject s where s.user_id = ?1")
    List<String> getSubIdByUserId(Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.chapter_right_count = :chapter_right_count, u.chapter_count = :chapter_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveChapter(@Param("chapter_right_count") String chapter_right_count, @Param("chapter_count") String chapter_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.hard_right_count = :hard_right_count, u.hard_count = :hard_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveHard(@Param("hard_right_count") String hard_right_count, @Param("hard_count") String hard_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.importance_right_count = :importance_right_count, u.importance_count = :importance_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveImportance(@Param("importance_right_count") String importance_right_count, @Param("importance_count") String importance_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.chapter_count = :chapter_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveChapterCount(@Param("chapter_count") String chapter_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.hard_count = :hard_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveHardCount(@Param("hard_count") String hard_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Modifying
    @Transactional
    @Query("update UserSubject u set u.importance_count = :importance_count where u.sub_id = :sub_id and u.user_id = :user_id")
    void saveImportanceCount(@Param("importance_count") String importance_count, @Param("sub_id") String sub_id, @Param("user_id") Integer user_id);

    @Query("select u from UserSubject u where u.sub_id = ?1")
    List<UserSubject> getUserSubjectBySubId(String sub_id);
}
