package com.agile.train.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mengting Lu
 * @date 2022/2/6 14:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationDTO {
    private int involvedQuestionNum;

    private int repliedNum;
}
