package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    /* Should be a database */
    protected static ArrayList<String> tokens = new ArrayList<>();
    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/health")
    public String GetHealth() {
        return "200 OK";
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees(@RequestParam(name="token") String token) {
        List<Employee> employees = employeeService.retrieveEmployees();

        if (null == employees) {
            System.out.println("Failed to retrieve data");
            return null;
        }

        /* Just adding a token shouldn't be here in production code */
        if (tokens.isEmpty()) {
            tokens.add("lol");
            System.out.println("added default token");
        }

        /*
         * Very simple would also add a delay for too many retries
         * also use a database with encrypted and hashed+salted passwords
         * the database should handle this normally
         */
        for (String s : tokens) {
            if (s.contains(token)) {
                System.out.println("Access granted");
                return employees;
            }
        }

        /* the token should be here in production code */
        System.out.println("Failed to validate access [ " + token + " ] ");
        return null;
    }

    @PostMapping("/employee")
    public String addEmployee(@RequestBody Employee employee, @RequestParam(name="token") String token) {
        /* Just adding a token shouldn't be here in production code */
        if (tokens.isEmpty()) {
            tokens.add("lol");
            System.out.println("added default token");
        }

        /*
         * Very simple would also add a delay for too many retries
         * also use a database with encrypted and hashed+salted passwords
         * the database should handle this normally
         */
        for (String s : tokens) {
            if (s.contains(token)) {
                System.out.println("Access granted");
                /* We don't want a segfault, so lets check the value before failing */
                if (employee.getName().isEmpty()) {
                    return "Failure";
                }

                /* probably increase the length of the access token */
                employee.setToken(UUID.randomUUID().toString().replace("-", ""));
                employeeService.saveEmployee(employee);

                System.out.println(employee.getToken());
                return "Success";
            }
        }

        return "Failure";
    }

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId, @RequestParam(name="token") String token) {
        Employee employee = employeeService.getEmployee(employeeId);

        if (null == employee) {
            System.out.println("Failed to retrieve data");
            return null;
        }

        /* Just adding a token shouldn't be here in production code */
        if (tokens.isEmpty()) {
            tokens.add("lol");
            System.out.println("added default token");
        }

        /*
         * Very simple would also add a delay for too many retries
         * also use a database with encrypted and hashed+salted passwords
         * the database should handle this normally
         */
        for (String s : tokens) {
            if (s.contains(token) || employee.getToken().contains(token)) {
                System.out.println("Access granted");
                return employee;
            }
        }

        return null;
    }

    /*
    * similar to the add employee method, but I think the add employee method
    * has a clearer name
    @PostMapping("/employees")
    public void saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
        System.out.println("Employee Saved Successfully");
    }
    */

    /* allowing employees to delete themselfs maybe is not the best thing to do...*/
    @DeleteMapping("/employees/{employeeId}")
    public String deleteEmployee(@PathVariable(name="employeeId")Long employeeId, @RequestParam(name="token") String token) {
        Employee employee = employeeService.getEmployee(employeeId);

        /* Just adding a token shouldn't be here in production code */
        if (tokens.isEmpty()) {
            tokens.add("lol");
            System.out.println("added default token");
        }

        /*
         * Very simple would also add a delay for too many retries
         * also use a database with encrypted and hashed+salted passwords
         * the database should handle this normally
         */
        for (String s : tokens) {
            if (s.contains(token) || employee.getToken().contains(token)) {
                System.out.println("Access granted");
                employeeService.deleteEmployee(employeeId);
                return "Employee Deleted Successfully";
            }
        }

        return "Failure";
    }

    @PutMapping("/employees/{employeeId}")
    public String updateEmployee(@RequestBody Employee employeeData, @PathVariable(name="employeeId") Long employeeId, @RequestParam String token) {
        Employee employee = employeeService.getEmployee(employeeId);

        if (null != employee) {
            System.out.println("Failed to retrieve data");
            return "Failure";
        }

        /* Just adding a token shouldn't be here in production code */
        if (tokens.isEmpty()) {
            tokens.add("lol");
            System.out.println("added default token");
        }

        /*
         * Very simple would also add a delay for too many retries
         * also use a database with encrypted and hashed+salted passwords
         * the database should handle this normally
         */
        for (String s : tokens) {
            if (s.contains(token) || employee.getToken().contains(token)) {
                System.out.println("Access granted");
                employeeService.updateEmployee(employee);
                return "Employee Updated Successfully";
            }
        }

        return "Failure";
    }

}
