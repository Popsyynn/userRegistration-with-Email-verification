package com.popsyn.springbootuserregristartion.controller;

import com.popsyn.springbootuserregristartion.entity.User;
import com.popsyn.springbootuserregristartion.entity.VerificationToken;
import com.popsyn.springbootuserregristartion.event.RegistrationCompleteEvent;
import com.popsyn.springbootuserregristartion.registration.RegistrationRequest;
import com.popsyn.springbootuserregristartion.repository.VerificationTokenRepository;
import com.popsyn.springbootuserregristartion.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @PostMapping
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = userService.saveUser(registrationRequest);
        //publishing an event
        publisher.publishEvent(new RegistrationCompleteEvent(user ,applicationUrl(request)));
        return "Success , Check email for registration code";
    }

    //verifying the user and validating token
    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken.getUser().getIsEnabled()){
            return "Account verified";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid")){
            return "Email verified successfully ";
        }
        return "Invalid verification Token";

    }


    public String applicationUrl(HttpServletRequest request) {
        return "https://" + request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }

}
