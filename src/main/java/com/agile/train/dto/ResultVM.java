package com.agile.train.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Mengting Lu
 * @date 2022/1/30 14:17
 */
@ApiModel(value = "返回类")
public class ResultVM<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "code")
    private int code = 200;

    @ApiModelProperty(value = "描述")
    private String message = "";

    @ApiModelProperty(value = "是否成功")
    private boolean success = true;

    @ApiModelProperty(value = "对象")
    private T data;

    public ResultVM<T> success() {
        this.success = true;
        return this;
    }

    public ResultVM<T> data(T data) {
        this.data = data;
        return this;
    }

    public ResultVM<T> code(int code) {
        this.code = code;
        return this;
    }

    public ResultVM<T> code(HttpStatus code) {
        this.code = code.value();
        return this;
    }

    public ResultVM<T> fail() {
        this.success = false;
        return this;
    }

    public ResultVM<T> message(String message) {
        this.message = message;
        return this;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public  void setCode(HttpStatus code) {
        this.code = code.value();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}