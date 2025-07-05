import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class SocialMediaApp implements Serializable {
    public Map<String, User> users;
    private List<Post> posts;
    private static final String DATA_FILE = "data.txt";

    public SocialMediaApp() {
        users = new HashMap<>();
        posts = new ArrayList<>();
        loadData();
    }

    public boolean register(String username, String password, String email, String fullName, String dateOfBirth, List<String> interests) {
        if (users.containsKey(username)) {
            return false;
        }
        users.put(username, new User(username, password, email, fullName, dateOfBirth, interests));
        saveData();
        return true;
    }

    public boolean login(String username, String password) {
        User user = users.get(username);
        return user != null && user.checkPassword(password);
    }

    public void addFriend(String user, String friend) {
        if (users.containsKey(user) && users.containsKey(friend)) {
            users.get(user).addFriend(friend);
            users.get(friend).addFriend(user);
            saveData();
        }
    }

    public void removeFriend(String user, String friend) {
        if (users.containsKey(user) && users.containsKey(friend)) {
            users.get(user).removeFriend(friend);
            users.get(friend).removeFriend(user);
            saveData();
        }
    }

    public List<String> viewMutuals(String user, String friend) {
        List<String> mutuals = new ArrayList<>();
        if (users.containsKey(user) && users.containsKey(friend)) {
            for (String f : users.get(user).getFriends()) {
                if (users.get(friend).getFriends().contains(f)) {
                    mutuals.add(f);
                }
            }
        }
        return mutuals;
    }

    public List<String> suggestFriends(String user) {
        List<String> suggestions = new ArrayList<>();
        if (users.containsKey(user)) {
            Set<String> userFriends = users.get(user).getFriends();
            List<String> userInterests = users.get(user).getInterests();

            for (String friend : userFriends) {
                for (String friendsFriend : users.get(friend).getFriends()) {
                    if (!userFriends.contains(friendsFriend) && !friendsFriend.equals(user)) {
                        User potentialFriend = users.get(friendsFriend);
                        if (!Collections.disjoint(potentialFriend.getInterests(), userInterests)) {
                            suggestions.add(friendsFriend);
                        }
                    }
                }
            }
        }
        return suggestions;
    }

    public void addPost(String username, String content) {
        posts.add(new Post(username, content));
        saveData();
    }

    public List<Post> viewFeed() {
        return new ArrayList<>(posts);
    }

    public List<String> viewFriends(String username) {
        List<String> friendsDetails = new ArrayList<>();
        if (users.containsKey(username)) {
            for (String friend : users.get(username).getFriends()) {
                User friendUser = users.get(friend);
                String mutuals = "Mutual Friends: " + viewMutuals(username, friend);
                friendsDetails.add(friendUser.getFullName() + " (Interests: " + friendUser.getInterests() + ") " + mutuals);
            }
        }
        return friendsDetails;
    }

    private void saveData() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
            out.writeObject(posts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) in.readObject();
            posts = (List<Post>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}