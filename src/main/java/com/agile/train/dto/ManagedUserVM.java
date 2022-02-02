package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * @author Mengting Lu
 * @date 2022/2/2 19:35
 */
@ApiModel(value = "登录需要的参数")
@NoArgsConstructor
@Data
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    @ApiModelProperty(value = "登录密码", required = true)
    private String password;

    @ApiModelProperty(value = "用户角色，分为ROLE_ADMIN, ROLE_STUDENT, ROLE_TEACHER",required = true)
    private String role;

    @Override
    public String toString() {
        return "ManagedUserVM{" +
                "} " + super.toString();
    }
}
