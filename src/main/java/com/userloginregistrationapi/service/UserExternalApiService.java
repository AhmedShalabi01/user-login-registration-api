package com.userloginregistrationapi.service;

import com.userloginregistrationapi.model.EmployeeAttributesModel;
import com.userloginregistrationapi.model.UserInfoModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
import com.userloginregistrationapi.model.VisitorAttributesModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class UserExternalApiService {
    private final WebClient webClient1;
    private final WebClient webClient2;
    private final WebClient webClient3;
    private final WebClient webClient4;

    public UserExternalApiService() {
        this.webClient1 = WebClient.builder()
                .baseUrl("http://localhost:8081/user-credentials")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient2 = WebClient.builder()
                .baseUrl("http://localhost:8082/users-attributes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient3 = WebClient.builder()
                .baseUrl("http://localhost:8083/employee")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient4 = WebClient.builder()
                .baseUrl("http://localhost:8087/visitor")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public UserInfoModel fetchEmployeeInfoFromCompanyApi(String email) {
        return webClient3.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/info/email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }

    public EmployeeAttributesModel fetchEmployeeAttributesFromCompanyApi(String email) {
        return webClient3.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/attributes/email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(EmployeeAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchEmployeeCredentials(String email) {
        return webClient1.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/employee/find/email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserRegistrationModel.class)
                .block();
    }

    public EmployeeAttributesModel fetchEmployeeAttributesFromAttributesApi(String userId) {
        return webClient2.get()
                .uri("/employee/find/id/{id}", userId)
                .retrieve()
                .bodyToMono(EmployeeAttributesModel.class)
                .block();
    }

    public void saveNewEmployeeAttributes(EmployeeAttributesModel employeeAttributesModel) {
        webClient2.post()
                .uri("/employee/add")
                .bodyValue(employeeAttributesModel)
                .retrieve()
                .toBodilessEntity()
                .block();

    }

    public void saveNewEmployeeCredentials(UserRegistrationModel userModel) {
        webClient1.post()
                .uri("/employee/add")
                .bodyValue(userModel)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


    //---------------------------------------------------------------------------------------//

    public UserInfoModel fetchVisitorInfoFromVisitorApi(String email) {
        return webClient4.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/info")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }

    public VisitorAttributesModel fetchVisitorAttributesFromVisitorApi(String email) {
        return webClient4.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/find/attributes")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(VisitorAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchVisitorCredentials(String email) {
        return webClient1.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/visitor/find/email")
                        .queryParam("email", email)
                        .build())
                .retrieve()
                .bodyToMono(UserRegistrationModel.class)
                .block();
    }

    public VisitorAttributesModel fetchVisitorAttributesFromAttributesApi(String userId) {
        return webClient2.get()
                .uri("/visitor/find/id/{id}", userId)
                .retrieve()
                .bodyToMono(VisitorAttributesModel.class)
                .block();
    }

    public void saveNewVisitorAttributes(VisitorAttributesModel visitorAttributesModel) {
        webClient2.post()
                .uri("/visitor/add")
                .bodyValue(visitorAttributesModel)
                .retrieve()
                .toBodilessEntity()
                .block();

    }

    public void saveNewVisitorCredentials(UserRegistrationModel userModel) {
        webClient1.post()
                .uri("/visitor/add")
                .bodyValue(userModel)
                .retrieve()
                .toBodilessEntity()
                .block();
    }



}
