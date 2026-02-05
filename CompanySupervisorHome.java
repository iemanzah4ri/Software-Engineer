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
        setSize(800, 650);
        setLocationRelativeTo(null);
    }
    
    public CompanySupervisorHome() {
        this.supervisorId = "0"; 
        this.supervisorName = "Unknown Supervisor";
        this.companyName = "Unknown";
        initComponents();
        setSize(800, 650);
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
        buttonPanel.setLayout(new GridLayout(8, 1, 10, 10)); 
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnManageApps = new JButton("Manage Student Applications");
        btnManageApps.setBackground(new Color(144, 238, 144));
        btnManageApps.addActionListener(e -> {
            new CompanyManageApplicationsUI(companyName).setVisible(true);
        });

        JButton btnViewAssigned = new JButton("View Assigned Student");
        JButton btnViewProfile = new JButton("View Student Profile");
        JButton btnVerifyLogbook = new JButton("Verify Logbook");
        JButton btnSubmitEval = new JButton("Submit Performance Evaluation");
        JButton btnViewProgress = new JButton("View Internship Progress");
        JButton btnPostListing = new JButton("Post Internship Listing"); 
        JButton btnLogout = new JButton("Log Out");

        btnViewAssigned.addActionListener(evt -> {
            JOptionPane.showMessageDialog(this, "Feature in development (Requires link)");
        });

        btnVerifyLogbook.addActionListener(evt -> {
            new VerifyLogbookUI(supervisorName).setVisible(true);
        });

        btnPostListing.addActionListener(evt -> {
            new CreateListingUI("Company Supervisor").setVisible(true);
        });

        btnLogout.addActionListener(evt -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });

        buttonPanel.add(btnManageApps);
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