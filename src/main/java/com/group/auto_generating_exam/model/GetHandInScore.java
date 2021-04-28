package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetHandInScore {
    public Integer stu_id;
    public Integer exam_id;
    List<UserExamQuestion> scoreList;
}
