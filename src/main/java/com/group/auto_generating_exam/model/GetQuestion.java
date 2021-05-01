package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQuestion implements Serializable {


    private Integer question_id;

    private String content;
    private Integer user_id;
    private String sub_id;
    private String options;
    private String answer;
    private Integer exam_id;

    private Integer kind;

    private ArrayList<ToTestCase> test_case;

    private String tip; //提示

    private Double hard;

    private Double diff;

    private Integer importance;

    private Integer chapter;

    private Integer score;
}
