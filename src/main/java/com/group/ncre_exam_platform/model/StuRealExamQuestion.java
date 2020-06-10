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
@Table(name = "stu_real_exam_question")
@IdClass(StuRealExamQuestionPK.class)
public class StuRealExamQuestion  implements Serializable {
    @Id
    @Column(length = 32,nullable = false)
    @NotNull(message = "exam_id 不为空")
    private Integer exam_id;

    @Id
    @Column(length = 32,nullable = false)
    @NotNull(message = "question_id 不为空")
    private Integer question_id;

    @Id
    @Column(length = 32,nullable = false)
    @NotNull(message = "user_id 不为空")
    private Integer user_id;

    private String answer;
    private Integer grade;
    private boolean is_right;
    private String collection;
    private String tag;

    @Column
    @Enumerated(EnumType.STRING)
    private Exam.Subject subject = null;
}
