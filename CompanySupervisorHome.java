import javax.swing.*;
import java.awt.*;

public class CompanySupervisorHome extends JFrame {
    private String supervisorId;
    private String supervisorName;
    private String companyName;

    public CompanySupervisorHome(String id) {
        this.supervisorId = id;
        String[] data = DBHelper.getUserById(id);
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
        setTitle("Company Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("Welcome " + supervisorName + " (" + companyName + ")");
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(lbl);
        add(header, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnManage = new JButton("Manage Applications");
        JButton btnEval = new JButton("Submit Evaluation");
        JButton btnVer = new JButton("Verify Logbook and Attendance");
        JButton btnViewS = new JButton("View Assigned Student Profile");
        JButton btnCreate = new JButton("Manage Listings");
        JButton btnNotif = new JButton("View Notifications");
        JButton btnOut = new JButton("Logout");

        btnManage.addActionListener(e -> new CompanyManageApplicationsUI(supervisorId, companyName).setVisible(true));
        btnEval.addActionListener(e -> new CompanyEvaluation(supervisorId).setVisible(true));
        btnVer.addActionListener(e -> new VerifyLogbookAttd(supervisorId).setVisible(true));
        btnViewS.addActionListener(e -> new ViewStudentProfileUI(supervisorId).setVisible(true));
        btnCreate.addActionListener(e -> new CreateListingUI(supervisorId, companyName).setVisible(true));
        btnNotif.addActionListener(e -> new NotificationViewUI(supervisorId).setVisible(true));
        btnOut.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

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
