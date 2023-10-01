package com.example.rqchallenge;

import com.example.rqchallenge.employees.EmployeeController;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.example.rqchallenge.employees.restexecutor.RestTemplateExecutor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
;

@WebMvcTest(EmployeeController.class)
@RunWith(SpringRunner.class)
public class EmployeeServiceTests {

  @TestConfiguration
  static class EmployeeServiceTestContextConfiguration {

    @MockBean public static RestTemplateExecutor restTemplateExecutor;

    @Bean
    public EmployeeService employeeService() {
      return new EmployeeService(restTemplateExecutor);
    }
  }

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  public void testGetEmployees() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0].id", Matchers.equalTo("1")));
  }

  @Test
  public void testGetEmployeesEmpty() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getEmptyMockResponse()));
    mockMvc
        .perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(0)));
  }

  @Test
  public void testGetEmployeesError() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockErrorResponse()));
    mockMvc
        .perform(get("/"))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", Matchers.equalTo("Something went wrong.We are on it.")));
  }

  @Test
  public void testGetEmployeesByNameSearch() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/search/tiger"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(1)))
        .andExpect(jsonPath("$[0].id", Matchers.equalTo("1")));
  }

  @Test
  public void testGetEmployeesByNameSearchEmpty() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/search/sample"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(0)));
  }

  @Test
  public void testGetEmployeeById() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/1"))
        .andExpect(jsonPath("$.id", Matchers.equalTo("1")))
        .andExpect(jsonPath("$.employee_age", Matchers.equalTo(61)))
        .andExpect(jsonPath("$.employee_name", Matchers.equalTo("Tiger Nixon")));
  }

  @Test
  public void testGetEmployeeByIdError() throws Exception {
    String id = "99";

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/" + id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", Matchers.equalTo("Employee with Id 99 not found")));
  }

  @Test
  public void testGetHighestSalaryOfEmployeesError() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getEmptyMockResponse()));
    mockMvc
        .perform(get("/highestSalary"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", Matchers.equalTo("No Data found.")));
  }

  @Test
  public void testGetHighestSalaryOfEmployees() throws Exception {

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/highestSalary"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.equalTo(320800)));
  }

  @Test
  public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(get("/topTenHighestEarningEmployeeNames"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.hasSize(2)))
        .andExpect(jsonPath("$[0]", Matchers.equalTo("Tiger Nixon")))
        .andExpect(jsonPath("$[1]", Matchers.equalTo("Garrett Winters")));
  }

  @Test
  public void testCreateEmployee() throws Exception {
    String createJson =
        "{\n"
            + "\"employee_name\": \"Tiger Nixon\",\n"
            + "\"employee_salary\": \"320800\",\n"
            + "\"employee_age\": \"61\",\n"
            + "\"profile_image\": \"\"\n"
            + "}";
    String response =
        "{\n"
            + "\"id\": \"1\",\n"
            + "\"employee_name\": \"Tiger Nixon\",\n"
            + "\"employee_salary\": \"320800\",\n"
            + "\"employee_age\": \"61\",\n"
            + "\"profile_image\": \"\"\n"
            + "}";
    String res =
        "{\"status\":\"success\",\"data\":"
            + response
            + ",\"message\":\"Successfully! All records has been fetched.\"}\n";

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                any(), any(), any(), any()))
        .thenReturn(Optional.ofNullable(res));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/")
                .content(createJson)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.equalTo("1")))
        .andExpect(jsonPath("$.employee_name", Matchers.equalTo("Tiger Nixon")))
        .andExpect(jsonPath("$.employee_age", Matchers.equalTo(61)))
        .andExpect(jsonPath("$.employee_salary", Matchers.equalTo(320800)));
  }

  @Test
  public void testCreateEmployeeError() throws Exception {
    String createJson =
        "{\n"
            + "\"employee_name\": \"Tiger Nixon\",\n"
            + "\"employee_salary\": \"320800\",\n"
            + "\"employee_age\": \"61\",\n"
            + "\"profile_image\": \"\"\n"
            + "}";

    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                any(), any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/")
                .content(createJson)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.message", Matchers.equalTo("Error in creating employee")));
  }

  @Test
  public void testDeleteEmployeeById() throws Exception {
    String id = "1";
    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                any(), any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));
    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc
        .perform(delete("/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", Matchers.equalTo("Tiger Nixon")));
  }

  @Test
  public void testDeleteEmployeeByIdError() throws Exception {
    String id = "21";
    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                any(), any(), any(), any()))
        .thenReturn(Optional.ofNullable(null));
    Mockito.when(
            EmployeeServiceTestContextConfiguration.restTemplateExecutor.execute(
                "employees", HttpMethod.GET, null, String.class))
        .thenReturn(Optional.ofNullable(getMockMultiResponse()));
    mockMvc.perform(delete("/" + id)).andExpect(status().isNotFound());
  }

  public String getMockMultiResponse() {
    return "{\"status\":\"success\",\"data\":[{\"id\":1,\"employee_name\":\"Tiger Nixon\",\"employee_salary\":320800,\"employee_age\":61,\"profile_image\":\"\"},{\"id\":2,\"employee_name\":\"Garrett Winters\",\"employee_salary\":170750,\"employee_age\":63,\"profile_image\":\"\"}],\"message\":\"Successfully! All records has been fetched.\"}\n";
  }

  public String getMockResponse() {
    return "{\"status\":\"success\",\"data\":[{\"id\":1,\"employee_name\":\"Tiger Nixon\",\"employee_salary\":320800,\"employee_age\":61,\"profile_image\":\"\"}],\"message\":\"Successfully! All records has been fetched.\"}\n";
  }

  public String getMockErrorResponse() {
    return "{\"status\":\"success\",\"data\":[{\"id\":1,\"employee_name\":\"Tiger Nixon\",\"employee_salary\":320800,\"employee_age\":61,\"profffile_image\":\"\"}],\"message\":\"Successfully! All records has been fetched.\"}\n";
  }

  public String getEmptyMockResponse() {
    return "{\"status\":\"success\",\"data\":[],\"message\":\"Successfully! All records has been fetched.\"}\n";
  }
}
