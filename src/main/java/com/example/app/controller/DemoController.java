package com.example.app.controller;

import com.example.app.dao.EmployeeDao;
import com.example.app.dto.CreateEmployeeRequest;
import com.example.app.dto.CreateEmployeeResponse;
import com.example.app.dto.DeleteEmployeeResponse;
import com.example.app.dto.DemoErrorResponse;
import com.example.app.dto.DemoResponse;
import com.example.app.dto.GetEmployeeResponse;
import com.example.app.dto.RequestInfo;
import com.example.app.dto.UpdateEmployeeResponse;
import com.example.app.dto.UpdateEmployeeRequest;
import com.example.app.exception.InvalidRequestException;
import com.example.app.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employees")
@Slf4j
public class DemoController {
	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	EmployeeDao employeeDao;

	public DemoController() {
		log.info("Creating DemoController");
	}

	@GetMapping("{employeeId}")
	public ResponseEntity<DemoResponse> getEmployee(@PathVariable(value = "employeeId") String employeeId) {
		RequestInfo requestInfo = new RequestInfo();
		DemoResponse response;
		try {
			Employee employee = employeeDao.getEmployee(employeeId);
			response = new GetEmployeeResponse(employee, requestInfo);
		} catch (InvalidRequestException e) {
			log.info("InvalidRequestException for getEmployee, employeeId: {}, requestId: {}",
					employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(422, "Invalid request: " + e.getMessage(), requestInfo);
		} catch (EntityNotFoundException e) {
			log.info("EntityNotFound for getEmployee, employeeId: {}, requestId: {}", employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(400, "Employee not found", requestInfo);
		} catch (Exception e) {
			log.error("An error occurred for getEmployee, employeeId: {}, requestId: {}, error: {}", employeeId, requestInfo.getRequestId(), e.getMessage(), e);
			response = new DemoErrorResponse(500, "An error occurred processing this request", requestInfo);
		}
		return ResponseEntity.status(response.getStatus()).body(response);

	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DemoResponse> createEmployee(@RequestBody CreateEmployeeRequest request) {
		RequestInfo requestInfo = new RequestInfo();
		DemoResponse response;
		try {
			Employee employee = employeeDao.createEmployee(request);
			log.info("Processed update request for employeeId: {}", employee.getEmployeeId());
			response = new CreateEmployeeResponse(employee, requestInfo);
		} catch (InvalidRequestException e) {
			log.info("Invalid request for createEmployee, firstName: {}, lastName: {},  requestId: {}",
					request.getFirstName(), request.getLastName(), requestInfo.getRequestId());
			response = new DemoErrorResponse(422, "Invalid request: " + e.getMessage(), requestInfo);
		} catch (Exception e) {
			log.error("An error occurred for getEmployee, requestId: {}, message: {}",
					requestInfo.getRequestId(), e.getMessage(), e);
			response = new DemoErrorResponse(500, "Oops, something went wrong!", requestInfo);
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@PutMapping(path = "{employeeId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DemoResponse> updateEmployee(@PathVariable String employeeId,
													   @RequestBody UpdateEmployeeRequest request) {
		RequestInfo requestInfo = new RequestInfo();
		DemoResponse response;
		try {
			Employee employee = employeeDao.updateEmployee(request);
			log.info("Processed update request for employeeId: {}", request.getEmployeeId());
			response = new UpdateEmployeeResponse(employee, requestInfo);
		} catch (InvalidRequestException e) {
			log.info("Invalid request for updateEmployee, employeeId: {}, requestId: {}", employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(422, "Invalid request: " + e.getMessage(), requestInfo);
		} catch (EntityNotFoundException e) {
			log.info("EntityNotFound for getEmployee, employeeId: {}, requestId: {}", employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(400, "Employee not found", requestInfo);
		} catch (Exception e) {
			log.error("An error occurred for getEmployee, employeeId: {}, requestId: {}, message: {}", employeeId,
					requestInfo.getRequestId(), e.getMessage(), e);
			response = new DemoErrorResponse(500, "Oops, something went wrong!", requestInfo);
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}

	@DeleteMapping(path = "{employeeId}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DemoResponse> deleteEmployee(@PathVariable String employeeId) {
		RequestInfo requestInfo = new RequestInfo();
		DemoResponse response;
		try {
			employeeDao.deleteEmployee(employeeId);
			log.info("Processed delete request for employeeId: {}", employeeId);
			response = new DeleteEmployeeResponse(requestInfo);
		} catch (InvalidRequestException e) {
			log.info("Invalid request for deleteEmployee, employeeId: {}, requestId: {}", employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(422, "Invalid request: " + e.getMessage(), requestInfo);
		} catch (EntityNotFoundException e) {
			log.info("EntityNotFound for getEmployee, employeeId: {}, requestId: {}", employeeId, requestInfo.getRequestId());
			response = new DemoErrorResponse(400, "Employee not found", requestInfo);
		} catch (Exception e) {
			log.error("An error occurred for getEmployee, employeeId: {}, requestId: {}, message: {}", employeeId,
					requestInfo.getRequestId(), e.getMessage(), e);
			response = new DemoErrorResponse(500, "Oops, something went wrong!", requestInfo);
		}
		return ResponseEntity.status(response.getStatus()).body(response);
	}

}
