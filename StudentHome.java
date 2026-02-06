import javax.swing.*;
import java.awt.*;

public class StudentHome extends JFrame {

    private String studentId;
    private String studentName;
    private boolean profileComplete = true;

    public StudentHome(String id, String name) {
        this.studentId = id;
        this.studentName = name;
        this.profileComplete = checkProfileStatus();
        
        if (profileComplete) {
            initComponents();
            setSize(800, 600);
            setLocationRelativeTo(null);
            checkNotifications();
        }
    }
    
    public StudentHome(String id) {
        this(id, "Student");
    }

    @Override
    public void setVisible(boolean b) {
        if (b && !profileComplete) {
            return;
        }
        super.setVisible(b);
    }
    
    private void checkNotifications() {
        if (NotificationHelper.hasUnreadNotifications(studentId)) {
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
        for (String[] notif : NotificationHelper.getNotifications(studentId)) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }
    
    private boolean checkProfileStatus() {
        String[] data = DBHelper.getUserById(studentId);
        if (data != null) {
            boolean emailMissing = data.length <= 6 || data[6].trim().isEmpty() || data[6].equalsIgnoreCase("N/A");
            boolean contactMissing = data.length <= 7 || data[7].trim().isEmpty() || data[7].equalsIgnoreCase("N/A");
            boolean addressMissing = data.length <= 8 || data[8].trim().isEmpty() || data[8].equalsIgnoreCase("N/A");

            if (emailMissing || contactMissing || addressMissing) {
                JOptionPane.showMessageDialog(null, 
                    "Welcome! You must complete your profile details to proceed.", 
                    "Profile Incomplete", 
                    JOptionPane.WARNING_MESSAGE);
                
                new StudentProfile(studentId, studentName, true).setVisible(true);
                return false; 
            }
        }
        return true;
    }

    private boolean isInterning() {
        String[] data = DBHelper.getUserById(studentId);
        return data != null && data.length > 9 && "Placed".equalsIgnoreCase(data[9]);
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Dashboard - " + studentName);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome " + studentName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(8, 1, 10, 10)); 
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnUpdateProfile = new JButton("Update Profile");
        btnUpdateProfile.addActionListener(e -> new StudentProfile(studentId, studentName, false).setVisible(true));

        JButton btnViewListings = new JButton("View Internship Listings");
        btnViewListings.addActionListener(e -> {
            if (isInterning()) {
                JOptionPane.showMessageDialog(this, "You are already placed in an internship.\nYou cannot view new listings.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            } else {
                new StudentViewListingsUI(studentId).setVisible(true);
            }
        });

        JButton btnSubmitLog = new JButton("Submit Daily Logbook");
        btnSubmitLog.addActionListener(e -> {
            if (isInterning()) {
                new StudentLogbook(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnAttendance = new JButton("Submit Attendance Record");
        btnAttendance.addActionListener(e -> {
            if (isInterning()) {
                new SubmitAttendance(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnTrack = new JButton("Track Application Status");
        btnTrack.addActionListener(e -> new StudentTrack(studentId, studentName).setVisible(true));

        JButton btnProgress = new JButton("View Internship Progress and Feedback");
        btnProgress.addActionListener(e -> {
            if (isInterning()) {
                new StudentProgress(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton btnNotif = new JButton("View Notifications");
        btnNotif.addActionListener(e -> new NotificationViewUI(studentId).setVisible(true));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        buttons.add(btnUpdateProfile);
        buttons.add(btnViewListings);
        buttons.add(btnTrack);
        buttons.add(btnSubmitLog);
        buttons.add(btnAttendance);
        buttons.add(btnProgress);
        buttons.add(btnNotif);
        buttons.add(btnLogout);

        add(buttons, BorderLayout.CENTER);
    }
}
