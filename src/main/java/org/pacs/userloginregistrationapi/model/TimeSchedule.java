package org.pacs.userloginregistrationapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class TimeSchedule {
    @JsonProperty("ST")
    private LocalTime startTime;
    @JsonProperty("ET")
    private LocalTime endTime;
    @JsonProperty("DW")
    private Set<String> daysOfWeek;
}
