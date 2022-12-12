package com.example.app.dto;

import com.example.app.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateEmployeeResponse extends DemoResponse {
   private Employee employee;
   public UpdateEmployeeResponse(Employee employee, RequestInfo requestInfo) {
      super(requestInfo, 200);
      this.employee = employee;
   }
}
