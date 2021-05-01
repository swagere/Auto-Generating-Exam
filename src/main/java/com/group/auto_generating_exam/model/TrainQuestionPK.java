package com.group.auto_generating_exam.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TrainQuestionPK implements Serializable {
    private Integer train_id;
    private Integer question_id;
}
