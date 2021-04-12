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
@Entity
@Builder
@Table(name = "subject")
public class Subject implements Serializable {
    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String sub_id;

    @Column
    private Integer user_id;

    @Column
    private String sub_name;

    @Column
    private String description;

    @Column
    private Integer stu_num;//时间戳

    @Column
    private String outline;

    @Column
    private String outline_score;

    @Column
    private Integer question_kind_count;

    @Column
    private Integer chapter_count;
}
