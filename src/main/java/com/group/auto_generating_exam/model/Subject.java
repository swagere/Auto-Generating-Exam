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
    private Integer stu_num;

    @Column
    private String chapter;

    @Column
    private Integer chapter_count;
}
