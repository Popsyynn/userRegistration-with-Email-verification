package com.popsyn.springbootuserregristartion.event;

import com.popsyn.springbootuserregristartion.entity.User;
import com.popsyn.springbootuserregristartion.event.RegistrationCompleteEvent;
import com.popsyn.springbootuserregristartion.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    private User theUser;



    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //1. get the newly registered user
        theUser = event.getUser();
        //2. Create a verification token for the user
        String verificationToken = UUID.randomUUID().toString();
        //3. save the verification token for the user
        userService.saveVerificationToken(theUser , verificationToken);
        //4. Build the verification url to be sent to the user
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        //5. send the email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException e) {
           throw new RuntimeException(e);}
         catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("click the link to complete your registration: {}" + url);
    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Popsyynn";
        String content = "<p>Dear"  + theUser.getFirstName() + "</p>"+
                "<p>Welcome to our platform! We are excited to have you on board.</p>"+
    "<p>To complete your registration, please click on the button below:</p>" +
        "<a href="+url+ ">Complete Registration</a>"+
    "<p>Please note that this link will expire in 15 minutes.</p>"+
    "<p>If you did not request this registration, you can ignore this email. Your account will not be activated.</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("popsyynn@gmail.com" , senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(content ,true);
        javaMailSender.send(message);


}}
