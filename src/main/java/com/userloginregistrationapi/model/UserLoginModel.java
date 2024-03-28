package com.userloginregistrationapi.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginModel {

    @NotBlank(message = "Email cannot be blank")
    @Email
    private String email;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
