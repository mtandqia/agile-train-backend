package com.agile.train.dto;

import com.agile.train.entity.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wqy
 * @date 2022/2/2 23:26
 */
@Data
@ApiModel(value = "论坛问题信息封装")
public class QuestionDTO {

    @ApiModelProperty(value="问题id")
    private String id;

    @ApiModelProperty(value="用户名称")
    private String loginName;

    @ApiModelProperty(value="问题标题")
    private String questionTitle;

    @ApiModelProperty(value="问题内容")
    private String questionContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="修改日期")
    private String modifyTime;

    @ApiModelProperty(value = "回复量")
    private Integer commentNum;

    public QuestionDTO(Question q, int commentNum) {
        this.id=q.getId();
        this.loginName=q.getLoginName();
        this.questionTitle=q.getQuestionTitle();
        this.questionContent=q.getQuestionContent();
        this.modifyTime =q.getModifyTime();
        this.commentNum=commentNum;
    }
}
