package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public
class GetExamQuestion implements Serializable {
    Integer question_id;
    String question;
    String options;
    Question.QuestionType question_type;
    String tip;
    String input;
    String output;
}