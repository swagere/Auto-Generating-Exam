package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "train")
public class Train implements Serializable {

    @Id
    @Column
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer train_id;

    @Column
    private Integer train_type;

    @Column
    private String train_name;

    @Column
    private Long begin_time;

    @Column
    private Long last_time;

    @Column
    private String sub_id;

    @Column
    private Integer user_id;

    @Column
    private String hard_distribute;

    @Column
    private String chapter_distribute;

    @Column
    private String importance_distribute;

    @Column
    private Double diff;

    @Column
    private Integer score;


}
