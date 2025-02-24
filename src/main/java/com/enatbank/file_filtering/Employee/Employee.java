package com.enatbank.file_filtering.Employee;

import com.enatbank.file_filtering.Branch.Branch;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {
    private String employeeId;
    private String fullName;
    private Branch branch;


}
