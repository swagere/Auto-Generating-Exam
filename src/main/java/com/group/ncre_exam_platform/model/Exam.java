package com.group.ncre_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "exam")
public class Exam implements Serializable {
    public static enum ExamType {
        REAL,
        SIMULATION,
        ENHANCED,
        POINT,
    }
    public static enum Subject {
        MSOffice,
        Web,
        C,
        Cpp,
        Java,
        VB,
        Python,
        MySQL,
        Access,
    }
    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer exam_id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Subject subject = null;

    @Column
    private Integer year;

    @Column
    private Integer month;

    @Column
    @Enumerated(EnumType.STRING)
    private ExamType exam_type  = null;

    @Column
    private Long begin_time;//时间戳
}
