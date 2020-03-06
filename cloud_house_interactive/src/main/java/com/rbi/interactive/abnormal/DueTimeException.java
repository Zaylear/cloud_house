package com.rbi.interactive.abnormal;


/**
 * 过期时间不是当月第一天抛出异常
 */
public class DueTimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    // 提供无参数的构造方法
    public DueTimeException() {
    }

    // 提供一个有参数的构造方法，可自动生成
    public DueTimeException(String message) {
        super(message);// 把参数传递给Throwable的带String参数的构造方法
    }
}
