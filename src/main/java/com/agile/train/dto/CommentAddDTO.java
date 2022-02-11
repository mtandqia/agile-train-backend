package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author wqy
 * @date 2022/2/1 22:06
 */
@Data
@ApiModel(value="评论信息封装")
@NoArgsConstructor
@AllArgsConstructor
public class CommentAddDTO {

    @NotBlank
    @ApiModelProperty(value="评论内容")
    private String commentContent;

    @ApiModelProperty(value="评论父id")
    private String parentId;

    @ApiModelProperty(value="问题id")
    private String questionId;

    @ApiModelProperty("被回复人")
    private String replyUserLoginName;

}
