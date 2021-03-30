package com.group.auto_generating_exam.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExamQuestionPK implements Serializable {
    private Integer exam_id;
    private Integer question_id;
}
