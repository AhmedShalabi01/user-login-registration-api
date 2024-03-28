package com.userloginregistrationapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorAttributesModel {
    private String id;
    private String role;
    private TimeSchedule timeSchedule;
    private String clearanceLevel;
}
