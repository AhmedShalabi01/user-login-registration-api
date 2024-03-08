package com.userloginregistrationapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCompanyModel {
    private String id;
    private String ssn;
    private String firstName;
    private String lastName;
    private String email;
}
