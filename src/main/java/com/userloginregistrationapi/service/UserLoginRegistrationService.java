package com.userloginregistrationapi.service;

import com.userloginregistrationapi.model.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class UserLoginRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserExternalApiService userExternalApiService;

    public EmployeeAttributesModel registerNewEmployee(@Valid UserRegistrationModel userModel) {

        UserInfoModel userFetchedFromCompanyDB = userExternalApiService.fetchEmployeeInfoFromCompanyApi(userModel.getEmail());
        EmployeeAttributesModel employeeAttributesModel = userExternalApiService.fetchEmployeeAttributesFromCompanyApi(userModel.getEmail());
        Boolean checkCredentials = checkCredentials(userFetchedFromCompanyDB, userModel);

        if (checkCredentials) {
            String encodedPass = this.passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(encodedPass);
            userModel.setId(userFetchedFromCompanyDB.getId());
        } else {
            throw new EntityNotFoundException("The User details can not be found please contact ADMIN ");
        }

        userExternalApiService.saveNewEmployeeCredentials(userModel);
        userExternalApiService.saveNewEmployeeAttributes(employeeAttributesModel);
        return employeeAttributesModel;

    }

    public VisitorAttributesModel registerNewVisitor(@Valid UserRegistrationModel userModel){

        UserInfoModel userFetchedFromVisitorsDB = userExternalApiService.fetchVisitorInfoFromVisitorApi(userModel.getEmail());
        VisitorAttributesModel visitorAttributesModel = userExternalApiService.fetchVisitorAttributesFromVisitorApi(userModel.getEmail());
        Boolean checkCredentials = checkCredentials(userFetchedFromVisitorsDB,userModel);

        if (checkCredentials) {
            String encodedPass = this.passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(encodedPass);
            userModel.setId(userFetchedFromVisitorsDB.getId());
        } else {
            throw new EntityNotFoundException("The User details can not be found please contact ADMIN ");
        }
        userExternalApiService.saveNewVisitorCredentials(userModel);
        userExternalApiService.saveNewVisitorAttributes(visitorAttributesModel);
        return visitorAttributesModel;
    }

    public EmployeeAttributesModel validateExistingEmployee(UserLoginModel userLoginModel) {

        UserRegistrationModel userRegistrationModel = userExternalApiService.fetchEmployeeCredentials(userLoginModel.getEmail());
            boolean isPwdRight = passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
            if (isPwdRight) {
                return userExternalApiService.fetchEmployeeAttributesFromAttributesApi(userRegistrationModel.getId());
            } else
                throw new EntityNotFoundException("User was not found");
    }

    public VisitorAttributesModel validateExistingVisitor(UserLoginModel userLoginModel) {

        UserRegistrationModel userRegistrationModel = userExternalApiService.fetchVisitorCredentials(userLoginModel.getEmail());
        boolean isPwdRight = passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
        if (isPwdRight) {
            return userExternalApiService.fetchVisitorAttributesFromAttributesApi(userRegistrationModel.getId());
        } else
            throw new EntityNotFoundException("User was not found");
    }

    private Boolean checkCredentials(UserInfoModel userInfoModel, UserRegistrationModel userRegistrationModel) {
        return userInfoModel.getSsn().equals(userRegistrationModel.getSsn()) &&
                userInfoModel.getFirstName().equals(userRegistrationModel.getFirstName()) &&
                userInfoModel.getLastName().equals(userRegistrationModel.getLastName()) &&
                userInfoModel.getEmail().equals(userRegistrationModel.getEmail());
    }

}
