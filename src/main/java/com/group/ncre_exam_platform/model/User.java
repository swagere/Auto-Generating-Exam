package com.group.ncre_exam_platform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(length = 16, nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer user_id;

    @Column(length = 32, nullable = false)
    @NotBlank(message = "姓名不能为空")
    private String name;

    @Column
    @NotBlank(message = "密码不能为空")
    private String password;

    @Column
    @Email(message = "邮箱格式不正确")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Column
    private int subject_id;

    @Column
    private int code;
}
