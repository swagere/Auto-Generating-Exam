package com.group.ncre_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "real_exam_question")
@IdClass(RealExamQuestionPK.class)
public class RealExamQuestion  implements Serializable {
    @Id
    @Column(length = 32,nullable = false)
    @NotNull(message = "exam_id 不为空")
    private Integer exam_id;

    @Id
    @Column(length = 32,nullable = false)
    @NotNull(message = "question_id 不为空")
    private Integer question_id;

    private Integer num;
}
