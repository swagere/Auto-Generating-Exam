package com.group.ncre_exam_platform.dao;

import com.group.ncre_exam_platform.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ExamRepository extends JpaRepository<Exam, Integer> {
    @Query("select s from Exam s where s.exam_type = ?1 and s.subject = ?2")
    ArrayList<Exam> getExamByExam_typeAndSubject(Exam.ExamType exam_type, Exam.Subject subject);
}
