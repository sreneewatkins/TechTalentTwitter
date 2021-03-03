package com.tts.techtalenttwitter.service;

import com.tts.techtalenttwitter.model.Tweet;
import com.tts.techtalenttwitter.model.TweetDisplay;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.TweetRepository;

import java.util.List;

public interface TweetService {

    List<TweetDisplay> findAll();

    List<TweetDisplay> findAllByUser(User user);

    List<TweetDisplay> findAllByUsers(List<User> users);

    List<TweetDisplay> findAllWithTag(String tag);

    List<TweetDisplay> formatTweets(List<Tweet> tweets);

    void shortenLinks(List<Tweet> tweets);

    void handleTags(Tweet tweet);

    void save(Tweet tweet);

}//end TweetService
