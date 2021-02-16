package com.tts.techtalenttwitter.service;

import com.tts.techtalenttwitter.model.User;

import java.util.List;

public interface UserService {

    User findByUsername(String userName);

    List<User> findAll();

    void save(User user);

    User saveNewUser(User user);

    User getLoggedInUser();


}//end UserService
