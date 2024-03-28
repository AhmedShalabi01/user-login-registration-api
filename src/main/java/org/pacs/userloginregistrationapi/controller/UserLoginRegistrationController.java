package org.pacs.userloginregistrationapi.controller;

import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.model.UserLoginModel;
import org.pacs.userloginregistrationapi.model.UserRegistrationModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.VisitorAttributesModel;
import org.pacs.userloginregistrationapi.service.NonceGenerator;
import org.pacs.userloginregistrationapi.service.UserLoginRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/pacs")
public class UserLoginRegistrationController {

    private final UserLoginRegistrationService userLoginRegistrationService;
    private final NonceGenerator nonceGenerator;

    @PostMapping(path = "/employee/register")
    public ResponseEntity<EmployeeAttributesModel> registerNewEmployee(@RequestBody UserRegistrationModel userModel){
        EmployeeAttributesModel employeeAttributesModel = userLoginRegistrationService.registerNewEmployee(userModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(employeeAttributesModel,httpHeaders,HttpStatus.CREATED);
    }

    @PostMapping(path = "/visitor/register")
    public ResponseEntity<VisitorAttributesModel> registerNewVisitor(@RequestBody UserRegistrationModel userModel){
        VisitorAttributesModel visitorAttributesModel = userLoginRegistrationService.registerNewVisitor(userModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(visitorAttributesModel,httpHeaders,HttpStatus.CREATED);
    }

    @PostMapping(path = "/employee/login")
    public ResponseEntity<EmployeeAttributesModel> loginEmployee(@RequestBody UserLoginModel loginModel) {
        EmployeeAttributesModel employeeAttributesModel = userLoginRegistrationService.validateExistingEmployee(loginModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(employeeAttributesModel,httpHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/visitor/login")
    public ResponseEntity<VisitorAttributesModel> loginUVisitor(@RequestBody UserLoginModel loginModel) {
        VisitorAttributesModel visitorAttributesModel = userLoginRegistrationService.validateExistingVisitor(loginModel);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Nonce",nonceGenerator.generateNonceWithDateSeed());
        return new ResponseEntity<>(visitorAttributesModel,httpHeaders,HttpStatus.OK);
    }
}
