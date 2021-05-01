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
@Table(name = "userTrain_question")
@IdClass(UserTrainQuestion.class)
public class UserTrainQuestion implements Serializable {
    @Id
    @Column
    private Integer user_id;

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
}
