package com.example.app.dto;

import com.example.app.model.Employee;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeleteEmployeeResponse extends DemoResponse {
    public DeleteEmployeeResponse(RequestInfo requestInfo) {
        super(requestInfo, 200);
    }
}
