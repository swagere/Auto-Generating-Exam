package com.group.auto_generating_exam.dao;

import com.group.auto_generating_exam.model.Exam;
import com.group.auto_generating_exam.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface SubjectRepository extends JpaRepository<Subject, String> {
    @Query("select s from Subject s where s.sub_id = ?1")
    Subject getSubjectBySubId(Integer sub_id);

    @Query("select s.user_id from Subject s where s.sub_id = ?1")
    Integer getUserIdBySubId(String sub_id);

    @Query("select s.sub_name from Subject s where s.sub_id = ?1")
    String getSubNameBySubId(String sub_id);
}
