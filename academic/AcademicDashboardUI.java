//main dashboard for academic supervisors
//shows alerts for assigned students
package academic;
import common.*;
import javax.swing.*;
import java.awt.*;

public class AcademicDashboardUI extends JFrame {
    private String supervisorId;
    private String supervisorName;

    public AcademicDashboardUI(String id) {
        this.supervisorId = id;
        String[] d = DatabaseHelper.getUserById(id); // UPDATED
        this.supervisorName = (d!=null)?d[3]:"Academic Supervisor";
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
        setTitle("Academic Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome " + supervisorName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(6, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton b1 = new JButton("View Assigned Student Profile");
        b1.addActionListener(e -> new SupervisorStudentProfileUI(supervisorId).setVisible(true)); // RENAMED (Shared)

        JButton b2 = new JButton("View Student Logbook");
        b2.addActionListener(e -> new AcademicLogReviewUI().setVisible(true)); // RENAMED
        
        JButton b3 = new JButton("Submit Academic Evaluation");
        b3.addActionListener(e -> new AcademicEvaluationUI(supervisorId).setVisible(true)); // RENAMED

        JButton b4 = new JButton("View Internship Progress Dashboard");
        b4.addActionListener(e -> new AcademicProgressUI(supervisorId).setVisible(true)); // RENAMED

        JButton b5 = new JButton("View Notifications");
        b5.addActionListener(e -> new NotificationViewUI(supervisorId).setVisible(true));

        JButton bOut = new JButton("Logout");
        bOut.addActionListener(e -> { dispose(); new LoginUI().setVisible(true); }); // RENAMED

        buttons.add(b1); buttons.add(b2); buttons.add(b3); 
        buttons.add(b4); buttons.add(b5); buttons.add(bOut);

        add(buttons, BorderLayout.CENTER);
    }
}