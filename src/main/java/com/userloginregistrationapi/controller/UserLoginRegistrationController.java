package com.userloginregistrationapi.controller;

import com.userloginregistrationapi.model.UserAttributesModel;
import com.userloginregistrationapi.model.UserLoginModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
import com.userloginregistrationapi.service.NonceGenerator;
import com.userloginregistrationapi.service.UserLoginRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pacs")
public class UserLoginRegistrationController {

    private final UserLoginRegistrationService userLoginRegistrationService;
    private final NonceGenerator nonceGenerator;
    @PostMapping(path = "/register")
    public ResponseEntity<?> registerNewUser(@RequestBody UserRegistrationModel userModel){
        UserAttributesModel userAttributesModel = userLoginRegistrationService.registerNewUser(userModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(userAttributesModel,httpHeaders,HttpStatus.CREATED);
    }



    @PostMapping(path = "/login")
    public ResponseEntity<UserAttributesModel> loginUser(@RequestBody UserLoginModel loginModel) {

        UserAttributesModel userAttributesModel = userLoginRegistrationService.validateExistingUser(loginModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(userAttributesModel,httpHeaders, HttpStatus.OK);
    }
}
