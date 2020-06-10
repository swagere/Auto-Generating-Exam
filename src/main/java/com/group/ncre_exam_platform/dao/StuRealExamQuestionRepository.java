package com.group.ncre_exam_platform.dao;

import com.group.ncre_exam_platform.model.RealExamQuestion;
import com.group.ncre_exam_platform.model.RealExamQuestionPK;
import com.group.ncre_exam_platform.model.StuRealExamQuestion;
import com.group.ncre_exam_platform.model.StuRealExamQuestionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface StuRealExamQuestionRepository extends JpaRepository<StuRealExamQuestion, StuRealExamQuestionPK> {
//    @Query("select s from RealExamQuestion s where s.exam_id = ?1")
//    ArrayList<RealExamQuestion> getQuestionListByExam_id(Integer exam_id);
}
