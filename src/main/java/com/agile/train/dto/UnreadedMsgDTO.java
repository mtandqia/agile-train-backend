package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Mengting Lu
 * @date 2022/2/10 17:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("未读消息封装")
public class UnreadedMsgDTO {
    @ApiModelProperty(value = "未读消息总数")
    private int count;

    @ApiModelProperty(value="未读消息列表")
    private Set<QuestionSimpleDTO> set;
}
