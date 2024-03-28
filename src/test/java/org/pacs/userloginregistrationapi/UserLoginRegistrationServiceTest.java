package org.pacs.userloginregistrationapi;

import org.pacs.userloginregistrationapi.model.UserInfoModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.service.UserExternalApiService;
import org.pacs.userloginregistrationapi.service.UserLoginRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pacs.userloginregistrationapi.model.TimeSchedule;
import org.pacs.userloginregistrationapi.model.UserLoginModel;
import org.pacs.userloginregistrationapi.model.UserRegistrationModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserLoginRegistrationServiceTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserExternalApiService userExternalApiService;

    @InjectMocks
    private UserLoginRegistrationService userLoginRegistrationService;

    private static UserRegistrationModel userRegestrationModel;
    private static UserInfoModel userInfoModel;
    private static EmployeeAttributesModel employeeAttributesModel;
    private static UserLoginModel userLoginModel;

    @BeforeAll
    static void setup() {
        userRegestrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test2244");
        userInfoModel = new UserInfoModel("204209", "32154897513214", "John", "Doe", "john@example.com");
        TimeSchedule timeSchedule = new TimeSchedule(LocalTime.of(8, 30), LocalTime.of(17, 30), Set.of("MONDAY", "WEDNESDAY"));
        employeeAttributesModel = new EmployeeAttributesModel("204209", "Manger", "HR", timeSchedule, 5, "Level 1", "Full-time");
        userLoginModel = new UserLoginModel("204209", "@Test2244");
    }

    @Test
    void testRegisterNewUser_Success() {

        when(userExternalApiService.fetchEmployeeInfoFromCompanyApi(any())).thenReturn(userInfoModel);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userExternalApiService.fetchEmployeeAttributesFromCompanyApi(any())).thenReturn(employeeAttributesModel);

        userLoginRegistrationService.registerNewEmployee(userRegestrationModel);

        verify(userExternalApiService, times(1)).fetchEmployeeInfoFromCompanyApi(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userExternalApiService, times(1)).fetchEmployeeAttributesFromCompanyApi(any());
        verify(userExternalApiService, times(1)).saveNewEmployeeCredentials(any());
        verify(userExternalApiService, times(1)).saveNewEmployeeAttributes(any());
    }

    @Test
    void testValidateExistingUser_Success() {

        when(userExternalApiService.fetchEmployeeCredentials(userLoginModel.getEmail())).thenReturn(userRegestrationModel);
        when(passwordEncoder.matches(userLoginModel.getPassword(),userRegestrationModel.getPassword())).thenReturn(true);
        when(userExternalApiService.fetchEmployeeAttributesFromAttributesApi(userRegestrationModel.getId())).thenReturn(employeeAttributesModel);

        userLoginRegistrationService.validateExistingEmployee(userLoginModel);

        verify(userExternalApiService, times(1)).fetchEmployeeCredentials(userLoginModel.getEmail());
        verify(passwordEncoder, times(1)).matches(userLoginModel.getPassword(), userRegestrationModel.getPassword());
        verify(userExternalApiService, times(1)).fetchEmployeeAttributesFromAttributesApi(userRegestrationModel.getId());
    }
    @Test
    void testRegisterNewUser_Failure_UserNotFound() {

        when(userExternalApiService.fetchEmployeeInfoFromCompanyApi(any())).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> userLoginRegistrationService.registerNewEmployee(userRegestrationModel));
    }

    @Test
    void testValidateExistingUser_Failure_UserNotFound() {

        when(userExternalApiService.fetchEmployeeCredentials(any())).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> userLoginRegistrationService.validateExistingEmployee(userLoginModel));
    }

    @Test
    void testValidateExistingUser_Failure_IncorrectPassword() {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test");

        when(userExternalApiService.fetchEmployeeCredentials(any())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.validateExistingEmployee(userLoginModel));
    }
}