package com.group.ncre_exam_platform.dao;

import com.group.ncre_exam_platform.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
