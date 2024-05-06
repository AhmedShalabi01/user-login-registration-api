package org.pacs.userloginregistrationapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @Email(message = "Given Email address has an unsuitable format")
    @JsonProperty("EM")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @JsonProperty("PW")
    private String password;
}
