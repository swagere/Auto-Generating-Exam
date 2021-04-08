package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
        Judge,
        Discussion,
        Normal_Program,
        SpecialJudge_Program;
        }

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer question_id;

    @Column
    private String question;

    @Column
    private String sub_id;

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
    private String tip;
}
