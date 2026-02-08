package admin;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;

// main menu for admin
public class AdminDashboardUI extends JFrame {

    // constructor
    public AdminDashboardUI() {
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(null);
        // check for alerts on load
        checkNotifications();
    }
    
    // check if admin has messages
    private void checkNotifications() {
        if (NotificationHelper.hasUnreadNotifications("admin")) {
            // run later to avoid ui lag
            SwingUtilities.invokeLater(() -> {
                int unreadCount = countUnreadNotifications();
                JOptionPane.showMessageDialog(this, 
                    "You have " + unreadCount + " unread system notification(s)!", 
                    "New Notifications", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    // helper to count unread msgs
    private int countUnreadNotifications() {
        int count = 0;
        for (String[] notif : NotificationHelper.getNotifications("admin")) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }

    // setup buttons
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Admin Dashboard");
        setLayout(new BorderLayout());

        // welcome header
        JPanel headerPanel = new JPanel();
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel lblWelcome = new JLabel("Welcome Admin");
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        headerPanel.add(lblWelcome);

        add(headerPanel, BorderLayout.NORTH);

        // button container
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); 
        // padding to make buttons smaller width
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150)); 

        // define buttons
        JButton btnManageUsers = new JButton("Register & Manage User Account");
        JButton btnListings = new JButton("Manage Company Account & Internship Listings");
        JButton btnMatching = new JButton("Internship Matching");
        JButton btnAnalytics = new JButton("View System Analytics & Report");
        JButton btnBroadcast = new JButton("Broadcast System Notification");
        JButton btnNotif = new JButton("View Admin Notifications");
        JButton btnLogout = new JButton("Log Out");

        // actions for buttons
        
        // open user management
        btnManageUsers.addActionListener(evt -> {
            this.dispose(); 
            new AdminUserHubUI().setVisible(true); 
        });

        // open listing management
        btnListings.addActionListener(evt -> {
            this.dispose();
            new AdminListingManagerUI().setVisible(true); 
        });

        // open matching screen
        btnMatching.addActionListener(evt -> {
            this.dispose();
            new AdminMatchingUI().setVisible(true); 
        });

        // open analytics
        btnAnalytics.addActionListener(evt -> {
            this.dispose();
            new AdminAnalyticsUI().setVisible(true); 
        });
        
        // open broadcast tool
        btnBroadcast.addActionListener(evt -> {
            new AdminBroadcastUI().setVisible(true);
        });
        
        // view notifications
        btnNotif.addActionListener(evt -> {
            new NotificationViewUI("admin").setVisible(true);
        });

        // logout
        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginUI().setVisible(true); 
        });

        // add buttons to panel
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