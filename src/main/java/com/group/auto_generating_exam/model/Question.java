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
//    public static enum QuestionType {
//        Single,1
//        Judge,2
//        Discussion,3
//        Normal_Program,4
//        SpecialJudge_Program;5
//        }

    @Id
    @Column
    private Integer question_id;

    @Column
    private String question;

    @Column
    private String sub_id;

    @Column
    private Integer user_id;

    @Column
    private String options;

    @Column
    private String answer;

    @Column
    private Integer kind;

    @Column
    private String tip;

    @Column
    private Double hard;

    @Column
    private Double diff;

    @Column
    private Integer importance;

    @Column
    private Integer chapter;
}
