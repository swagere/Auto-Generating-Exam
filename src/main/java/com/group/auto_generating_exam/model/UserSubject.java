package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_subject")
@IdClass(UserSubjectPK.class)
public class UserSubject implements Serializable {
    @Id
    @Column
    private Integer user_id;

    @Id
    @Column
    private String sub_id;

    @Column
    private String chapter_right_count;

    @Column
    private String chapter_count;

    @Column
    private String hard_right_count;

    @Column
    private String hard_count;

    @Column
    private String importance_right_count;

    @Column
    private String importance_count;
}
