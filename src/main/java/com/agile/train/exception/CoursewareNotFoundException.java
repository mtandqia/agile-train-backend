package com.agile.train.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author wqy
 * @date 2022/1/30 22:16
 */
public class CoursewareNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public CoursewareNotFoundException() {
        super(ErrorConstants.COURSEWARE_NOT_FOUND_TYPE, "Courseware's id does not exist", Status.BAD_REQUEST);
    }
}
