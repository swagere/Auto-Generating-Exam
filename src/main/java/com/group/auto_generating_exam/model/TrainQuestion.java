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
@Table(name = "train_question")
@IdClass(TrainQuestionPK.class)
public class TrainQuestion implements Serializable {
    @Id
    @Column
    private Integer train_id;

    @Id
    @Column
    private Integer question_id;

    @Column
    private String answer;

    @Column
    private Integer is_right;

    @Column
    private Integer is_commit;

    @Column
    private Integer score;
}
