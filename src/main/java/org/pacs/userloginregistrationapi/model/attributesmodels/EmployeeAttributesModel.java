package org.pacs.userloginregistrationapi.model.attributesmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pacs.userloginregistrationapi.model.TimeSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeAttributesModel {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("RL")
    private String role;
    @JsonProperty("DP")
    private String department;
    @JsonProperty("TS")
    private TimeSchedule timeSchedule;
    @JsonProperty("CL")
    private String clearanceLevel;
    @JsonProperty("ES")
    private String employmentStatus;
}
