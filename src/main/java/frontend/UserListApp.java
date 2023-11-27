package frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserListApp extends JFrame {

    private DefaultListModel<User> userListModel;
    private JList<User> userList;

    public UserListApp() {
        setTitle("User List App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        userListModel = new DefaultListModel<>();
        userListModel.addElement(new User("User1", "user1@example.com"));
        userListModel.addElement(new User("User2", "user2@example.com"));
        userListModel.addElement(new User("User3", "user3@example.com"));

        initComponents();
    }

    private void initComponents() {
        userList = new JList<>(userListModel);
        userList.setCellRenderer(new UserListCellRenderer());
        userList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int index = userList.locationToIndex(evt.getPoint());
                if (index != -1) {
                    User user = userList.getModel().getElementAt(index);
                    user.toggleFollowStatus();
                    userList.repaint();
                }
            }
        });

        JScrollPane userListScrollPane = new JScrollPane(userList);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(userListScrollPane, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(userListScrollPane, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                                .addContainerGap())
        );
    }

    private class User {
        private String username;
        private String email;
        private boolean following;

        public User(String username, String email) {
            this.username = username;
            this.email = email;
            this.following = false;
        }

        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public boolean isFollowing() {
            return following;
        }

        public void toggleFollowStatus() {
            following = !following;
        }
    }

    private class UserListCellRenderer extends JPanel implements ListCellRenderer<User> {
        private JLabel usernameLabel;
        private JLabel emailLabel;
        private JButton followButton;

        public UserListCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            usernameLabel = new JLabel();
            emailLabel = new JLabel();
            followButton = new JButton("Follow");

            followButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int index = userList.getSelectedIndex();
                    if (index != -1) {
                        User user = userList.getModel().getElementAt(index);
                        user.toggleFollowStatus();
                        userList.repaint();
                    }
                }
            });

            add(usernameLabel, BorderLayout.NORTH);
            add(emailLabel, BorderLayout.CENTER);
            add(followButton, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User user, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            usernameLabel.setText(user.getUsername());
            emailLabel.setText("<html><font color='gray'>" + user.getEmail() + "</font></html>");
            followButton.setText(user.isFollowing() ? "Following" : "Follow");
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserListApp userListApp = new UserListApp();
            userListApp.setVisible(true);
        });
    }
}
