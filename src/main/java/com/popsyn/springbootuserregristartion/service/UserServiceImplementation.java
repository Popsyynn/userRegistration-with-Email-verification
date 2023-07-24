package com.popsyn.springbootuserregristartion.service;

import com.popsyn.springbootuserregristartion.entity.User;
import com.popsyn.springbootuserregristartion.entity.VerificationToken;
import com.popsyn.springbootuserregristartion.exception.UserAlreadyExistsException;
import com.popsyn.springbootuserregristartion.registration.RegistrationRequest;
import com.popsyn.springbootuserregristartion.repository.UserRepository;
import com.popsyn.springbootuserregristartion.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User saveUser(RegistrationRequest registrationRequest) {
        Optional<User> user = userRepository.findByEmail(registrationRequest.email());
        if (user.isPresent()){
            throw new UserAlreadyExistsException(
                    "Email" + registrationRequest.email()+"is already registered");
        }
        var user1 = new User();
        user1.setEmail(registrationRequest.email());
        user1.setFirstName(registrationRequest.firstName());
        user1.setLastName(registrationRequest.lastName());
        user1.setPassword(passwordEncoder.encode(registrationRequest.password()));
        user1.setRole(registrationRequest.role());
        return userRepository.save(user1);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveVerificationToken(User theUser, String verificationToken) {
        VerificationToken verificationToken1 = new VerificationToken(verificationToken, theUser);
        verificationTokenRepository.save(verificationToken1);
    }

    @Override
    public String validateToken(String verificationToken) {
        VerificationToken verificationToken1 = verificationTokenRepository.findByToken(verificationToken);
        if (verificationToken1 == null){
            return "Token not found or is invalid";
        }
        User user = verificationToken1.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken1.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0 ){
            verificationTokenRepository.delete(verificationToken1);
            return "Token has expired";
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return "Valid";
    }
}
