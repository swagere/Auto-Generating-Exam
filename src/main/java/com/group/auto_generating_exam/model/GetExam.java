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
public class GetExam implements Serializable {
    private Integer user_id;
    private Integer exam_id;
    private String exam_name;
    private String sub_id;
    private Long begin_time;//时间戳
    private Long last_time;//时间戳

}
