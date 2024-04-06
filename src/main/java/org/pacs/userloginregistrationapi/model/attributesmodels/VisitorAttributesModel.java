package org.pacs.userloginregistrationapi.model.attributesmodels;

import org.pacs.userloginregistrationapi.model.TimeSchedule;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VisitorAttributesModel {
    private String id;
    private String department;
    private String role;
    private TimeSchedule timeSchedule;
    private String clearanceLevel;
}
