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
            boolean emailMissing = data[6].trim().isEmpty() || data[6].equalsIgnoreCase("N/A");
            boolean contactMissing = data[7].trim().isEmpty() || data[7].equalsIgnoreCase("N/A");
            boolean addressMissing = data[8].trim().isEmpty() || data[8].equalsIgnoreCase("N/A");

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

    @Override
    public void setVisible(boolean b) {
        if (b && !profileComplete) {
            super.setVisible(false);
            return;
        }
        super.setVisible(b);
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

        JPanel buttons = new JPanel(new GridLayout(4, 2, 10, 10)); 
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton btnUpdateProfile = new JButton("Update Profile");
        btnUpdateProfile.addActionListener(e -> new StudentProfile(studentId, studentName, false).setVisible(true));

        JButton btnViewListings = new JButton("View Internship Listings");
        btnViewListings.addActionListener(e -> new ViewInternship(studentId, studentName).setVisible(true));

        JButton btnSubmitLog = new JButton("Submit Daily Logbook");
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