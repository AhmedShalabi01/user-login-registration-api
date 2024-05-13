package org.pacs.userloginregistrationapi.controller;

import org.pacs.userloginregistrationapi.model.UserInfoModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.model.UserLoginModel;
import org.pacs.userloginregistrationapi.model.UserRegistrationModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.VisitorAttributesModel;
import org.pacs.userloginregistrationapi.service.UserLoginRegistrationService;
import lombok.RequiredArgsConstructor;
import org.pacs.userloginregistrationapi.utils.SeedGenerator;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/login-registration")
public class UserLoginRegistrationController {

    private final SeedGenerator seedGenerator;
    private final UserLoginRegistrationService userLoginRegistrationService;

    @PostMapping(path = "/employee/register")
    public ResponseEntity<EmployeeAttributesModel> registerNewEmployee(@RequestBody UserRegistrationModel userModel){
        String seed = seedGenerator.generateSeed();
        EmployeeAttributesModel employeeAttributesModel = userLoginRegistrationService.registerNewEmployee(userModel, seed);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Seed", seed);
        return new ResponseEntity<>(employeeAttributesModel,httpHeaders,HttpStatus.CREATED);
    }

    @PostMapping(path = "/visitor/register")
    public ResponseEntity<VisitorAttributesModel> registerNewVisitor(@RequestBody UserRegistrationModel userModel){
        String seed = seedGenerator.generateSeed();
        VisitorAttributesModel visitorAttributesModel = userLoginRegistrationService.registerNewVisitor(userModel, seed);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Seed", seed);
        return new ResponseEntity<>(visitorAttributesModel,httpHeaders,HttpStatus.CREATED);
    }

    @PostMapping(path = "/employee/login")
    public ResponseEntity<EmployeeAttributesModel> loginEmployee(@RequestBody UserLoginModel loginModel) {
        String seed = seedGenerator.generateSeed();
        EmployeeAttributesModel employeeAttributesModel = userLoginRegistrationService.validateExistingEmployee(loginModel, seed);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Seed", seed);
        return new ResponseEntity<>(employeeAttributesModel,httpHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/visitor/login")
    public ResponseEntity<VisitorAttributesModel> loginUVisitor(@RequestBody UserLoginModel loginModel) {
        String seed = seedGenerator.generateSeed();
        VisitorAttributesModel visitorAttributesModel = userLoginRegistrationService.validateExistingVisitor(loginModel, seed);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Server-Seed", seed);
        return new ResponseEntity<>(visitorAttributesModel,httpHeaders,HttpStatus.OK);
    }
    @GetMapping("/employee/find/info/email/{email}")
    public ResponseEntity<UserInfoModel> findEmployeeByEmail(@PathVariable String email) {
        UserInfoModel user = userLoginRegistrationService.findExistingEmployee(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/visitor/find/info/email/{email}")
    public ResponseEntity<UserInfoModel> findVisitorByEmail(@PathVariable String email) {
        UserInfoModel user = userLoginRegistrationService.findExistingVisitor(email);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
