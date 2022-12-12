package com.example.app.dto;

import com.example.app.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.RequestEntity;

@Data
@NoArgsConstructor
public class CreateEmployeeResponse extends DemoResponse {
    private Employee employee;
    public CreateEmployeeResponse(Employee employee, RequestInfo requestInfo) {
        super(requestInfo, 200);
        this.employee = employee;
    }
}
