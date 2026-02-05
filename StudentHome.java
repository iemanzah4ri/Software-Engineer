import javax.swing.*;
import java.awt.*;

public class StudentHome extends JFrame {

    private String studentId;
    private String studentName; // Added field to store name

    // Updated Constructor to accept both ID and Name
    public StudentHome(String id, String name) {
        this.studentId = id;
        this.studentName = name;
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
        checkProfileStatus();
    }
    
    // Fallback constructor (just in case)
    public StudentHome(String id) {
        this(id, "Student");
    }
    
    private void checkProfileStatus() {
        String[] data = DBHelper.getUserById(studentId);
        if (data != null) {
            // Check specific profile fields (Email, Contact, Address)
            if (data[6].equals("N/A") || data[7].equals("N/A") || data[8].equals("N/A")) {
                JOptionPane.showMessageDialog(this, 
                    "Welcome! You must complete your profile details to proceed.", 
                    "Profile Incomplete", 
                    JOptionPane.WARNING_MESSAGE);
                // Ensure StudentProfile accepts just ID (based on your previous code)
                new StudentProfile(studentId).setVisible(true);
            }
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Student Dashboard - " + studentName);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome " + studentName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        // Buttons Grid
        JPanel buttons = new JPanel(new GridLayout(4, 2, 10, 10)); // Adjusted rows
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnUpdateProfile = new JButton("Update Profile");
        btnUpdateProfile.addActionListener(e -> new StudentProfile(studentId).setVisible(true));

        JButton btnViewListings = new JButton("View Internship Listings");
        btnViewListings.addActionListener(e -> new ViewInternship(studentId, studentName).setVisible(true));

        JButton btnSubmitLog = new JButton("Submit Daily Logbook");
        // FIXED LINE: Passing both ID and Name
        btnSubmitLog.addActionListener(e -> new StudentLogbook(studentId, studentName).setVisible(true));

        JButton btnAttendance = new JButton("Submit Attendance Record");
        btnAttendance.addActionListener(e -> new SubmitAttendance(studentId, studentName).setVisible(true));

        JButton btnTrack = new JButton("Track Application Status");
        btnTrack.addActionListener(e -> new StudentTrack(studentId, studentName).setVisible(true));

        JButton btnFeedback = new JButton("View Supervisor Feedback");
        btnFeedback.addActionListener(e -> new SupervisorFeedback(studentId, studentName).setVisible(true));

        JButton btnProgress = new JButton("View Internship Progress");
        btnProgress.addActionListener(e -> new StudentProgress(studentId, studentName).setVisible(true));
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(Color.PINK);
        btnLogout.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        // Add buttons to panel
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