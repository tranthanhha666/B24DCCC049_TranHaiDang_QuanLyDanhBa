package com.example.myapplication;

/** Tài khoản cố định theo đề thực hành; không phải cơ chế xác thực cho sản phẩm. */
public final class LoginValidator {
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";

    private LoginValidator() {}

    public static boolean matches(String username, String password) {
        return USERNAME.equals(username == null ? null : username.trim())
                && PASSWORD.equals(password);
    }
}
