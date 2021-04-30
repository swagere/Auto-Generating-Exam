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
public class TestQuestion implements Serializable {
    public Integer id;

    public Integer kind; //题型

    public Double hard; //难度

    public Double diff; //区分度

    public Integer score; //分数

    public Integer chapter; //所属章节

    public Integer importance; //重要性

    public Integer start; //这一类在试题库中的起始位置（要转换成具体的题目） 【题库按难度、区分度等存放】

    public Integer count; //当前类中一共有多少题目

    public String content;

    public String answer;


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

    public int ScoreN() {
        int[] scoreRange = {1,2,3,4,5,6,8,10,16,50};

        for (int n = 0; n < 10; n++) {
            if (score >= scoreRange[n] && score < scoreRange[n + 1]) {
                return n;
            }
        }

        return 0;
    }

    public int ChapterN() {
        return chapter;
    }

    public int ImportanceN() {
        return importance;
    }

    // 排序
    // 1. 题型；2. 难度；3. 区分度；4. 分数
    public int Compare(TestQuestion b) {
        if (KindN() < b.KindN()) return 1;
        if (KindN() > b.KindN()) return 2;

        if (HardN() < b.HardN()) return 1;
        if (HardN() > b.HardN()) return 2;

        if (DiffN() < b.DiffN()) return 1;
        if (DiffN() > b.DiffN()) return 2;

        if (ScoreN() < b.ScoreN()) return 1;
        if (ScoreN() > b.ScoreN()) return 2;

        if (ChapterN() < b.ChapterN()) return 1;
        if (ChapterN() > b.ChapterN()) return 2;

        if (ImportanceN() < b.ImportanceN()) return 1;
        if (ImportanceN() > b.ImportanceN()) return 2;

        return 0;
    }
}