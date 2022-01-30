package com.agile.train.exception;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:44
 */
public class CoursewareAlreadyExistException  extends BadRequestAlertException{

    private static final long serialVersionUID = 1L;

    public CoursewareAlreadyExistException() {
        super(ErrorConstants.COURSEWARE_ALREADY_EXIST_TYPE, "Courseware already exists!", "courseware", "coursewareexists");
    }
}
