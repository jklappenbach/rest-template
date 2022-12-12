package com.example.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DemoErrorResponse extends DemoResponse {
    private String message;
    public DemoErrorResponse(int status, String message, RequestInfo requestInfo) {
        super(requestInfo, status);
        this.message = message;
    }
}
