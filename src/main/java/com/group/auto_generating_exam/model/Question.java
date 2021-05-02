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
@Builder
@Entity
@Table(name = "question")
public class Question implements Serializable {
//    public static enum QuestionType {
//        Single,0
//        Judge,1
//        Discussion,2
//        Normal_Program,3
//        SpecialJudge_Program;4
//        }

    @Id
    @Column
    private Integer question_id;

    @Column
    private String content;

    @Column
    private String sub_id;

    @Column
    private Integer user_id;

    @Column
    private String options;

    @Column
    private String answer;

    @Column
    private Integer kind;

    @Column
    private String tip;

    @Column
    private Double hard;

    @Column
    private Double diff;

    @Column
    private Integer importance;

    @Column
    private Integer chapter;

    @Column
    private Integer right_num;

    @Column
    private Integer sum_num;

    // 返回所属题型
    public int KindN() {
        return kind;
    }

    // 返回难度
    public int HardN() {
        //以0.2为间隔计算
        return (int)(hard/0.2);
    }

    public int DiffN() {
        if (diff < 0.2) return 0; //[-1,0.2]都属于第一类
        return (int)(diff / 0.2);
    }

    public int ChapterN() {
        return chapter;
    }

    public int ImportanceN() {
        return importance;
    }

}
