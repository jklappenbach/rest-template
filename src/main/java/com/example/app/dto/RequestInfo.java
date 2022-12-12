package com.example.app.dto;

import com.example.app.controller.DemoController;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class RequestInfo {
    public static ThreadLocal<RequestInfo> threadLocal = new ThreadLocal<>();

    private Date requestDate = new Date();
    private String requestId = UUID.randomUUID().toString();
    public RequestInfo() {
        threadLocal.set(this);
    }
}
