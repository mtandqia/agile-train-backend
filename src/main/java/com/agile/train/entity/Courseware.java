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
 * @date 2022/1/30 14:14
 */
@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "train_courseware")
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="课件类")
public class Courseware {

    @Id
    @ApiModelProperty(value="课件id")
    private String id;

    @ApiModelProperty(value="文件路径和名字")
    private String coursewareUrl;

    @ApiModelProperty(value="文件名")
    private String coursewareName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value="新增日期")
    private String addTime;

}

