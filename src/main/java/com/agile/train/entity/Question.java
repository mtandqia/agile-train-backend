package com.agile.train.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:12
 */
@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "train_question")
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "论坛问题类")
public class Question {

    @Id
    @ApiModelProperty(value="问题id")
    private String id;

    @NotBlank
    @ApiModelProperty(value="用户名称")
    private String loginName;

    @ApiModelProperty(value="问题标题")
    private String questionTitle;

    @NotBlank
    @ApiModelProperty(value="问题内容")
    private String questionContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="提问日期")
    private String addTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="修改日期")
    private String modifyTime;
}
