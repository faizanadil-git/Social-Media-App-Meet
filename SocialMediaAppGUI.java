import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SocialMediaAppGUI extends JFrame {
    private SocialMediaApp app;
    private String currentUser = null;

    // Swing components for GUI
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // New components for enhanced functionality
    private JTextArea feedTextArea;

    public SocialMediaAppGUI() {
        app = new SocialMediaApp();
        setTitle("Connect - Social Network");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Modern, professional look
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // CardLayout to switch between different screens (Login/Register, etc.)
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        // Initialize the Login and Register panels
        cardPanel.add(createLoginPanel(), "Login");
        cardPanel.add(createRegisterPanel(), "Register");
        cardPanel.add(createMainMenuPanel(), "MainMenu");

        setVisible(true);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Modern color scheme
        panel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Connect", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(24, 119, 242));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        panel.add(titleLabel, gbc);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        panel.add(usernameLabel, gbc);

        JTextField usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        JButton loginButton = createStyledButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (app.login(username, password)) {
                currentUser = username;
                cardLayout.show(cardPanel, "MainMenu");
                usernameField.setText("");
                passwordField.setText("");
            } else {
                showErrorDialog("Invalid credentials", "Login Error");
            }
        });
        panel.add(loginButton, gbc);

        JButton registerButton = createStyledButton("Register");
        gbc.gridx = 1;
        gbc.gridy = 3;
        registerButton.addActionListener(e -> cardLayout.show(cardPanel, "Register"));
        panel.add(registerButton, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Modern color scheme
        panel.setBackground(new Color(240, 242, 245));

        JLabel titleLabel = new JLabel("Create Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(24, 119, 242));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weighty = 0.2;
        panel.add(titleLabel, gbc);

        // Input fields with labels
        String[] labels = {"Username:", "Full Name:", "Email:", "Date of Birth (yyyy-MM-dd):", "Password:", "Confirm Password:", "Interests (comma-separated):"};
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            gbc.gridx = 0;
            gbc.gridy = i + 1;
            gbc.gridwidth = 1;
            panel.add(label, gbc);

            if (labels[i].contains("Password")) {
                fields[i] = new JPasswordField(15);
            } else {
                fields[i] = new JTextField(15);
            }
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            gbc.gridx = 1;
            panel.add(fields[i], gbc);
        }

        // Register button
        JButton registerButton = createStyledButton("Register");
        gbc.gridx = 0;
        gbc.gridy = labels.length + 1;
        registerButton.addActionListener(e -> {
            String username = fields[0].getText().trim();
            String fullName = fields[1].getText().trim();
            String email = fields[2].getText().trim();
            String dateOfBirth = fields[3].getText().trim();
            String password = new String(((JPasswordField)fields[4]).getPassword());
            String confirmPassword = new String(((JPasswordField)fields[5]).getPassword());

            // Split interests
            List<String> interests = Arrays.stream(
                            fields[6].getText().trim().split(",")
                    )
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            // Validation
            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() ||
                    dateOfBirth.isEmpty() || password.isEmpty()) {
                showErrorDialog("All fields are required", "Registration Error");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showErrorDialog("Passwords do not match", "Registration Error");
                return;
            }

            // Validate date format
            try {
                LocalDate.parse(dateOfBirth, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception ex) {
                showErrorDialog("Invalid date format. Use yyyy-MM-dd", "Registration Error");
                return;
            }

            // Attempt registration
            if (app.register(username, password, email, fullName, dateOfBirth, interests)) {
                JOptionPane.showMessageDialog(this,
                        "Registration successful! Please log in.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                cardLayout.show(cardPanel, "Login");
            } else {
                showErrorDialog("Username already exists", "Registration Error");
            }
        });
        panel.add(registerButton, gbc);

        // Back to Login button
        JButton backToLoginButton = createStyledButton("Back to Login");
        gbc.gridx = 1;
        gbc.gridy = labels.length + 1;
        backToLoginButton.addActionListener(e -> cardLayout.show(cardPanel, "Login"));
        panel.add(backToLoginButton, gbc);

        return panel;
    }

    private JPanel createMainMenuPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(240, 242, 245));

        // Top panel with welcome and logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser, SwingConstants.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(24, 119, 242));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = createStyledButton("Logout");
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cardPanel, "Login");
        });
        topPanel.add(logoutButton, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Left panel with friend management buttons
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.setBorder(new EmptyBorder(0, 0, 0, 15));

        String[] buttonLabels = {
                "Add Friend", "Remove Friend", "View Friends",
                "View Mutual Friends", "Friend Suggestions"
        };

        ActionListener[] listeners = {
                e -> addFriend(),
                e -> removeFriend(),
                e -> viewFriends(),
                e -> viewMutualFriends(),
                e -> viewFriendSuggestions()
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createStyledButton(buttonLabels[i]);
            button.addActionListener(listeners[i]);
            leftPanel.add(button);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Central panel for posts and feed
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // Post creation panel
        JPanel postPanel = new JPanel(new BorderLayout(10, 10));
        postPanel.setOpaque(false);
        JTextArea postTextArea = new JTextArea(3, 30);
        postTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        postTextArea.setLineWrap(true);
        postTextArea.setWrapStyleWord(true);
        JScrollPane postScrollPane = new JScrollPane(postTextArea);

        JButton submitPostButton = createStyledButton("Submit Post");
        submitPostButton.addActionListener(e -> {
            String postContent = postTextArea.getText().trim();
            if (!postContent.isEmpty()) {
                app.addPost(currentUser, postContent);
                postTextArea.setText("");
                refreshFeed();
            }
        });

        postPanel.add(new JLabel("Create a Post:", SwingConstants.LEFT), BorderLayout.NORTH);
        postPanel.add(postScrollPane, BorderLayout.CENTER);
        postPanel.add(submitPostButton, BorderLayout.SOUTH);

        centerPanel.add(postPanel, BorderLayout.NORTH);

        // Feed display
        feedTextArea = new JTextArea(15, 30);
        feedTextArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feedTextArea.setEditable(false);
        JScrollPane feedScrollPane = new JScrollPane(feedTextArea);
        centerPanel.add(feedScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Refresh feed when entering main menu
        refreshFeed();

        return mainPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(24, 119, 242));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        // Hover and click effects
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 139, 242));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(24, 119, 242));
            }
        });

        return button;
    }

    private void addFriend() {
        String friend = JOptionPane.showInputDialog(this, "Enter the username of the friend to add:");
        if (friend != null && !friend.trim().isEmpty()) {
            // Check if user exists before adding
            if (app.users.containsKey(friend)) {
                if (!friend.equals(currentUser)) {
                    app.addFriend(currentUser, friend);
                    JOptionPane.showMessageDialog(this, "Friend added successfully!");
                } else {
                    showErrorDialog("You cannot add yourself as a friend", "Add Friend Error");
                }
            } else {
                showErrorDialog("User does not exist", "Add Friend Error");
            }
        }
    }

    private void removeFriend() {
        String friend = JOptionPane.showInputDialog(this, "Enter the username of the friend to remove:");
        if (friend != null && !friend.trim().isEmpty()) {
            // Check if user exists before removing
            if (app.users.containsKey(friend)) {
                app.removeFriend(currentUser, friend);
                JOptionPane.showMessageDialog(this, "Friend removed successfully!");
            } else {
                showErrorDialog("User does not exist", "Remove Friend Error");
            }
        }
    }

    private void viewFriends() {
        List<String> friends = app.viewFriends(currentUser);
        if (friends.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no friends.");
        } else {
            JTextArea textArea = new JTextArea(15, 40);
            textArea.setEditable(false);
            for (String friend : friends) {
                textArea.append(friend + "\n");
            }
            JScrollPane scrollPane = new JScrollPane(textArea);
            JOptionPane.showMessageDialog(this, scrollPane, "Your Friends", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void viewMutualFriends() {
        String friend = JOptionPane.showInputDialog(this, "Enter the username to view mutual friends:");
        if (friend != null && !friend.trim().isEmpty()) {
            List<String> mutuals = app.viewMutuals(currentUser, friend);
            if (mutuals.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No mutual friends found.");
            } else {
                JOptionPane.showMessageDialog(this, "Mutual Friends: " + mutuals);
            }
        }
    }

    private void viewFriendSuggestions() {
        List<String> suggestions = app.suggestFriends(currentUser);
        if (suggestions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No friend suggestions.");
        } else {
            JOptionPane.showMessageDialog(this, "Friend Suggestions: " + suggestions);
        }
    }

    private void refreshFeed() {
        List<Post> posts = app.viewFeed();
        feedTextArea.setText("");
        for (Post post : posts) {
            feedTextArea.append(post.toString() + "\n\n");
        }
    }

    private void showErrorDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SocialMediaAppGUI::new);
    }
}