package com.agile.train.dto;

import com.agile.train.constant.Constants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author wqy
 * @date 2022/2/4 20:52
 */
@Data
@ApiModel(value="用户信息修改类")
public class UserModifyDTO {
    @NotBlank
    @ApiModelProperty(value="用户id",required = true)
    private String id;

    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "用户名")
    private String login;

    @Email
    @Size(min = 5, max = 254)
    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "密码")
    private String password;


}
