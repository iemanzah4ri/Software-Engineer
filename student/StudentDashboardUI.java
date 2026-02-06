//main dashboard for student users
//checks profile completion status on login
package student;
import common.*;
import javax.swing.*;
import java.awt.*;

public class StudentDashboardUI extends JFrame {

    private String studentId;
    private String studentName;
    private boolean profileComplete = true;

    public StudentDashboardUI(String id, String name) {
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
    
    public StudentDashboardUI(String id) {
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

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Dashboard - " + studentName);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + studentName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(4, 2, 15, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnUpdateProfile = new JButton("Update Profile / Upload Resume");
        btnUpdateProfile.addActionListener(e -> new StudentProfileUI(studentId, studentName, false).setVisible(true)); // RENAMED

        JButton btnViewListings = new JButton("View & Apply for Internships");
        btnViewListings.addActionListener(e -> new StudentJobBoardUI(studentId).setVisible(true)); // RENAMED
        
        JButton btnSubmitLog = new JButton("Daily Logbook");
        btnSubmitLog.addActionListener(e -> {
            if (isInterning()) {
                new StudentLogbookUI(studentId, studentName).setVisible(true); // RENAMED
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnAttendance = new JButton("Attendance (Clock In/Out)");
        btnAttendance.addActionListener(e -> {
            if (isInterning()) {
                new StudentAttendanceUI(studentId, studentName).setVisible(true); // RENAMED
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        JButton btnTrack = new JButton("Track Application Status");
        btnTrack.addActionListener(e -> new StudentTrackerUI(studentId, studentName).setVisible(true)); // RENAMED

        JButton btnProgress = new JButton("View Internship Progress and Feedback");
        btnProgress.addActionListener(e -> {
            if (isInterning()) {
                new StudentProgressUI(studentId, studentName).setVisible(true); // RENAMED
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton btnNotif = new JButton("View Notifications");
        btnNotif.addActionListener(e -> new NotificationViewUI(studentId).setVisible(true));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginUI().setVisible(true); // RENAMED
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
    
    private boolean checkProfileStatus() {
        String[] data = DatabaseHelper.getUserById(studentId); // UPDATED
        if (data == null) return false;
        
        boolean hasResume = DatabaseHelper.getResumeFile(studentId) != null; // UPDATED
        
        if (!hasResume) {
            JOptionPane.showMessageDialog(null, "Welcome! Please complete your profile and upload a resume to continue.");
            new StudentProfileUI(studentId, studentName, true).setVisible(true); // RENAMED
            return false; 
        }
        return true;
    }

    private boolean isInterning() {
        String[] data = DatabaseHelper.getUserById(studentId); // UPDATED
        return data != null && data.length > 9 && "Placed".equalsIgnoreCase(data[9]);
    }
}