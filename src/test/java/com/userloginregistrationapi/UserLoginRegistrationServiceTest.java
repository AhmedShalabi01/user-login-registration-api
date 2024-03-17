package com.userloginregistrationapi;

import com.userloginregistrationapi.model.*;
import com.userloginregistrationapi.service.UserExternalApiService;
import com.userloginregistrationapi.service.UserLoginRegistrationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    private static UserRegistrationModel userModel;
    private static UserCompanyModel userCompanyModel;
    private static UserAttributesModel userAttributesModel;
    private static UserLoginModel userLoginModel;

    @BeforeAll
    static void setup() {
        userModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test2244");
        userCompanyModel = new UserCompanyModel("204209", "32154897513214", "John", "Doe", "john@example.com");
        TimeSchedule timeSchedule = new TimeSchedule(LocalTime.of(8, 30), LocalTime.of(17, 30), Set.of("MONDAY", "WEDNESDAY"));
        userAttributesModel = new UserAttributesModel("204209", "Manger", "HR", timeSchedule, 5, "Level 1", "Full-time");
        userLoginModel = new UserLoginModel("204209", "@Test2244");
    }

    @Test
    void testRegisterNewUser_Success() {

        when(userExternalApiService.fetchUserInfoFromCompanyDB(any())).thenReturn(userCompanyModel);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userExternalApiService.fetchUserAttributesFromCompanyDB(any())).thenReturn(userAttributesModel);

        userLoginRegistrationService.registerNewUser(userModel);

        verify(userExternalApiService, times(1)).fetchUserInfoFromCompanyDB(any());
        verify(passwordEncoder, times(1)).encode(any());
        verify(userExternalApiService, times(1)).fetchUserAttributesFromCompanyDB(any());
        verify(userExternalApiService, times(1)).saveNewUserCredentials(any());
        verify(userExternalApiService, times(1)).saveNewUserAttributes(any());
    }

    @Test
    void testValidateExistingUser_Success() {

        when(userExternalApiService.fetchUserCredentials(any())).thenReturn(userModel);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(userExternalApiService.fetchUserAttributes(any())).thenReturn(userAttributesModel);

        userLoginRegistrationService.validateExistingUser(userLoginModel);

        verify(userExternalApiService, times(1)).fetchUserCredentials(any());
        verify(passwordEncoder, times(1)).matches(any(), any());
        verify(userExternalApiService, times(1)).fetchUserAttributes(any());
    }
    @Test
    void testRegisterNewUser_Failure_UserNotFound() {

        when(userExternalApiService.fetchUserInfoFromCompanyDB(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.registerNewUser(userModel));
    }

    @Test
    void testValidateExistingUser_Failure_UserNotFound() {

        when(userExternalApiService.fetchUserCredentials(any())).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.validateExistingUser(userLoginModel));
    }

    @Test
    void testValidateExistingUser_Failure_IncorrectPassword() {
        UserRegistrationModel userRegistrationModel = new UserRegistrationModel("204209", "32154897513214", "John", "Doe", "john@example.com", "@Test");

        when(userExternalApiService.fetchUserCredentials(any())).thenReturn(userRegistrationModel);
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userLoginRegistrationService.validateExistingUser(userLoginModel));
    }
}