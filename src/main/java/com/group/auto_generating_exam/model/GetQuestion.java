package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetQuestion implements Serializable {

    public static enum Type {
        Single,
        Judge,
        Discussion,
        Normal_Program,
        SpecialJudge_Program;
    }

    private Integer question_id;

    private String question;
    private String sub_id;
    private String tag;
    private Integer level;
    private String options;
    private String answer;

    @Enumerated(EnumType.STRING)
    private Type question_type = null;

    private ArrayList<ToTestCase> test_case;

    private String tip; //提示
}
