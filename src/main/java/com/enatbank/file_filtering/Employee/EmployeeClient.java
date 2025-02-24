package com.enatbank.file_filtering.Employee;

import com.enatbank.file_filtering.Config.HrClientRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "employeeClient",configuration = HrClientRequestInterceptor.class,url = "${client.hr.baseUrl}")

public interface EmployeeClient {

    @GetMapping("/employees/by-employeeId/{employeeId}")
    Employee getEmployeesByEmployeeId(@PathVariable("employeeId") String employeeId);

}