package com.agile.train.dto;

import com.agile.train.constant.Constants;
import com.agile.train.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author wqy
 * @date 2022/3/28 22:38
 */
@ApiModel(value = "用户进度信息封装")
@Data
public class UserProgressDTO {

    private String id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @ApiModelProperty(value = "用户名", required = true)
    private String login;

    @Email
    @Size(min = 5, max = 254)
    @ApiModelProperty(value = "邮件", required = true)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    @Size(min = 2, max = 6)
    private String langKey;

    private String createdDate;

    private String lastModifiedDate;

    private String authorities;

    private Double progress;

    public UserProgressDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserProgressDTO(UserDTO user, Double progress) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdDate = user.getCreatedDate().toString();
        this.lastModifiedDate = user.getLastModifiedDate().toString();
        this.authorities = user.getAuthorities();
        this.progress = progress;
    }
}