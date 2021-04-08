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
@Table(name = "test_case")
public class TestCase implements Serializable {
    @Id
    @Column
    private Integer question_id;

    @Column
    private String input;

    @Column
    private String output;
}
