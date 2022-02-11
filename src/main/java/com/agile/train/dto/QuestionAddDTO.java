package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:12
 */
@Data
@ApiModel(value = "问题信息封装")
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAddDTO {
    @ApiModelProperty(value = "问题标题")
    private String questionTitle;

    @ApiModelProperty(value = "问题内容")
    private String questionContent;
}

