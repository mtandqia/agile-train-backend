package com.agile.train.exception;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

/**
 * @author Mengting Lu
 * @date 2022/2/1 13:50
 */
public class EmailNotFoundException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EmailNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Email address not registered", Status.BAD_REQUEST);
    }
}
