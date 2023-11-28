package frontend;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserPage extends JFrame {

    private JPanel userPanel;
    private JLabel userNameLabel;
    private JLabel emailLabel;
    private JList<String> postList;

    public UserPage(String userName, String userEmail) {
        setTitle("User Page");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents(userName, userEmail);
    }

    private void initComponents(String userName, String userEmail) {
        userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Styling for user information
        Font userNameFont = new Font("Arial", Font.BOLD, 24);
        Font emailFont = new Font("Arial", Font.ITALIC, 16);

        userNameLabel = new JLabel( userName);
        userNameLabel.setFont(userNameFont);
        userNameLabel.setForeground(new Color(30, 144, 255)); // Dodger Blue

        emailLabel = new JLabel(userEmail);
        emailLabel.setFont(emailFont);
        emailLabel.setForeground(new Color(169, 169, 169)); // Dark Gray

        postList = new JList<>();
        postList.setLayoutOrientation(JList.VERTICAL);
        postList.setCellRenderer(new PostListCellRenderer());

        JScrollPane postScrollPane = new JScrollPane(postList);
        postScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(userPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(postScrollPane))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(userPanel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(postScrollPane, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                .addContainerGap())
        );

        userPanel.add(userNameLabel);
        userPanel.add(emailLabel);
    }

    public void setPosts(List<String> posts) {
        DefaultListModel<String> postListModel = new DefaultListModel<>();
        for (String post : posts) {
            postListModel.addElement(post);
        }
        postList.setModel(postListModel);
    }

    private class PostListCellRenderer extends JPanel implements ListCellRenderer<String> {
        private JLabel postLabel;
        private JLabel timestampLabel;

        public PostListCellRenderer() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY));

            postLabel = new JLabel();
            postLabel.setForeground(Color.DARK_GRAY);

            timestampLabel = new JLabel();
            timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            timestampLabel.setForeground(new Color(169, 169, 169)); // Dark Gray

            add(postLabel, BorderLayout.CENTER);
            add(timestampLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends String> list, String post, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            postLabel.setText("<html><body style='width: 300px; padding: 5px;'>" + post + "</body></html>");

            // Add timestamp below each post
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            timestampLabel.setText("<html><body style='padding: 2px;'>" + timestamp + "</body></html>");

            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserPage userPage = new UserPage("John Doe", "john.doe@example.com");
            userPage.setPosts(List.of(
                    "This is a great post!",
                    "Another interesting post.",
                    "Enjoying the day!"));
            userPage.setVisible(true);
        });
    }
}
