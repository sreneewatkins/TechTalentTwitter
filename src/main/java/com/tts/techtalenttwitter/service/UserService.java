package com.tts.techtalenttwitter.service;

import com.tts.techtalenttwitter.model.User;

import java.util.List;

public interface UserService {

    User findByUsername(String userName);

    User getLoggedInUser();

    User saveNewUser(User user);

    List<User> findAll();

    void save(User user);


}//end UserService
