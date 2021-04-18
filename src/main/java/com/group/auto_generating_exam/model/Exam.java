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
@Table(name = "exam")
public class Exam implements Serializable {
    public static enum ProgressStatus {
        WILL,
        ING,
        DONE,
    }

    @Id
    @Column
//    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer exam_id;

    @Column
    private String exam_name;

    @Column
    private String sub_id;

    @Column
    private Long begin_time;//时间戳

    @Column
    private Long last_time;//时间戳

    @Column
    private Integer is_judge;

    @Column
    @Enumerated(EnumType.STRING)
    private ProgressStatus progress_status = ProgressStatus.WILL; //状态
}
