package com.example.app.dao;

import java.util.List;

import com.example.app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    List<Employee> findByFirstName(String employeeId);
}
