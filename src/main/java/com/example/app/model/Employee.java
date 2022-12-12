package com.example.app.model;

import com.example.app.dto.CreateEmployeeRequest;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Employee {

    public Employee(CreateEmployeeRequest request) {
        firstName = request.getFirstName();
        lastName = request.getLastName();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String employeeId;
    @NotNull
    @Size(min = 1, max = 64)
    private String firstName;
    @NotNull
    @Size(min = 1, max = 64)
    private String lastName;
}
