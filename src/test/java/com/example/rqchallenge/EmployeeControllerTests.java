package com.example.rqchallenge;

import com.example.rqchallenge.employees.EmployeeController;
import com.example.rqchallenge.employees.exceptions.EmployeeNotFoundException;
import com.example.rqchallenge.employees.models.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@RunWith(SpringRunner.class)
public class EmployeeControllerTests {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetEmployees() throws Exception {

        Mockito.when(employeeService.getAllEmployees()).thenReturn(getMockEmployees());
        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo("21")));
    }

    @Test
    public void testGetEmployeesEmpty() throws Exception {

        Mockito.when(employeeService.getAllEmployees()).thenReturn(new ArrayList<Employee>());
        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    public void testGetEmployeesError() throws Exception {

        Mockito.when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Parse"));
        mockMvc.perform(get("/")).
                andExpect(status().is5xxServerError()).
                andExpect(jsonPath("$.message", Matchers.equalTo("Something went wrong.We are on it.")));
    }

    @Test
    public void testGetEmployeesByNameSearch() throws Exception {

        Mockito.when(employeeService.getEmployeesByNameSearch("sample")).thenReturn(getMockEmployees());
        ;
        mockMvc.perform(get("/search/sample"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Matchers.equalTo("21")));
    }

    @Test
    public void testGetEmployeeById() throws Exception {

        Mockito.when(employeeService.getEmployeeById("21")).thenReturn(getMockEmployees().get(0));
        ;
        mockMvc.perform(get("/21"))
                .andExpect(jsonPath("$.id", Matchers.equalTo("21")));
    }

    @Test
    public void testGetEmployeeByIdError() throws Exception {
        String id = "99";

        Mockito.when(employeeService.getEmployeeById(id)).thenThrow(new EmployeeNotFoundException(id));
        mockMvc.perform(get("/" + id)).
                andExpect(status().isNotFound()).
                andExpect(jsonPath("$.message", Matchers.equalTo("Employee with Id 99 not found")));
    }

    @Test
    public void testGetHighestSalaryOfEmployees() throws Exception {

        Mockito.when(employeeService.getHighestSalaryOfEmployees()).thenReturn(1212);
        ;
        mockMvc.perform(get("/highestSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo(1212)));
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() throws Exception {
        ArrayList<String> names = new ArrayList<>();
        names.add("sad");
        names.add("sda");
        Mockito.when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(names);
        ;
        mockMvc.perform(get("/topTenHighestEarningEmployeeNames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0]", Matchers.equalTo("sad")));
    }

    @Test
    public void testCreateEmployee() throws Exception {
        String createJson = "{\n" +
                "\"employee_name\": \"Tiger Nixon\",\n" +
                "\"employee_salary\": \"320800\",\n" +
                "\"employee_age\": \"61\",\n" +
                "\"profile_image\": \"\"\n" +
                "}";
        Employee employee = new Employee("1", 320800, 61, "Tiger Nixon", "");
        Mockito.when(employeeService.createEmployee(any())).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .content(createJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employee_name", Matchers.equalTo("Tiger Nixon")))
                .andExpect(jsonPath("$.employee_age", Matchers.equalTo(61)))
                .andExpect(jsonPath("$.employee_salary", Matchers.equalTo(320800)));
    }

    @Test
    public void testDeleteEmployeeById() throws Exception {
        String id = "21";
        Employee employee = new Employee("21", 3000, 21, "sample", "sa");
        Mockito.when(employeeService.deleteEmployeeById(id)).thenReturn(employee.getName());
        mockMvc.perform(delete("/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.equalTo("sample")));
    }

    public List<Employee> getMockEmployees() {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee("21", 3000, 21, "sample", "sa");
        Employee employee1 = new Employee("212", 30000, 23, "sample sam", "");
        employees.add(employee);
        employees.add(employee1);
        return employees;
    }

}
