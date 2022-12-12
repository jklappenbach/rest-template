package com.example.app.dao;

import com.example.app.dto.CreateEmployeeRequest;
import com.example.app.dto.UpdateEmployeeRequest;
import com.example.app.exception.InvalidRequestException;
import com.example.app.exception.ServiceUnavailableException;
import com.example.app.model.Employee;
import jakarta.persistence.EntityNotFoundException;

public interface EmployeeDao {
    Employee createEmployee(CreateEmployeeRequest request) throws InvalidRequestException, ServiceUnavailableException;
    Employee updateEmployee(UpdateEmployeeRequest request) throws EntityNotFoundException, InvalidRequestException, ServiceUnavailableException;
    Employee getEmployee(String employeeId) throws EntityNotFoundException, InvalidRequestException, ServiceUnavailableException;
    void deleteEmployee(String employeeId) throws EntityNotFoundException, InvalidRequestException, ServiceUnavailableException;

}
