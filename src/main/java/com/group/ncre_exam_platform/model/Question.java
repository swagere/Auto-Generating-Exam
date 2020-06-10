package com.group.ncre_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "question")
public class Question implements Serializable {
    public static enum QuestionType {
        Single,
        ProgramCorrection,
        ProgramDesign,
        BasicOperation,
        SimpleApplication,
        ComprehensiveApplication,
        }

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer question_id;

    @Column
    private String question;

    @Column
    @Enumerated(EnumType.STRING)
    private Exam.Subject subject = null;

    @Column
    private String tag;

    @Column
    private Integer level;

    @Column
    private String options;

    @Column
    private String answer;

    @Column
    @Enumerated(EnumType.STRING)
    private QuestionType question_type  = null;

    @Column
    private String analysis;

    @Column
    private Integer score;
}
