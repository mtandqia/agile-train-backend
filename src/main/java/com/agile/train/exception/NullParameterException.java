package com.agile.train.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:39
 */
public class NullParameterException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public NullParameterException() {
        super(ErrorConstants.NULL_PARAMETER_TYPE, "Parameter is null", Status.BAD_REQUEST);
    }
    public NullParameterException(String msg) {
        super(ErrorConstants.NULL_PARAMETER_TYPE, msg, Status.BAD_REQUEST);
    }
}
