package org.pacs.userloginregistrationapi.service;

import org.pacs.userloginregistrationapi.model.attributesmodels.EmployeeAttributesModel;
import org.pacs.userloginregistrationapi.model.UserInfoModel;
import org.pacs.userloginregistrationapi.model.UserRegistrationModel;
import org.pacs.userloginregistrationapi.model.attributesmodels.VisitorAttributesModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class UserExternalApiService {
    private final WebClient webClient1;
    private final WebClient webClient2;
    private final WebClient webClient3;
    private final WebClient webClient4;

    @Autowired
    public UserExternalApiService(WebClient.Builder webClientBuilder) {
        this.webClient1 = webClientBuilder
                .baseUrl("http://USER-CREDENTIALS-API/user-credentials")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient2 = webClientBuilder
                .baseUrl("http://ATTRIBUTES-MANAGEMENT-API/attributes-management/users-attributes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient3 = webClientBuilder
                .baseUrl("http://COMPANY-DATA-GATEWAY-API/employee")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient4 = webClientBuilder
                .baseUrl("http://VISITOR-MANAGEMENT-API/visitor-management")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    public UserInfoModel fetchEmployeeInfoFromCompanyApi(String email) {
        return webClient3.get()
                .uri("/find/info/email/{email}", email)
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }

    public EmployeeAttributesModel fetchEmployeeAttributesFromCompanyApi(String email) {
        return webClient3.get()
                .uri("/find/attributes/email/{email}", email)
                .retrieve()
                .bodyToMono(EmployeeAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchEmployeeCredentials(String email) {
        return webClient1.get()
                .uri("/employee/find/email/{email}", email)
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

    public UserInfoModel    fetchEmployeeInfoFromUserCredentials(String email) {
        return webClient1.get()
                .uri("/employee/find/email/{email}", email)
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }

    //---------------------------------------------------------------------------------------//

    public UserInfoModel fetchVisitorInfoFromVisitorApi(String email) {
        return webClient4.get()
                .uri("/find/info/email/{email}", email)
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }

    public VisitorAttributesModel fetchVisitorAttributesFromVisitorApi(String email) {
        return webClient4.get()
                .uri("/find/attributes/email/{email}", email)
                .retrieve()
                .bodyToMono(VisitorAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchVisitorCredentials(String email) {
        return webClient1.get()
                .uri("/visitor/find/email/{email}", email)
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

    public UserInfoModel fetchVisitorInfoFromUserCredentials(String email) {
        return webClient1.get()
                .uri("/visitor/find/email/{email}", email)
                .retrieve()
                .bodyToMono(UserInfoModel.class)
                .block();
    }
}
