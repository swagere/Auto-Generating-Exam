package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.UserExamQuestion;
import com.group.auto_generating_exam.model.UserExamQuestionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserExamQuestionRepository extends JpaRepository<UserExamQuestion, UserExamQuestionPK> {
}
