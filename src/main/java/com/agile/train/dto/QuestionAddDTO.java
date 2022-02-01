package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:12
 */
@Data
@ApiModel(value = "问题信息封装")
public class QuestionAddDTO {
    @ApiModelProperty(value = "问题标题")
    private String questionTitle;

    @ApiModelProperty(value = "问题内容")
    private String questionContent;
}

