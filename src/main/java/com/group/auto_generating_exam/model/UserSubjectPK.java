package com.group.auto_generating_exam.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserSubjectPK implements Serializable {
    private Integer user_id;
    private String sub_id;
}
