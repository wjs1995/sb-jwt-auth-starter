package com.xiaoweiyun.auth.vo;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    public static String message(String msg, Integer code) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"msg\": ").append(msg)
                .append(",\"code\": ").append(code).append(" }");
        return sb.toString();
    }
}
