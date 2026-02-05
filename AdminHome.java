import javax.swing.*;
import java.awt.*;

public class AdminHome extends JFrame {

    public AdminHome() {
        initComponents();
        setSize(800, 500);
        setLocationRelativeTo(null);
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
        buttonPanel.setLayout(new GridLayout(5, 1, 10, 10)); // 5 rows, 10px spacing
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150)); // Margins

        JButton btnManageUsers = new JButton("Register & Manage User Account");
        JButton btnListings = new JButton("Manage Company Account & Internship Listings");
        JButton btnMatching = new JButton("Internship Matching");
        JButton btnAnalytics = new JButton("View System Analytics & Report");
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

        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });

        buttonPanel.add(btnManageUsers);
        buttonPanel.add(btnListings);
        buttonPanel.add(btnMatching);
        buttonPanel.add(btnAnalytics);
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new AdminHome().setVisible(true));
    }
}