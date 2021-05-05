package com.group.auto_generating_exam.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;


import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Login implements Serializable {
    @NotBlank(message = "手机号不能为空")
    private String telephone;

    @NotBlank(message = "密码不能为空")
    private String password;

    private Integer id = null;

    private int authority;
}
