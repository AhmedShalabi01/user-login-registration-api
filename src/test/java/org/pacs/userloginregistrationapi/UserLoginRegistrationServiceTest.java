package org.pacs.userloginregistrationapi;

import org.pacs.userloginregistrationapi.model.UserInfoModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.VisitorAttributesModel;
import org.pacs.userloginregistrationapi.service.NonceService;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

    @Mock
    private NonceService nonceService;

    @InjectMocks
    private UserLoginRegistrationService userLoginRegistrationService;

    private static UserRegistrationModel userRegistrationModel;
    private static UserInfoModel userInfoModel;
    private static EmployeeAttributesModel employeeAttributesModel;
    private static VisitorAttributesModel visitorAttributesModel;
    private static UserLoginModel userLoginModel;
    private static String seed;

    @BeforeAll
    static void setup() {
        userRegistrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test2244");
        userInfoModel = new UserInfoModel("204209", "32154897513214", "John", "Doe", "john@example.com");
        TimeSchedule timeSchedule = new TimeSchedule(LocalTime.of(8, 30), LocalTime.of(17, 30), Set.of("MONDAY", "WEDNESDAY"));
        userLoginModel = new UserLoginModel("204209", "@Test2244");

        employeeAttributesModel = new EmployeeAttributesModel("204209", "Manger", "HR", timeSchedule, "Level 1", "Full-time");
        visitorAttributesModel = new VisitorAttributesModel("1","Engineering","Visitor",timeSchedule,"Level 2");
        seed = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
    }

    @Test
    void testRegisterNewEmployee_Success() {

        when(userExternalApiService.fetchEmployeeInfoFromCompanyApi(any())).thenReturn(userInfoModel);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userExternalApiService.fetchEmployeeAttributesFromCompanyApi(any())).thenReturn(employeeAttributesModel);
        doNothing().when(nonceService).generateNonceSequence(userRegistrationModel.getId(), seed, 10);

        userLoginRegistrationService.registerNewEmployee(userRegistrationModel, seed);

        verify(userExternalApiService, times(1)).fetchEmployeeInfoFromCompanyApi(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userExternalApiService, times(1)).fetchEmployeeAttributesFromCompanyApi(any());
        verify(userExternalApiService, times(1)).saveNewEmployeeCredentials(any());
        verify(userExternalApiService, times(1)).saveNewEmployeeAttributes(any());
    }

    @Test
    void testValidateExistingEmployee_Success() {

        when(userExternalApiService.fetchEmployeeCredentials(userLoginModel.getEmail())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword())).thenReturn(true);
        when(userExternalApiService.fetchEmployeeAttributesFromAttributesApi(userRegistrationModel.getId())).thenReturn(employeeAttributesModel);
        doNothing().when(nonceService).generateNonceSequence(userRegistrationModel.getId(), seed, 10);

        userLoginRegistrationService.validateExistingEmployee(userLoginModel, seed);

        verify(userExternalApiService, times(1)).fetchEmployeeCredentials(userLoginModel.getEmail());
        verify(passwordEncoder, times(1)).matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
        verify(userExternalApiService, times(1)).fetchEmployeeAttributesFromAttributesApi(userRegistrationModel.getId());
    }
    @Test
    void testRegisterNewEmployee_Failure_EmployeeNotFound() {

        when(userExternalApiService.fetchEmployeeInfoFromCompanyApi(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.registerNewEmployee(userRegistrationModel, seed));
    }

    @Test
    void testValidateExistingEmployee_Failure_EmployeeNotFound() {

        when(userExternalApiService.fetchEmployeeCredentials(any())).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> userLoginRegistrationService.validateExistingEmployee(userLoginModel, seed));
    }

    @Test
    void testValidateExistingEmployee_Failure_IncorrectPassword() {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test");

        when(userExternalApiService.fetchEmployeeCredentials(any())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.validateExistingEmployee(userLoginModel, seed));
    }


    @Test
    void testRegisterNewVisitor_Success() {

        when(userExternalApiService.fetchVisitorInfoFromVisitorApi(any())).thenReturn(userInfoModel);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userExternalApiService.fetchVisitorAttributesFromVisitorApi(any())).thenReturn(visitorAttributesModel);
        doNothing().when(nonceService).generateNonceSequence(userRegistrationModel.getId(), seed, 10);

        userLoginRegistrationService.registerNewVisitor(userRegistrationModel, seed);

        verify(userExternalApiService, times(1)).fetchVisitorInfoFromVisitorApi(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userExternalApiService, times(1)).fetchVisitorAttributesFromVisitorApi(any());
        verify(userExternalApiService, times(1)).saveNewVisitorCredentials(any());
        verify(userExternalApiService, times(1)).saveNewVisitorAttributes(any());
    }

    @Test
    void testValidateExistingVisitor_Success() {

        when(userExternalApiService.fetchVisitorCredentials(userLoginModel.getEmail())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword())).thenReturn(true);
        when(userExternalApiService.fetchVisitorAttributesFromAttributesApi(userRegistrationModel.getId())).thenReturn(visitorAttributesModel);
        doNothing().when(nonceService).generateNonceSequence(userRegistrationModel.getId(), seed, 10);

        userLoginRegistrationService.validateExistingVisitor(userLoginModel, seed);

        verify(userExternalApiService, times(1)).fetchVisitorCredentials(userLoginModel.getEmail());
        verify(passwordEncoder, times(1)).matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
        verify(userExternalApiService, times(1)).fetchVisitorAttributesFromAttributesApi(userRegistrationModel.getId());
    }

    @Test
    void testRegisterNewVisitor_Failure_visitorNotFound() {

        when(userExternalApiService.fetchVisitorInfoFromVisitorApi(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.registerNewVisitor(userRegistrationModel, seed));
    }

    @Test
    void testValidateExistingVisitor_Failure_VisitorNotFound() {

        when(userExternalApiService.fetchVisitorCredentials(any())).thenThrow(WebClientResponseException.class);

        assertThrows(WebClientResponseException.class, () -> userLoginRegistrationService.validateExistingVisitor(userLoginModel, seed));
    }

    @Test
    void testValidateExistingVisitor_Failure_IncorrectPassword() {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test");

        when(userExternalApiService.fetchEmployeeCredentials(any())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.validateExistingEmployee(userLoginModel, seed));
    }
}