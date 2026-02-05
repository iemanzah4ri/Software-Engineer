import javax.swing.*;
import java.awt.*;

public class CompanySupervisorHome extends JFrame {

    private String supervisorId;
    private String supervisorName;
    private String companyName;

    public CompanySupervisorHome(String id) {
        this.supervisorId = id;
        loadSupervisorDetails();
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
    
    public CompanySupervisorHome() {
        this.supervisorId = "0"; 
        this.supervisorName = "Unknown Supervisor";
        this.companyName = "Unknown";
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void loadSupervisorDetails() {
        String[] data = DBHelper.getUserById(supervisorId);
        if (data != null) {
            this.supervisorName = data[3];
            this.companyName = (data.length > 8) ? data[8] : "Unknown";
        } else {
            this.supervisorName = "Supervisor";
            this.companyName = "Unknown";
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Company Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(153, 255, 255));
        headerPanel.setPreferredSize(new Dimension(800, 80));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));

        JLabel lblWelcome = new JLabel("Welcome " + supervisorName + " (" + companyName + ")");
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        headerPanel.add(lblWelcome);

        add(headerPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 1, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnManageApp = new JButton("Manage Applications");
        JButton btnViewAssigned = new JButton("View Assigned Student");
        JButton btnViewProfile = new JButton("View Student Profile");
        JButton btnVerifyLogbook = new JButton("Verify Logbook");
        JButton btnSubmitEval = new JButton("Submit Performance Evaluation");
        JButton btnViewProgress = new JButton("View Internship Progress");
        JButton btnPostListing = new JButton("Post Internship Listing"); 
        JButton btnLogout = new JButton("Log Out");

        btnManageApp.addActionListener(evt -> {
            new CompanyManageApplicationsUI(companyName).setVisible(true);
        });

        btnViewAssigned.addActionListener(evt -> {
            new ViewAssignedStudentsUI(supervisorId).setVisible(true);
        });

        // UPDATED: Now passes supervisorId
        btnViewProfile.addActionListener(evt -> {
            new ViewStudentProfileUI(supervisorId).setVisible(true);
        });

        btnVerifyLogbook.addActionListener(evt -> {
            new VerifyLogbookUI(supervisorName).setVisible(true);
        });

        btnPostListing.addActionListener(evt -> {
            new CreateListingUI("Company Supervisor", companyName).setVisible(true);
        });

        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });

        buttonPanel.add(btnManageApp);
        buttonPanel.add(btnViewAssigned);
        buttonPanel.add(btnViewProfile);
        buttonPanel.add(btnVerifyLogbook);
        buttonPanel.add(btnSubmitEval);
        buttonPanel.add(btnViewProgress);
        buttonPanel.add(btnPostListing); 
        buttonPanel.add(btnLogout);

        add(buttonPanel, BorderLayout.CENTER);
    }
}