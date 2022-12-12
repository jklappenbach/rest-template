package com.example.app.dto;

import com.example.app.model.Employee;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetEmployeeResponse extends DemoResponse {
    private Employee employee;
    public GetEmployeeResponse(Employee employee, RequestInfo requestInfo) throws EntityNotFoundException {
        super(requestInfo, 200);
        this.employee = employee;
    }
}
