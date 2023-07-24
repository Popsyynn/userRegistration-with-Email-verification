package com.popsyn.springbootuserregristartion.registration;

import jakarta.persistence.Entity;

public record RegistrationRequest(
        String firstName , String lastName , String email , String role , String password
) {

}
