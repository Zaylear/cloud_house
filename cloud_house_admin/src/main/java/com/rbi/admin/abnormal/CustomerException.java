package com.rbi.admin.abnormal;

public class CustomerException extends Exception {

    private static final long serialVersionUID = 1L;

    // 提供无参数的构造方法
    public CustomerException() {
    }

    // 提供一个有参数的构造方法，可自动生成
    public CustomerException(String message) {
        super(message);// 把参数传递给Throwable的带String参数的构造方法
    }
}
