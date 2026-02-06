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
        //check if profile is done before starting
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
        //dont show window if profile incomplete
        if (b && !profileComplete) {
            return;
        }
        super.setVisible(b);
    }
    
    private void checkNotifications() {
        //see if there are new msgs
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
        //count how many are unread
        for (String[] notif : NotificationHelper.getNotifications(studentId)) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                count++;
            }
        }
        return count;
    }

    private void initComponents() {
        //basic window setup
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Dashboard - " + studentName);
        setLayout(new BorderLayout());

        //header part
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome, " + studentName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        //buttons grid
        JPanel buttons = new JPanel(new GridLayout(4, 2, 15, 15));
        buttons.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton btnUpdateProfile = new JButton("Update Profile / Upload Resume");
        btnUpdateProfile.addActionListener(e -> new StudentProfileUI(studentId, studentName, false).setVisible(true));

        JButton btnViewListings = new JButton("View & Apply for Internships");
        btnViewListings.addActionListener(e -> new StudentJobBoardUI(studentId).setVisible(true));
        
        //logbook button
        JButton btnSubmitLog = new JButton("Daily Logbook");
        btnSubmitLog.addActionListener(e -> {
            //check if working and date started
            if (isActiveIntern()) {
                if (DatabaseHelper.isInternshipStarted(studentId)) {
                    new StudentLogbookUI(studentId, studentName).setVisible(true); 
                } else {
                    JOptionPane.showMessageDialog(this, "Your internship has not started yet.\nPlease wait for your Start Date.", "Too Early", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Access Restricted:\nOnly 'Placed' (Active) interns can add log entries.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        //attendance button
        JButton btnAttendance = new JButton("Attendance (Clock In/Out)");
        btnAttendance.addActionListener(e -> {
            //check permissions again
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

        JButton btnTrack = new JButton("Track Application Status");
        btnTrack.addActionListener(e -> new StudentTrackerUI(studentId, studentName).setVisible(true));

        //progress button
        JButton btnProgress = new JButton("View Internship Progress and Feedback");
        btnProgress.addActionListener(e -> {
            //completed students can see this too
            if (canViewProgress()) {
                new StudentProgressUI(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You have not started an internship yet.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton btnNotif = new JButton("View Notifications");
        btnNotif.addActionListener(e -> new NotificationViewUI(studentId).setVisible(true));
        
        //logout logic
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginUI().setVisible(true);
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
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null) return false;
        
        //make sure they uploaded resume
        boolean hasResume = DatabaseHelper.getResumeFile(studentId) != null;
        
        if (!hasResume) {
            JOptionPane.showMessageDialog(null, "Welcome! Please complete your profile and upload a resume to continue.");
            new StudentProfileUI(studentId, studentName, true).setVisible(true);
            return false; 
        }
        return true;
    }

    //helper stuff

    //strict check: only active interns
    private boolean isActiveIntern() {
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null || data.length <= 9) return false;
        
        String status = data[9];
        return "Placed".equalsIgnoreCase(status);
    }

    //lenient check: active or completed
    private boolean canViewProgress() {
        String[] data = DatabaseHelper.getUserById(studentId);
        if (data == null || data.length <= 9) return false;
        
        String status = data[9];
        return "Placed".equalsIgnoreCase(status) || "Completed".equalsIgnoreCase(status);
    }
}