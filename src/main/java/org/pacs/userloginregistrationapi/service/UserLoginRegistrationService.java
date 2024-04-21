package org.pacs.userloginregistrationapi.service;

import org.pacs.userloginregistrationapi.model.UserInfoModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.VisitorAttributesModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.pacs.userloginregistrationapi.model.UserLoginModel;
import org.pacs.userloginregistrationapi.model.UserRegistrationModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@AllArgsConstructor
@Validated
public class UserLoginRegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserExternalApiService userExternalApiService;

    public EmployeeAttributesModel registerNewEmployee(@Valid UserRegistrationModel userModel) {

        UserInfoModel employeeFetchedFromCompanyDB;
        EmployeeAttributesModel employeeAttributesModel;

        try {
            employeeFetchedFromCompanyDB = userExternalApiService.fetchEmployeeInfoFromCompanyApi(userModel.getEmail());
            employeeAttributesModel = userExternalApiService.fetchEmployeeAttributesFromCompanyApi(userModel.getEmail());
        } catch (WebClientResponseException exception) {
            throw new EntityNotFoundException("The Employee details can not be found please contact ADMIN");
        }

        if (checkCredentials(employeeFetchedFromCompanyDB, userModel)) {
            String encodedPass = this.passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(encodedPass);
            userModel.setId(employeeFetchedFromCompanyDB.getId());
        } else {
            throw new EntityNotFoundException("The Employee details can not be found please contact ADMIN");
        }

        userExternalApiService.saveNewEmployeeCredentials(userModel);
        userExternalApiService.saveNewEmployeeAttributes(employeeAttributesModel);

        return employeeAttributesModel;
    }

    public VisitorAttributesModel registerNewVisitor(@Valid UserRegistrationModel userModel){

        UserInfoModel visitorFetchedFromVisitorDB;
        VisitorAttributesModel visitorAttributesModel;

        try {
            visitorFetchedFromVisitorDB = userExternalApiService.fetchVisitorInfoFromVisitorApi(userModel.getEmail());
            visitorAttributesModel = userExternalApiService.fetchVisitorAttributesFromVisitorApi(userModel.getEmail());
        } catch (WebClientResponseException exception) {
            throw new EntityNotFoundException("The Visitor details can not be found please contact ADMIN");
        }

        if (checkCredentials(visitorFetchedFromVisitorDB,userModel)) {
            String encodedPass = this.passwordEncoder.encode(userModel.getPassword());
            userModel.setPassword(encodedPass);
            userModel.setId(visitorFetchedFromVisitorDB.getId());
        } else {
            throw new EntityNotFoundException("The Visitor details can not be found please contact ADMIN");
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
                throw new EntityNotFoundException("Employee was not found");
    }

    public VisitorAttributesModel validateExistingVisitor(UserLoginModel userLoginModel) {

        UserRegistrationModel userRegistrationModel = userExternalApiService.fetchVisitorCredentials(userLoginModel.getEmail());
        boolean isPwdRight = passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
        if (isPwdRight) {
            return userExternalApiService.fetchVisitorAttributesFromAttributesApi(userRegistrationModel.getId());
        } else
            throw new EntityNotFoundException("Visitor was not found");
    }

    public UserInfoModel findExistingEmployee(String email) {
        return userExternalApiService.fetchEmployeeInfoFromUserCredentials(email);
    }
    public UserInfoModel findExistingVisitor(String email) {
        return userExternalApiService.fetchVisitorInfoFromUserCredentials(email);
    }

    private Boolean checkCredentials(UserInfoModel userInfoModel, UserRegistrationModel userRegistrationModel) {
        return userInfoModel.getSsn().equals(userRegistrationModel.getSsn()) &&
                userInfoModel.getFirstName().equals(userRegistrationModel.getFirstName()) &&
                userInfoModel.getLastName().equals(userRegistrationModel.getLastName()) &&
                userInfoModel.getEmail().equals(userRegistrationModel.getEmail());
    }
}
