package com.tts.techtalenttwitter.service;


import com.tts.techtalenttwitter.model.Tag;
import com.tts.techtalenttwitter.model.Tweet;
import com.tts.techtalenttwitter.model.TweetDisplay;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.TagRepository;
import com.tts.techtalenttwitter.repository.TweetRepository;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TweetServiceImpl implements TweetService {

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private TagRepository tagRepository;

    public TweetServiceImpl(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<TweetDisplay> findAll() {
        List<Tweet> tweets = tweetRepository.findAllByOrderByCreatedAtDesc();
        return formatTweets(tweets);
    }

    @Override
    public List<TweetDisplay> findAllByUser(User user) {
        List<Tweet> tweets = tweetRepository.findAllByUserOrderByCreatedAtDesc(user);
        return formatTweets(tweets);
    }

    @Override
    public List<TweetDisplay> findAllByUsers(List<User> users) {
        List<Tweet> tweets = tweetRepository.findAllByUserInOrderByCreatedAtDesc(users);
        return formatTweets(tweets);
    }

    @Override
    public List<TweetDisplay> findAllWithTag(String tag) {
        List<Tweet> tweets = tweetRepository.findByTags_PhraseOrderByCreatedAtDesc(tag);
        return formatTweets(tweets);
    }
    //TODO: slide 62 says each of our service methods that load tweets from the repo add
    // the formatTweets meth, but I only see it on 2 of the methods

    @Override
    public void handleTags(Tweet tweet) {
        List<Tag> tags = new ArrayList<Tag>();
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(tweet.getMessage());
        while (matcher.find()) {
            String phrase = matcher.group().substring(1).toLowerCase();
            Tag tag = tagRepository.findByPhrase(phrase);
            if (tag == null) {
                tag = new Tag();
                tag.setPhrase(phrase);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        tweet.setTags(tags);
    }

    @Override
    public void save(Tweet tweet) {
        handleTags(tweet);
        tweetRepository.save(tweet);
    }

    /* this is the orignal. new changes in method below
    public List<Tweet> formatTweets(List<Tweet> tweets) {
        addTagLinks(tweets);
        shortenLinks(tweets);
        return tweets;
    }
    */

    /*
    modify our formatTweets method in TweetService. First, we need to change
    the method signature so that it now returns a list of TweetDisplay objects
    instead of Tweet objects.
     */
    public List<TweetDisplay> formatTweets(List<Tweet> tweets) {
        addTagLinks(tweets);
        shortenLinks(tweets);
        List<TweetDisplay> displayTweets = formatTimestamps(tweets);
        return displayTweets;
    }

    /*
    takes in a list of Tweets and returns back a list of TweetDisplay
     */
    private List<TweetDisplay> formatTimestamps(List<Tweet> tweets) {
        List<TweetDisplay> response = new ArrayList<>();
        PrettyTime prettyTime = new PrettyTime();
        SimpleDateFormat simpleDate = new SimpleDateFormat("M/d/yy");
        Date now = new Date();
        for (Tweet tweet : tweets) {
            TweetDisplay tweetDisplay = new TweetDisplay();
            tweetDisplay.setUser(tweet.getUser());
            tweetDisplay.setMessage(tweet.getMessage());
            tweetDisplay.setTags(tweet.getTags());
            long diffInMillies = Math.abs(now.getTime() - tweet.getCreatedAt().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff > 3) {
                tweetDisplay.setDate(simpleDate.format(tweet.getCreatedAt()));
            } else {
                tweetDisplay.setDate(prettyTime.format(tweet.getCreatedAt()));
            }
            response.add(tweetDisplay);
        }
        return response;
    }

    private void addTagLinks(List<Tweet> tweets) {
        Pattern pattern = Pattern.compile("#\\w+");
        for (Tweet tweet : tweets) {
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            Set<String> tags = new HashSet<String>();
            while (matcher.find()) {
                tags.add(matcher.group());
            }
            for (String tag : tags) {
                message = message.replaceAll(tag,
                        "<a class=\"tag\" href=\"/tweets/" + tag.substring(1).toLowerCase() + "\">" + tag + "</a>");
            }
            tweet.setMessage(message);
        }
    }

    public void shortenLinks(List<Tweet> tweets) {
        /*
        takes in a list of tweets and changes the message for each one
         */
        Pattern pattern = Pattern.compile("https?[^ ]+");

        for (Tweet tweet : tweets) {
            /*
            we extract the message from the tweet object, apply the regular expression to find all links,
            and then iterate through the found links one by one
             */
            String message = tweet.getMessage();
            Matcher matcher = pattern.matcher(message);
            while (matcher.find()) {
                /*
                For each link, we begin by shortening it if necessary.
                 */
                String link = matcher.group();
                String shortenedLink = link;
                if (link.length() > 23) {
                    shortenedLink = link.substring(0, 20) + "...";
                    /*
                    Then we modified the message to include the actual <a href> tag
                     */
                    message = message.replace(link,
                            "<a class=\"tag\" href=\"" + link + "\" target=\"_blank\">" + shortenedLink + "</a>");
                }
                /*
                Finally, we set the tweet message to the modified message, which now includes links
                 */
                tweet.setMessage(message);
            }
        }
    }


}//end TweetServiceImpl class
