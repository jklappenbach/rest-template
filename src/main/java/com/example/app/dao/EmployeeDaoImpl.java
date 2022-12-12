package com.example.app.dao;

import com.example.app.controller.DemoController;
import com.example.app.dto.CreateEmployeeRequest;
import com.example.app.dto.RequestInfo;
import com.example.app.dto.UpdateEmployeeRequest;
import com.example.app.exception.InvalidRequestException;
import com.example.app.exception.ServiceUnavailableException;
import com.example.app.model.Employee;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

@Service
@Slf4j
public class EmployeeDaoImpl implements EmployeeDao {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PlatformTransactionManager txManager;

    DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();

    @Override
    public Employee createEmployee(CreateEmployeeRequest request) throws InvalidRequestException, ServiceUnavailableException {
        Employee employee = null;
        TransactionStatus txStatus = null;
        try {
            txStatus = txManager.getTransaction(txDefinition);
            employee = employeeRepository.saveAndFlush(new Employee(request));
            txManager.commit(txStatus);
        } catch (ConstraintViolationException e) {
            if (txStatus != null) {
                txManager.rollback(txStatus);
            }
            throw InvalidRequestException.wrap(e);
        } catch (Exception e) {
            if (txStatus != null) {
                txManager.rollback(txStatus);
            }
            log.error("An error has occurred with createEmployee: {}. requestId: {}", e.getMessage(), RequestInfo.threadLocal.get().getRequestId(), e);
            throw new ServiceUnavailableException(e);
        }
        return employee;
    }

    @Override
    public Employee updateEmployee(UpdateEmployeeRequest request) throws EntityNotFoundException, InvalidRequestException,
            ServiceUnavailableException {
        Optional<Employee> optional = employeeRepository.findById(request.getEmployeeId());
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No employee found for id: " + request.getEmployeeId());
        }
        Employee employee;
        TransactionStatus txStatus = null;
        try {
            employee = optional.get();
            employee.setFirstName(request.getFirstName());
            employee.setLastName(request.getLastName());
            txStatus = txManager.getTransaction(txDefinition);
            employeeRepository.saveAndFlush(employee);
            txManager.commit(txStatus);
        } catch (ConstraintViolationException e) {
            if (txStatus != null) {
                txManager.rollback(txStatus);
            }
            throw InvalidRequestException.wrap(e);
        } catch (Exception e) {
            if (txStatus != null) {
                txManager.rollback(txStatus);
            }
            log.error("An error occurred updating an employee record: {}, requestId: {}", e.getMessage(),
                    RequestInfo.threadLocal.get().getRequestId(),
                    e);
            throw new ServiceUnavailableException("An error occurred processing this request");
        }
        return employee;
    }

    @Override
    public Employee getEmployee(String employeeId) throws ServiceUnavailableException, EntityNotFoundException, InvalidRequestException {
        if (Strings.isEmpty(employeeId)) {
            throw new InvalidRequestException("Invalid employeeId: " + employeeId);
        }
        TransactionStatus txStatus = null;
        Optional<Employee> optional = Optional.empty();
        try {
            txStatus = txManager.getTransaction(txDefinition);
            optional = employeeRepository.findById(employeeId);
            txManager.commit(txStatus);
        } catch (Exception e) {
            if (txStatus != null) {
                txManager.rollback(txStatus);
            }
            log.error("An error occurred updating an employee record: {}, requestId: {}",
                    e.getMessage(),
                    RequestInfo.threadLocal.get().getRequestId(),
                    e);
            throw new ServiceUnavailableException("An error occurred processing this request");
        }
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No employee found for id: " + employeeId);
        }
        return optional.get();
    }

    @Override
    @Transactional
    public void deleteEmployee(String employeeId) throws EntityNotFoundException, InvalidRequestException, ServiceUnavailableException {
        if (Strings.isEmpty(employeeId)) {
            throw new InvalidRequestException("Invalid employeeId: " + employeeId);
        }

        TransactionStatus txStatus;
        Optional<Employee> optional = Optional.empty();
        try {
            txStatus = txManager.getTransaction(txDefinition);
            optional = employeeRepository.findById(employeeId);
        } catch (Exception e) {
            log.error("An error occurred retrieving an employee record: employeeId: {}, requestId: {}, message: {}",
                    employeeId,
                    RequestInfo.threadLocal.get().getRequestId(),
                    e.getMessage(),
                    e);
            throw new ServiceUnavailableException("An error occurred processing this request");
        }

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("No employee found for id: " + employeeId);
        }

        if (txStatus != null) {
            try {
                employeeRepository.delete(optional.get());
                txManager.commit(txStatus);
            } catch (Exception e) {
                txManager.rollback(txStatus);
                log.error("An error occurred deleting an employee record: employeeId: {}, requestId: {}, message: {}",
                        employeeId,
                        RequestInfo.threadLocal.get().getRequestId(),
                        e.getMessage(),
                        e);
                throw new ServiceUnavailableException("An error occurred processing this request");
            }
        }
    }
}
