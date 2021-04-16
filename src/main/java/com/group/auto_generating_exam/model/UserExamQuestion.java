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
@Table(name = "userExam_question")
@IdClass(UserExamQuestionPK.class)
public class UserExamQuestion implements Serializable {
    @Id
    @Column
    private Integer user_id;

    @Id
    @Column
    private Integer exam_id;

    @Id
    @Column
    private Integer question_id;

    @Column
    private Integer score;

    @Column
    private String answer;

    @Column
    private Integer is_right;

    @Column
    private Integer is_judge;

    @Column
    private Integer is_commit;
}
