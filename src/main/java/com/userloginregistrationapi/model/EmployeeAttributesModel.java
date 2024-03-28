package com.userloginregistrationapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAttributesModel {
    private String id;
    private String role;
    private String department;
    private TimeSchedule timeSchedule;
    private int yearsOfExperience;
    private String clearanceLevel;
    private String employmentStatus;
}
