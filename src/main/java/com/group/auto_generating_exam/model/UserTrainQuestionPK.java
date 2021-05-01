package com.group.auto_generating_exam.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTrainQuestionPK implements Serializable {
    private Integer exam_id;
    private Integer question_id;
    private Integer user_id;
}
