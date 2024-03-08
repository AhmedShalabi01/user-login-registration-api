package com.userloginregistrationapi.controller;

import com.userloginregistrationapi.model.UserAttributesModel;
import com.userloginregistrationapi.model.UserLoginModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
import com.userloginregistrationapi.service.UserLoginRegistrationService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pacs")
public class UserLoginRegistrationController {

    private final UserLoginRegistrationService userLoginRegistrationService;

    public UserLoginRegistrationController(UserLoginRegistrationService userLoginRegistrationService) {
        this.userLoginRegistrationService = userLoginRegistrationService;
    }


    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserRegistrationModel userModel){
        UserAttributesModel userAttributesModel = userLoginRegistrationService.registerNewUser(userModel);
        return new ResponseEntity<>(userAttributesModel, HttpStatus.CREATED);
    }



    @PostMapping(path = "/login")
    public ResponseEntity<UserAttributesModel> loginUser(@RequestBody UserLoginModel loginModel) {

        UserAttributesModel userAttributesModel = userLoginRegistrationService.validateExistingUser(loginModel);
        return new ResponseEntity<>(userAttributesModel, HttpStatus.OK);
    }
}
