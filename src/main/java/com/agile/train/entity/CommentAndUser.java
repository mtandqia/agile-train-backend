package com.agile.train.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author wqy
 * @date 2022/2/1 22:08
 */
@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "train_comment")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "论坛评论类")
public class CommentAndUser {

    @Id
    @ApiModelProperty(value="评论id")
    private String id;

    @ApiModelProperty(value="评论父id")
    private String parentId;

    @ApiModelProperty(value="问题id")
    private String questionId;

    @NotBlank
    @ApiModelProperty(value="用户名称")
    private String loginName;

    @ApiModelProperty("被回复用户名称")
    private String replyUserLoginName;

    @NotBlank
    @ApiModelProperty(value="评论内容")
    private String commentContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="评论日期")
    private String addTime;

    @ApiModelProperty(value="子评论列表")
    private List<CommentAndUser> childList;
}