package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exam_question")
@IdClass(ExamQuestionPK.class)
public class ExamQuestion implements Serializable {
    @Id
    @Column
    private Integer exam_id;

    @Id
    @Column
    private Integer question_id;

    @Column
    private Integer score;
}
