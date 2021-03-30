package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import com.group.auto_generating_exam.model.UserSubject;
import com.group.auto_generating_exam.model.UserSubjectPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSubjectRepository extends JpaRepository<UserSubject, UserSubjectPK> {
    @Query("select s from UserSubject s where s.sub_id=?1 and s.user_id=?2")
    UserSubject getOneBySubIdAndUserId(String Sub_id, Integer user_id);
}
