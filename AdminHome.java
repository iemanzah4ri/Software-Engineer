import javax.swing.*;
import java.awt.*;

public class AdminHome extends JFrame {

    public AdminHome() {
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        checkNotifications();
    }
    
    private void checkNotifications() {
        if (NotificationHelper.hasUnreadNotifications("admin")) {
            SwingUtilities.invokeLater(() -> {
                int unreadCount = countUnreadNotifications();
                JOptionPane.showMessageDialog(this, 
                    "You have " + unreadCount + " unread system notification(s)!", 
                    "New Notifications", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    private int countUnreadNotifications() {
        int count = 0;
        for (String[] notif : NotificationHelper.getNotifications("admin")) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel lblWelcome = new JLabel("Welcome Admin");
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        headerPanel.add(lblWelcome);

        add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150)); 

        JButton btnManageUsers = new JButton("Register & Manage User Account");
        JButton btnListings = new JButton("Manage Company Account & Internship Listings");
        JButton btnMatching = new JButton("Internship Matching");
        JButton btnAnalytics = new JButton("View System Analytics & Report");
        JButton btnBroadcast = new JButton("Broadcast System Notification");
        JButton btnNotif = new JButton("View Admin Notifications");
        JButton btnLogout = new JButton("Log Out");

        btnManageUsers.addActionListener(evt -> {
            this.dispose(); 
            new UserManagementHub().setVisible(true);
        });

        btnListings.addActionListener(evt -> {
            this.dispose();
            new ManageListingsUI().setVisible(true);
        });

        btnMatching.addActionListener(evt -> {
            this.dispose();
            new MatchingUI().setVisible(true);
        });

        btnAnalytics.addActionListener(evt -> {
            this.dispose();
            new AnalyticsUI().setVisible(true);
        });
        
        btnBroadcast.addActionListener(evt -> {
            new AdminBroadcastUI().setVisible(true);
        });
        
        btnNotif.addActionListener(evt -> {
            new NotificationViewUI("admin").setVisible(true);
        });

        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });

        buttonPanel.add(btnManageUsers);
        buttonPanel.add(btnListings);
        buttonPanel.add(btnMatching);
        buttonPanel.add(btnAnalytics);
        buttonPanel.add(btnBroadcast);
        buttonPanel.add(btnNotif);
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new AdminHome().setVisible(true));
    }
}
