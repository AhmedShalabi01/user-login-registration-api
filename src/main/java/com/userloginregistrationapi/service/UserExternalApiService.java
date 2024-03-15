package com.userloginregistrationapi.service;

import com.userloginregistrationapi.model.UserAttributesModel;
import com.userloginregistrationapi.model.UserCompanyModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class UserExternalApiService {
    private final WebClient webClient1;
    private final WebClient webClient2;
    private final WebClient webClient3;

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
    }


    public UserCompanyModel fetchUserInfoFromCompanyDB(String userId) {
        return webClient3.get()
                .uri("/{id}/info", userId)
                .retrieve()
                .bodyToMono(UserCompanyModel.class)
                .block();
    }

    public UserAttributesModel fetchUserAttributesFromCompanyDB(String userId) {
        return webClient3.get()
                .uri("/{id}/attributes", userId)
                .retrieve()
                .bodyToMono(UserAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchUserCredentials(String userId) {
        return webClient1.get()
                .uri("/id/{id}", userId)
                .retrieve()
                .bodyToMono(UserRegistrationModel.class)
                .block();
    }

    public UserAttributesModel fetchUserAttributes(String userId) {
        return webClient2.get()
                .uri("/findUserAttributes/{id}", userId)
                .retrieve()
                .bodyToMono(UserAttributesModel.class)
                .block();
    }

    public void saveNewUserAttributes(UserAttributesModel userAttributesModel) {
        webClient2.post()
                .uri("/add")
                .bodyValue(userAttributesModel)
                .retrieve()
                .toBodilessEntity()
                .block();

    }

    public void saveNewUserCredentials(UserRegistrationModel userModel) {
        webClient1.post()
                .uri("/add")
                .bodyValue(userModel)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
