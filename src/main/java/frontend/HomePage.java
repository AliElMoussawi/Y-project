package frontend;

import backend.Client;
import backend.dto.PostDTO;
import backend.models.database.User;
import backend.models.protocol.RequestObject;
import backend.models.protocol.ResponseObject;
import backend.services.UserServiceImpl;
import backend.utils.enums.Action;
import backend.utils.enums.Method;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static backend.Client.createRequest;

public class HomePage extends JFrame {

    private JTextArea tweetTextArea;
    private DefaultListModel<Tweet> tweetListModel;
    private JList<Tweet> tweetList;
    Client client;
    String token;
    String username;

    User user;
    public HomePage(Client client, String username, String token) {
        this.client = client;
        this.token = token;
        this.username = username;
        //get user by username
        UserServiceImpl userService = new UserServiceImpl();
        user = userService.getUserByUsernameOrEmail(username);
        setTitle("Twitter Home Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        tweetTextArea = new JTextArea(5, 30);
        tweetTextArea.setLineWrap(true);

        JButton tweetButton = new JButton("Tweet");
        tweetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tweetContent = tweetTextArea.getText();
                if (!tweetContent.isEmpty()) {
                    // create a post action here and send it as broadcastObject
                    createPost(user.getId(), tweetContent);
                    Tweet tweet = new Tweet(username, user.getEmail(), tweetContent);
                    tweetListModel.addElement(tweet);
                    tweetTextArea.setText("");
                }
            }
        });

        tweetListModel = new DefaultListModel<>();
        tweetList = new JList<>(tweetListModel);
        tweetList.setLayoutOrientation(JList.VERTICAL);
        tweetList.setCellRenderer(new TweetListCellRenderer());

        tweetList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int index = tweetList.locationToIndex(evt.getPoint());
                if (index != -1) {
                    Tweet selectedTweet = tweetListModel.getElementAt(index);
                    // Navigate to user page (replace this with your logic)
                    navigateToUserPage(selectedTweet.getUserName(), selectedTweet.getEmail());
                }
            }
        });
        JButton userListButton = new JButton("User List");
        userListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get all users and pass then as argument to the UserListApp
                UserListApp dialog = new UserListApp();
                dialog.pack();
                dialog.setVisible(true);
            }
        });

        JScrollPane tweetScrollPane = new JScrollPane(tweetList);
        tweetScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(tweetScrollPane)
                                        .addComponent(tweetButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(userListButton,GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tweetTextArea))

                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(tweetScrollPane, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tweetButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tweetTextArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(userListButton)
                                .addContainerGap())
        );
    }

    private void navigateToUserPage(String userName, String email) {
        UserPage dialog = new UserPage(userName, email);
        dialog.pack();
        dialog.setVisible(true);
//        System.exit(0);
    }

    private class Tweet {
        private String userName;
        private String email;
        private String content;

        public Tweet(String userName, String email, String content) {
            this.userName = userName;
            this.email = email;
            this.content = content;
        }

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getContent() {
            return content;
        }
    }

    private class TweetListCellRenderer extends JPanel implements ListCellRenderer<Tweet> {
        private JLabel userNameLabel;
        private JLabel emailLabel;
        private JLabel contentLabel;

        public TweetListCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));

            userNameLabel = new JLabel();
            emailLabel = new JLabel();
            contentLabel = new JLabel();

            add(userNameLabel, BorderLayout.NORTH);
            add(emailLabel, BorderLayout.CENTER);
            add(contentLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Tweet> list, Tweet tweet, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            userNameLabel.setText("<html><strong><font color='#1E90FF'>" + tweet.getUserName() + "</font></strong></html>");
            emailLabel.setText("<html><font color='gray'>" + tweet.getEmail() + "</font></html>");
            contentLabel.setText("<html><body style='width: 300px; padding: 5px;'>" + tweet.getContent() + "</body></html>");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

            return this;
        }
    }

    public ResponseObject createPost(long userId, String context){
        RequestObject requestObject = new RequestObject();
        requestObject.setAction(Action.UPLOAD_POST);
        requestObject.setMethod(Method.POST);
        requestObject.setToken(token);
        PostDTO postDTO = new PostDTO();
        postDTO.setUserId(userId);
        postDTO.setContent(context);
        requestObject.setObject(postDTO);
        return createRequest(client, requestObject);
    }
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 9991, 1);
        HomePage twitterHomePage = new HomePage(client,"asdasd1", "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhc2Rhc2QxIiwiaWF0IjoxNzAxMTMwMDk0LCJleHAiOjE3MDExMzM2OTR9.uszrR1bx8gcz7lU3H2IosgWOwlrzSWXyA6JbVYh9ZM3K1ZGswNbjBVVKninlkM6dWnjrtDGcK8JPMAuxm_8iEw");
        twitterHomePage.setVisible(true);
    }
}
