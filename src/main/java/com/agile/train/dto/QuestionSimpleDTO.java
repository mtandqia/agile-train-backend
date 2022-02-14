package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mengting Lu
 * @date 2022/2/10 15:00
 */
@Data
@AllArgsConstructor
@ApiModel(value="未读消息问题信息简单封装")
public class QuestionSimpleDTO {
    @ApiModelProperty(value = "问题id")
    private String questionId;

    @ApiModelProperty(value="问题标题")
    private String questionTitle;
}
