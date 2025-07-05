// SocialMediaApp.java
import java.io.*;
import java.util.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// User.java


class User implements Serializable {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private Set<String> friends;
    private List<String> interests;

    public User(String username, String password, String email, String fullName, String dateOfBirth, List<String> interests) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.friends = new HashSet<>();
        this.interests = interests;
        this.dateOfBirth = LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Set<String> getFriends() {
        return friends;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void addFriend(String friend) {
        friends.add(friend);
    }

    public void removeFriend(String friend) {
        friends.remove(friend);
    }

    public void addInterest(String interest) {
        interests.add(interest);
    }

    public String getEmail() {
        return email;
    }
}

// Post.java


class Post implements Serializable {
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public Post(String username, String content) {
        this.username = username;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getContent() {
        return content;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return username + " (" + getFormattedTimestamp() + "): " + content;
    }
}

// Main.java for console-based interface (optional)


public class Main {
    public static void main(String[] args) {
        SocialMediaAppGUI app = new SocialMediaAppGUI();
    }
}