package com.tts.techtalenttwitter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

//import javax.management.relation.Role;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    private String email;

    @NotEmpty(message = "Please provide a username")
    @Length(min = 3, message = "Your username must have at least 3 characters")
    @Length(max = 15, message = "Your username cannot have more than 15 characters")
    @Pattern(regexp = "[^\\s]+", message = "Your username cannot contain spaces")
    private String username;

    @Length(min = 5, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide a password")
//    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @NotEmpty(message = "Please provide your first name")
    private String firstName;

    @NotEmpty(message = "Please provide your last name")
    private String lastName;
    /*
    In our application, every user will have a value of 1 for active, indicating
    that their account is enabled.
     */
    private int active;

    @CreationTimestamp
    private Date createdAt;

    /*
    In order to use Spring security, we have to define roles for each
    user. To keep things simple, every user in our application will
    have the same role - "USER".
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    /*
    what will a user be related to? Other users!
     */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_follower",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followers;

    /*
    we also want to be able to easily get a list of the users that a particular
    user is following
     */
    //TODO: whats the diff in mapped by and cascade?
    @ManyToMany(mappedBy = "followers")
    private List<User> following;

    /*
     // Use this code if your lombok is not working:
    // public Long getId() {
    // return id;
    // }

    // public String getEmail() {
    // return email;
    // }

    // public void setEmail(String email) {
    // this.email = email;
    // }

    // public String getUsername() {
    // return username;
    // }

    // public void setUsername(String username) {
    // this.username = username;
    // }

    // public String getPassword() {
    // return password;
    // }

    // public void setPassword(String password) {
    // this.password = password;
    // }

    // public String getFirstName() {
    // return firstName;
    // }

    // public void setFirstName(String firstName) {
    // this.firstName = firstName;
    // }

    // public String getLastName() {
    // return lastName;
    // }

    // public void setLastName(String lastName) {
    // this.lastName = lastName;
    // }

    // public int getActive() {
    // return active;
    // }

    // public void setActive(int active) {
    // this.active = active;
    // }

    // public Date getCreatedAt() {
    // return createdAt;
    // }

    // public void setCreatedAt(Date createdAt) {
    // this.createdAt = createdAt;
    // }

    // public Set<Role> getRoles() {
    // return roles;
    // }

    // public void setRoles(Set<Role> roles) {
    // this.roles = roles;
    // }

    // public List<User> getFollowers() {
    // return followers;
    // }

    // public void setFollowers(List<User> followers) {
    // this.followers = followers;
    // }

    // public List<User> getFollowing() {
    // return following;
    // }

    // public void setFollowing(List<User> following) {
    // this.following = following;
    // }

    // @Override
    // public String toString() {
    // return "User [active=" + active + ", createdAt=" + createdAt + ", email=" +
    // email + ", firstName=" + firstName
    // + ", followers=" + followers + ", following=" + following + ", id=" + id + ",
    // lastName=" + lastName
    // + ", password=" + password + ", roles=" + roles + ", username=" + username +
    // "]";
    // }
     */

}//end User class
