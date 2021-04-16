package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class GetProgram implements Serializable {
    private Integer user_id;
    @NotBlank(message = "代码不为空")
    private String code;
    @NotBlank(message = "语言不为空")
    private String language;
    private Integer question_id;
    private Integer exam_id;
}
