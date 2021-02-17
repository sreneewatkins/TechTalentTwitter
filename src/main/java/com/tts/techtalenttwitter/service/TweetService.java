package com.tts.techtalenttwitter.service;

import com.tts.techtalenttwitter.model.Tweet;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.TweetRepository;

import java.util.List;

public interface TweetService {

    List<Tweet> findAll();

    List<Tweet> findAllByUser(User user);

    List<Tweet> findAllByUsers(List<User> users);

    List<Tweet> findAllWithTag(String tag);

    void handleTags(Tweet tweet);

    void save(Tweet tweet);

}//end TweetService
