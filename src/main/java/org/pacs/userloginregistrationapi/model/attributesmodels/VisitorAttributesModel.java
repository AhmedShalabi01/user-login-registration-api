package org.pacs.userloginregistrationapi.model.attributesmodels;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.pacs.userloginregistrationapi.model.TimeSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorAttributesModel {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DP")
    private String department;
    @JsonProperty("RL")
    private String role;
    @JsonProperty("TS")
    private TimeSchedule timeSchedule;
    @JsonProperty("CL")
    private String clearanceLevel;
}
