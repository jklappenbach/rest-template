package com.example.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.app.dto.CreateEmployeeRequest;
import com.example.app.dto.CreateEmployeeResponse;
import com.example.app.dto.DemoErrorResponse;
import com.example.app.dto.GetEmployeeResponse;
import com.example.app.dto.UpdateEmployeeRequest;
import com.example.app.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class DemoControllerTests {
	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private MockMvc mockMvc;

	private Employee createEmployee(String firstName, String lastName) throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest(firstName, lastName);
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andReturn();
		CreateEmployeeResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(),
				CreateEmployeeResponse.class);
		return response.getEmployee();
	}

	private Employee getEmployee(String employeeId) throws Exception {
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/" + employeeId)
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk())
				.andReturn();
		GetEmployeeResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(),
				GetEmployeeResponse.class);
		return response.getEmployee();
	}

	@Test
	public void given_GET_with_NoEmployee_thenReturns400() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/200")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(400));
	}

	@Test
	public void given_GET_with_NoAccount_thenReturns404() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(404));
	}

	@Test
	public void given_GET_with_ValidAccount_thenReturns200() throws Exception {
		Employee employee = createEmployee("Bob", "Friday");
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employees/" + employee.getEmployeeId())
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(200));
	}

	@Test
	public void given_POST_with_EmptyFName_thenReturns422() throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest("", "McCloud");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(422))
				.andReturn();
		DemoErrorResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), DemoErrorResponse.class);
		assertTrue(response.getMessage().contains("firstName: size must be between 1 and 64"));
		assertEquals(response.getStatus(), 422);
	}

	@Test
	public void given_POST_with_EmptyLName_thenReturns422() throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest("Marty", "");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(422))
				.andReturn();
		DemoErrorResponse response = mapper.readValue(result.getResponse().getContentAsByteArray(), DemoErrorResponse.class);
		assertTrue(response.getMessage().contains("lastName: size must be between 1 and 64"));
		assertEquals(response.getStatus(), 422);
	}

	@Test
	public void given_POST_with_EmptyBody_thenReturns400() throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest("Marty", "");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(""))
				.andExpect(status().is(400))
				.andReturn();
	}

	@Test
	public void givenPost_with_InvalidBody_thenReturns400() throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest("Marty", "");
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content("a".repeat(100)))
				.andExpect(status().is(400))
				.andReturn();
	}

	@Test
	public void given_POST_thenReturns200() throws Exception {
		CreateEmployeeRequest request = new CreateEmployeeRequest("Jennifer", "McCloud");
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employees")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().isOk());
	}

	@Test
	public void given_PUT_with_NoEmployee_thenReturns400() throws Exception {
		UpdateEmployeeRequest request = new UpdateEmployeeRequest("123", "Jennifer", "McCloud");
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + request.getEmployeeId())
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(400));
	}

	@Test
	public void given_PUT_with_ValidEmployee_thenReturns200() throws Exception {
		Employee employee = createEmployee("Bob", "Barker");
		UpdateEmployeeRequest request = new UpdateEmployeeRequest(employee.getEmployeeId(), "Robert", "McCloud");
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + request.getEmployeeId())
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(200));
		Employee changed = getEmployee(employee.getEmployeeId());
		assertEquals("Robert", changed.getFirstName());
		assertEquals("McCloud", changed.getLastName());
	}

	@Test
	public void given_PUT_with_ValidEmployee_butWithInvalidLName_thenReturns422() throws Exception {
		Employee employee = createEmployee("Bob", "Barker");
		UpdateEmployeeRequest request = new UpdateEmployeeRequest(employee.getEmployeeId(), "Robert", "");
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + request.getEmployeeId())
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(422));
	}

	@Test
	public void given_PUT_with_ValidEmployee_butWithInvalidFName_thenReturns422() throws Exception {
		Employee employee = createEmployee("Bob", "Barker");
		UpdateEmployeeRequest request = new UpdateEmployeeRequest(employee.getEmployeeId(), "x".repeat(100), "Barker");
		mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employees/" + request.getEmployeeId())
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(mapper.writeValueAsString(request)))
				.andExpect(status().is(422));
	}

	@Test
	public void given_DELETE_with_NoEmployee_thenReturns400() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/2002")
						.accept(MediaType.APPLICATION_JSON_VALUE)
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(400));
	}

	@Test
	public void given_DELETE_with_ValidEmployee_thenReturns200() throws Exception {
		Employee employee = createEmployee("Bob", "Barker");
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employees/" + employee.getEmployeeId())
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().is(200));
	}

}
