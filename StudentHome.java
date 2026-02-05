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
        }
    }
    
    public StudentHome(String id) {
        this(id, "Student");
    }
    
    private boolean checkProfileStatus() {
        String[] data = DBHelper.getUserById(studentId);
        if (data != null) {
            // Check for missing details (Indices 6, 7, 8)
            boolean emailMissing = data.length <= 6 || data[6].trim().isEmpty() || data[6].equalsIgnoreCase("N/A");
            boolean contactMissing = data.length <= 7 || data[7].trim().isEmpty() || data[7].equalsIgnoreCase("N/A");
            boolean addressMissing = data.length <= 8 || data[8].trim().isEmpty() || data[8].equalsIgnoreCase("N/A");

            if (emailMissing || contactMissing || addressMissing) {
                JOptionPane.showMessageDialog(null, 
                    "Welcome! You must complete your profile details to proceed.", 
                    "Profile Incomplete", 
                    JOptionPane.WARNING_MESSAGE);
                
                new StudentProfile(studentId, studentName, true).setVisible(true);
                return false; // Prevent main dashboard from loading
            }
        }
        return true;
    }

    // Helper to check if student is currently placed
    private boolean isInterning() {
        String[] data = DBHelper.getUserById(studentId);
        // Column 9 is placement status ("Placed" / "Not Placed")
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

        JPanel buttons = new JPanel(new GridLayout(4, 2, 10, 10)); // Adjusted grid
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnUpdateProfile = new JButton("Update Profile");
        btnUpdateProfile.addActionListener(e -> new StudentProfile(studentId, studentName, false).setVisible(true));

        // 1. View Listings (Only if NOT interning)
        JButton btnViewListings = new JButton("View Internship Listings");
        btnViewListings.addActionListener(e -> {
            if (isInterning()) {
                JOptionPane.showMessageDialog(this, "You are already placed in an internship.\nYou cannot view new listings.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            } else {
                new StudentViewListingsUI(studentId).setVisible(true);
            }
        });

        // 2. Submit Logbook (Only if interning)
        JButton btnSubmitLog = new JButton("Submit Daily Logbook");
        btnSubmitLog.addActionListener(e -> {
            if (isInterning()) {
                new StudentLogbook(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 3. Submit Attendance (Only if interning)
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

        // 4. View Supervisor Feedback (Only if interning)
        JButton btnFeedback = new JButton("View Supervisor Feedback");
        btnFeedback.addActionListener(e -> {
            if (isInterning()) {
                new SupervisorFeedback(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });

        // 5. View Internship Progress (Only if interning)
        JButton btnProgress = new JButton("View Internship Progress");
        btnProgress.addActionListener(e -> {
            if (isInterning()) {
                new StudentProgress(studentId, studentName).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "You must be placed in an internship to access this.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(255, 100, 100));
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        buttons.add(btnUpdateProfile);
        buttons.add(btnViewListings);
        buttons.add(btnSubmitLog);
        buttons.add(btnAttendance);
        buttons.add(btnTrack);
        buttons.add(btnFeedback);
        buttons.add(btnProgress);
        buttons.add(btnLogout);

        add(buttons, BorderLayout.CENTER);
    }
}