package com.agile.train.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author Mengting Lu
 * @date 2022/2/6 12:54
 */
@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "train_download")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="课件下载量类")
public class UserDownload {

    @Id
    @ApiModelProperty(value="主键")
    private String id;

    @ApiModelProperty(value="用户id")
    private String userId;

    @ApiModelProperty(value="课件id")
    private String coursewareId;

    @ApiModelProperty(value="下载量")
    private int downloads;

    @ApiModelProperty(value="修改日期")
    private String modifyTime;
}
