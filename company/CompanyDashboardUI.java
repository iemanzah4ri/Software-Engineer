//main dashboard for company supervisors
//provides access to recruitment and management tools
package company;
import common.*;
import javax.swing.*;
import java.awt.*;

public class CompanyDashboardUI extends JFrame {
    private String supervisorId;
    private String supervisorName;
    private String companyName;

    public CompanyDashboardUI(String id) {
        this.supervisorId = id;
        String[] data = DatabaseHelper.getUserById(id); // UPDATED
        if (data != null) {
            this.supervisorName = data[3];
            this.companyName = (data.length > 8) ? data[8] : "Unknown";
        }
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
        checkNotifications();
    }
    
    private void checkNotifications() {
        if (NotificationHelper.hasUnreadNotifications(supervisorId)) {
            SwingUtilities.invokeLater(() -> {
                int unreadCount = countUnreadNotifications();
                JOptionPane.showMessageDialog(this, 
                    "You have " + unreadCount + " unread notification(s)!", 
                    "New Notifications", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    private int countUnreadNotifications() {
        int count = 0;
        for (String[] notif : NotificationHelper.getNotifications(supervisorId)) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Company Supervisor Dashboard - " + companyName);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome " + supervisorName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnManage = new JButton("Manage Applications");
        JButton btnEval = new JButton("Submit Evaluation");
        JButton btnVer = new JButton("Verify Logbook and Attendance");
        JButton btnViewS = new JButton("View Assigned Student Profile");
        JButton btnCreate = new JButton("Manage Listings");
        JButton btnNotif = new JButton("View Notifications");
        JButton btnOut = new JButton("Logout");

        btnManage.addActionListener(e -> new CompanyAppReviewUI(supervisorId, companyName).setVisible(true)); // RENAMED
        btnEval.addActionListener(e -> new CompanyEvaluationUI(supervisorId).setVisible(true)); // RENAMED
        btnVer.addActionListener(e -> new CompanyVerifyRecordsUI(supervisorId).setVisible(true)); // RENAMED
        btnViewS.addActionListener(e -> new SupervisorStudentProfileUI(supervisorId).setVisible(true)); // RENAMED (Shared)
        btnCreate.addActionListener(e -> new CompanyCreateJobUI("Company", companyName).setVisible(true)); // RENAMED
        btnNotif.addActionListener(e -> new NotificationViewUI(supervisorId).setVisible(true));
        btnOut.addActionListener(e -> { dispose(); new LoginUI().setVisible(true); }); // RENAMED

        btnPanel.add(btnManage);
        btnPanel.add(btnEval);
        btnPanel.add(btnVer);
        btnPanel.add(btnViewS);
        btnPanel.add(btnCreate);
        btnPanel.add(btnNotif);
        btnPanel.add(btnOut);
        add(btnPanel, BorderLayout.CENTER);
    }
}