package com.agile.train.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author Mengting Lu
 * @date 2022/2/8 16:17
 */
@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "train_readed")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="回复状态（已读/未读）类")
public class Readed {

    @Id
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="用户名")
    private String userLoginName;

    @ApiModelProperty(value="讨论id")
    private String questionId;

    @ApiModelProperty(value="阅读状态")
    private boolean reader;

    @ApiModelProperty(value="修改日期")
    private String modifyTime;
}
