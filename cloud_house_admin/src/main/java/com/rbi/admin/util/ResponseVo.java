package com.rbi.admin.util;

import java.io.Serializable;


public class ResponseVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private T data;

    public static <T> ResponseVo<T> build(String status, String message, T data) {
        return new ResponseVo<T>().setStatus(status).setMessage(message).setData(data);
    }
    
    public static <E> ResponseVo<E> build(String status, String message) {
        return new ResponseVo<E>().setStatus(status).setMessage(message);
    }

    public ResponseVo() {
    }

    public String getStatus() {
        return status;
    }

    public ResponseVo<T> setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ResponseVo<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResponseVo<T> setData(T data) {
        this.data = data;
        return this;
    }

    @Override
	public String toString() {
		return "{\"status\":\"" + status + "\",\"message\":\"" + message + "\",\"data\":" + data + "}";
	}
}
