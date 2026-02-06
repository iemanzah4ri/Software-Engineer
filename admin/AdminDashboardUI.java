//main dashboard for system administrators
//provides navigation to user management and system settings
package admin;
import common.*;
import javax.swing.*;
import java.awt.*;

public class AdminDashboardUI extends JFrame {

    public AdminDashboardUI() {
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
            new AdminUserHubUI().setVisible(true); // RENAMED
        });

        btnListings.addActionListener(evt -> {
            this.dispose();
            new AdminListingManagerUI().setVisible(true); // RENAMED
        });

        btnMatching.addActionListener(evt -> {
            this.dispose();
            new AdminMatchingUI().setVisible(true); // RENAMED
        });

        btnAnalytics.addActionListener(evt -> {
            this.dispose();
            new AdminAnalyticsUI().setVisible(true); // RENAMED
        });
        
        btnBroadcast.addActionListener(evt -> {
            new AdminBroadcastUI().setVisible(true);
        });
        
        btnNotif.addActionListener(evt -> {
            new NotificationViewUI("admin").setVisible(true);
        });

        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginUI().setVisible(true); // RENAMED
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
}