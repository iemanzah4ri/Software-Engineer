package student;
// standard imports
import common.*;
import javax.swing.*;
import java.awt.*;

// the main menu for students
public class StudentDashboardUI extends JFrame {

    // student data
    private String studentId;
    private String studentName;
    private boolean profileComplete = true;

    // constructor
    public StudentDashboardUI(String id, String name) {
        this.studentId = id;
        this.studentName = name;
        // make sure they have a resume uploaded
        this.profileComplete = checkProfileStatus();
        
        // only show if profile is ok
        if (profileComplete) {
            initComponents();
            setSize(800, 600);
            setLocationRelativeTo(null);
            // check for messages
            checkNotifications();
        }
    }
    
    // default constructor
    public StudentDashboardUI(String id) {
        this(id, "Student");
    }

    // override setvisible to block access if incomplete
    @Override
    public void setVisible(boolean b) {
        if (b && !profileComplete) {
            return;
        }
        super.setVisible(b);
    }
    
    // check for unread alerts
    private void checkNotifications() {
        if (NotificationHelper.hasUnreadNotifications(studentId)) {
            // run later to prevent lag
            SwingUtilities.invokeLater(() -> {
                int unreadCount = countUnreadNotifications();
                JOptionPane.showMessageDialog(this, 
                    "You have " + unreadCount + " unread notification(s)!", 
                    "New Notifications", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    // count how many red dots
    private int countUnreadNotifications() {
        int count = 0;
        for (String[] notif : NotificationHelper.getNotifications(studentId)) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }

    // setting up the dashboard buttons
    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Dashboard - " + studentName);
        setLayout(new BorderLayout());

        // header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + studentName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        // button grid
        JPanel buttons = new JPanel(new GridLayout(4, 2, 15, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        // profile button
        JButton btnUpdateProfile = new JButton("Update Profile / Upload Resume");
        btnUpdateProfile.addActionListener(e -> new StudentProfileUI(studentId, studentName, false).setVisible(true));

        // job board button
        JButton btnViewListings = new JButton("View & Apply for Internships");
        btnViewListings.addActionListener(e -> new StudentJobBoardUI(studentId).setVisible(true));
        
        // logbook button
        JButton btnSubmitLog = new JButton("Daily Logbook");
        btnSubmitLog.addActionListener(e -> {
            // check if they are officially an intern
            if (isActiveIntern()) {
                // check if start date passed
                if (DatabaseHelper.isInternshipStarted(studentId)) {
                    new StudentLogbookUI(studentId, studentName).setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(this, "Your internship has not started yet.\nPlease wait for your Start Date.", "Too Early", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Access Restricted:\nOnly 'Placed' (Active) interns can add log entries.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        // attendance button
        JButton btnAttendance = new JButton("Attendance (Clock In/Out)");
        btnAttendance.addActionListener(e -> {
            // same checks as logbook
            if (isActiveIntern()) {
                if (DatabaseHelper.isInternshipStarted(studentId)) {
                    new StudentAttendanceUI(studentId, studentName).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Your internship has not started yet.\nYou cannot clock in before your Start Date.", "Too Early", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Access Restricted:\nOnly 'Placed' (Active) interns can clock in.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        // tracking apps button
        JButton btnTrack = new JButton("Track Application Status");
        btnTrack.addActionListener(e -> new StudentTrackerUI(studentId, studentName).setVisible(true));

        // progress button
        JButton btnProgress = new JButton("View Internship Progress and Feedback");
        btnProgress.addActionListener(e -> {
            // can view if placed or completed
            if (canViewProgress()) {
                new StudentProgressUI(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You have not started an internship yet.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // notifications button
        JButton btnNotif = new JButton("View Notifications");
        btnNotif.addActionListener(e -> new NotificationViewUI(studentId).setVisible(true));
        
        // logout
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginUI().setVisible(true);
        });
        
        // add all buttons
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
    
    // verify resume exists
    private boolean checkProfileStatus() {
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null) return false;
        
        // check file system
        boolean hasResume = DatabaseHelper.getResumeFile(studentId) != null;
        
        // force them to update if missing
        if (!hasResume) {
            JOptionPane.showMessageDialog(null, "Welcome! Please complete your profile and upload a resume to continue.");
            new StudentProfileUI(studentId, studentName, true).setVisible(true);
            return false; 
        }
        return true;
    }

    // helper to check if status is Placed
    private boolean isActiveIntern() {
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null || data.length <= 9) return false;
        
        String status = data[9];
        return "Placed".equalsIgnoreCase(status);
    }

    // helper to check if Placed or Completed
    private boolean canViewProgress() {
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null || data.length <= 9) return false;
        
        String status = data[9];
        return "Placed".equalsIgnoreCase(status) || "Completed".equalsIgnoreCase(status);
    }
}