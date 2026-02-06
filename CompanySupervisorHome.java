import javax.swing.*;
import java.awt.*;

public class CompanySupervisorHome extends JFrame {
    private String supervisorId;
    private String supervisorName;
    private String companyName;

    public CompanySupervisorHome(String id) {
        this.supervisorId = id;
        String[] data = DBHelper.getUserById(id);
        if (data != null) {
            this.supervisorName = data[3];
            this.companyName = (data.length > 8) ? data[8] : "Unknown";
        }
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Company Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lbl = new JLabel("Welcome " + supervisorName + " (" + companyName + ")");
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(lbl);
        add(header, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton btnManage = new JButton("Manage Applications");
        JButton btnEval = new JButton("Submit Evaluation");
        JButton btnVer = new JButton("Verify Logbook and Attendance");
        JButton btnViewS = new JButton("View Assigned Student Profile");
        JButton btnCreate = new JButton("Post Listing");
        JButton btnOut = new JButton("Logout");

        // Pass ID here
        btnManage.addActionListener(e -> new CompanyManageApplicationsUI(supervisorId, companyName).setVisible(true));
        btnEval.addActionListener(e -> new CompanyEvaluation(supervisorId).setVisible(true));
        btnVer.addActionListener(e -> new VerifyLogbookAttd(supervisorId).setVisible(true));
        btnViewS.addActionListener(e -> new ViewStudentProfileUI(supervisorId).setVisible(true));
        btnCreate.addActionListener(e -> new CreateListingUI("Company Supervisor", companyName).setVisible(true));
        btnOut.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

        btnPanel.add(btnManage);
        btnPanel.add(btnEval);
        btnPanel.add(btnVer);
        btnPanel.add(btnViewS);
        btnPanel.add(btnCreate);
        btnPanel.add(btnOut);
        add(btnPanel, BorderLayout.CENTER);
    }
}