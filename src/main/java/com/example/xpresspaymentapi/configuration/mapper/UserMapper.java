package com.example.xpresspaymentapi.configuration.mapper;


import com.example.xpresspaymentapi.model.dto.user.SignUpRequest;
import com.example.xpresspaymentapi.model.entity.Contact;
import com.example.xpresspaymentapi.model.entity.User;
import com.example.xpresspaymentapi.model.enums.UserType;

public class UserMapper {
    public static User mapSignupRequestToUser(SignUpRequest signUpRequest) {
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setUserType(UserType.CUSTOMER);
        user.setBlocked(false);
        user.setActive(true);

        Contact contact = new Contact();
        contact.setEmailAddress(signUpRequest.getEmailAddress());
        contact.setPhoneNumber(signUpRequest.getPhoneNumber());

        user.setContact(contact);
        return user;
    }
}
