package com.tts.techtalenttwitter.controller;

import com.tts.techtalenttwitter.model.Tag;
import com.tts.techtalenttwitter.model.Tweet;
import com.tts.techtalenttwitter.model.TweetDisplay;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.TagRepository;
import com.tts.techtalenttwitter.service.TweetService;
import com.tts.techtalenttwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TweetController {

    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @Autowired
    private TagRepository tagRepository;

    /*
    The first will allow us to get all tweets, and will accept a
    GET request to either /tweets or /.
     */
    /*
    @GetMapping(value= {"/tweets", "/"})
    public String getFeed(Model model){
        List<TweetDisplay> tweets = tweetService.findAll();
        model.addAttribute("tweetList", tweets);
        return "feed";
    }
    */

    /*
    add the option to allow the user to switch back and forth between
    seeing this subset of tweets or all tweets
    We will accomplish this by using a query parameter in our request,
    so that when a request is made to /tweets, a value of following
    can be used for the parameter filter. This looks like
    /tweets?filter=following.
     */
    @GetMapping(value = { "/tweets", "/" })
    public String getFeed(@RequestParam(value = "filter", required = false) String filter, Model model) {
        User loggedInUser = userService.getLoggedInUser();
        List<TweetDisplay> tweets = new ArrayList<>();

        /*
        If no value is specified for the the filter, we're going to treat
        that as being a filter for all tweets with this if statement
         */
        if (filter == null) {
            filter = "all";
        }

        /*
        If a value of following is specified, we're going to call
        TweetService and only get tweets from users that the user is
        following, as well as add the filter value to the model.
         */
        if (filter.equalsIgnoreCase("following")) {
            List<User> following = loggedInUser.getFollowing();
            tweets = tweetService.findAllByUsers(following);
            model.addAttribute("filter", "following");
        }
        /*
        Else, we default to getting all tweets, still adding the filter
        value to the model.
         */
        else {
            tweets = tweetService.findAll();
            model.addAttribute("filter", "all");
        }
        /*
        Finally, in either case, we add the list of tweets to the model
        and return the html page
         */
        model.addAttribute("tweetList", tweets);
        return "feed";
    }

    /*
    allows us to serve up the 'new tweet' page, newTweet.html.
     */
    @GetMapping(value = "/tweets/new")
    public String getTweetForm(Model model) {
        model.addAttribute("tweet", new Tweet());
        return "newTweet";
    }

    /*
    a method that is called whenever we make a GET request to /tweets/{tag}
     */
    @GetMapping(value = "/tweets/{tag}")
    public String getTweetsByTag(@PathVariable(value="tag") String tag, Model model) {
        List<TweetDisplay> tweets = tweetService.findAllWithTag(tag);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("tag", tag);
        return "taggedTweets";
    }

    /*
    handles the form submission from the 'new tweet' page. This
    method gets the logged in user and associates them with the
    tweet!
     */
    @PostMapping(value = "/tweets")
    public String submitTweetForm(@Valid Tweet tweet, BindingResult bindingResult, Model model) {
        User user = userService.getLoggedInUser();
        if (!bindingResult.hasErrors()) {
            tweet.setUser(user);
            tweetService.save(tweet);
            model.addAttribute("successMessage", "Tweet successfully created!");
            model.addAttribute("tweet", new Tweet());
        }
        return "newTweet";
    }

    @GetMapping(value = "/tags")
    public String getTags(Model model) {
        List<Tag> tag = (List<Tag>)tagRepository.findAll();
        model.addAttribute("tagList", tag);
        return "tags";
    }

}//end TweetController class
