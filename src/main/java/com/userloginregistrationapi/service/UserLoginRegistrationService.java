package com.userloginregistrationapi.service;

import com.userloginregistrationapi.model.UserAttributesModel;
import com.userloginregistrationapi.model.UserCompanyModel;
import com.userloginregistrationapi.model.UserLoginModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
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


    public UserAttributesModel registerNewUser(@Valid UserRegistrationModel userModel) {
        UserCompanyModel userFetchedFromCompanyDB = userExternalApiService.fetchUserInfoFromCompanyDB(userModel.getId());

        if (userFetchedFromCompanyDB.getSsn().equals(userModel.getSsn()) &&
                userFetchedFromCompanyDB.getFirstName().equals(userModel.getFirstName()) &&
                userFetchedFromCompanyDB.getLastName().equals(userModel.getLastName()) &&
                userFetchedFromCompanyDB.getEmail().equals(userModel.getEmail())) {

            UserRegistrationModel registrationModel = new UserRegistrationModel(userModel.getId(), userModel.getSsn()
                    , userModel.getFirstName(), userModel.getLastName()
                    , userModel.getEmail(), this.passwordEncoder.encode(userModel.getPassword()));

            UserAttributesModel userAttributesModel = userExternalApiService.fetchUserAttributesFromCompanyDB(userModel.getId());
            userExternalApiService.saveNewUserCredentials(registrationModel);
            userExternalApiService.saveNewUserAttributes(userAttributesModel);
            return userAttributesModel;
        } else {
            throw new EntityNotFoundException("The User details can not be found please contact ADMIN ");
        }

    }

    public UserAttributesModel validateExistingUser(UserLoginModel userLoginModel) {

        UserRegistrationModel userRegistrationModel = userExternalApiService.fetchUserCredentials(userLoginModel.getId());
        if (userRegistrationModel != null) {
            boolean isPwdRight = passwordEncoder.matches(userLoginModel.getPassword(), userRegistrationModel.getPassword());
            if (isPwdRight) {
                return userExternalApiService.fetchUserAttributes(userRegistrationModel.getId());
            } else
                throw new EntityNotFoundException("User was not found");
        } else
            throw new EntityNotFoundException("User was not found");
    }

}
