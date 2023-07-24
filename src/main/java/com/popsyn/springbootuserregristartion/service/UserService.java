package com.popsyn.springbootuserregristartion.service;


import com.popsyn.springbootuserregristartion.entity.User;
import com.popsyn.springbootuserregristartion.entity.VerificationToken;
import com.popsyn.springbootuserregristartion.registration.RegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    List<User> getAllUser();

    User saveUser(RegistrationRequest registrationRequest);

    Optional<User> findByEmail(String email);

    void saveVerificationToken(User theUser, String verificationToken);

    String validateToken(String verificationToken);
}
