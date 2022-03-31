package com.agile.train.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Mengting Lu
 * @date 2022/2/6 14:19
 */
@Data
@AllArgsConstructor
public class ParticipationDTO {
    private int involvedQuestionNum;

    private int repliedNum;
}
