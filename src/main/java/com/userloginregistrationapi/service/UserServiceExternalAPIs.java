package com.userloginregistrationapi.service;

import com.userloginregistrationapi.model.UserAttributesModel;
import com.userloginregistrationapi.model.UserCompanyModel;
import com.userloginregistrationapi.model.UserRegistrationModel;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class UserServiceExternalAPIs {
    private final WebClient webClient;
    private final WebClient webClient2;
    private final WebClient webClient3;

    public UserServiceExternalAPIs() {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/employee")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient2 = WebClient.builder()
                .baseUrl("http://localhost:8080/user-credentials")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.webClient3 = WebClient.builder()
                .baseUrl("http://localhost:8083/users-attributes")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }


    public UserCompanyModel fetchUserInfoFromCompanyDB(String userId){
        return webClient.get()
                .uri("/{id}/info" ,userId)
                .retrieve()
                .bodyToMono(UserCompanyModel.class)
                .block();
    }

    public UserAttributesModel fetchUserAttributesFromCompanyDB(String userId){
        return webClient.get()
                .uri("/{id}/attributes" ,userId)
                .retrieve()
                .bodyToMono(UserAttributesModel.class)
                .block();
    }

    public UserRegistrationModel fetchUserCredentials(String userId){
        return webClient2.get()
                .uri("/id/{id}" ,userId)
                .retrieve()
                .bodyToMono(UserRegistrationModel.class)
                .block();
    }

    public UserAttributesModel fetchUserAttributes(String userId){
        return webClient3.get()
                .uri("/findUserAttributes/{id}" ,userId)
                .retrieve()
                .bodyToMono(UserAttributesModel.class)
                .block();
    }

    public void saveNewUserAttributes(UserAttributesModel userAttributesModel){
        webClient3.post()
                .uri("/add")
                .bodyValue(userAttributesModel)
                .retrieve()
                .toBodilessEntity()
                .block();

    }

    public void saveNewUserCredentials(UserRegistrationModel userModel){
        webClient2.post()
                .uri("/add")
                .bodyValue(userModel)
                .retrieve()
                .toBodilessEntity()
                .block();
    }


//    public void saveNewUserAttributes(UserAttributesModel userAttributesModel){
//        webClient3.post()
//                .uri("/add")
//                .bodyValue(userAttributesModel)
//                .retrieve()
//                .toBodilessEntity()
//                .subscribe(responseEntity -> {},
//                        error -> {
//                            if (error instanceof WebClientResponseException ex) {
//                                throw ex;
//                            } else {
//                                System.err.println("An unexpected error occurred: " + error.getMessage());
//                            }
//                        });
//
//    }
//
//    public void saveNewUserCredentials(UserRegistrationModel userModel){
//        webClient2.post()
//                .uri("/add")
//                .bodyValue(userModel)
//                .retrieve()
//                .toBodilessEntity()
//                .subscribe(responseEntity -> {},
//                        error -> {
//                            if (error instanceof WebClientResponseException ex) {
//                                throw ex;
//                            } else {
//                                System.err.println("An unexpected error occurred: " + error.getMessage());
//                            }
//                        });
//    }



}
