package com.agile.train.dto;

import com.agile.train.entity.CommentAndUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wqy
 * @date 2022/2/2 23:26
 */
@Data
@AllArgsConstructor
@ApiModel(value = "论坛问题和评论类")
public class QuestionAndCommentDTO {

    @ApiModelProperty(value="问题id")
    private String questionId;

    @ApiModelProperty(value="问题标题")
    private String questionTitle;

    @ApiModelProperty(value="问题内容")
    private String questionContent;

    @ApiModelProperty(value="评论列表")
    private List<CommentAndUser> commentList;


}
