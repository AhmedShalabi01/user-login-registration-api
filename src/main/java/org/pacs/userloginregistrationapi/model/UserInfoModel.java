package org.pacs.userloginregistrationapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoModel {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("SSN")
    private String ssn;
    @JsonProperty("FN")
    private String firstName;
    @JsonProperty("LN")
    private String lastName;
    @JsonProperty("EM")
    private String email;
}
