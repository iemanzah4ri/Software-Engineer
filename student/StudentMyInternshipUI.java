//displays details of current internship
//shows company and supervisor contact info
package student;
import common.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentMyInternshipUI extends JFrame {

    private String studentId;
    private JLabel lblComp, lblRole, lblAdd, lblSvName, lblSvEmail;

    public StudentMyInternshipUI(String id) {
        this.studentId = id;
        initComponents();
        loadInternshipDetails();
    }

    private void initComponents() {
        setTitle("My Internship Details");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Current Placement Details", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridLayout(6, 1, 10, 10));
        content.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        lblComp = new JLabel("Company: Loading...");
        lblRole = new JLabel("Role: Loading...");
        lblAdd = new JLabel("Address: Loading...");
        lblSvName = new JLabel("Supervisor: Not Assigned");
        lblSvEmail = new JLabel("Email: N/A");
        
        Font f = new Font("Arial", Font.PLAIN, 16);
        lblComp.setFont(f); lblRole.setFont(f); lblAdd.setFont(f);
        lblSvName.setFont(f); lblSvEmail.setFont(f);

        content.add(lblComp);
        content.add(lblRole);
        content.add(lblAdd);
        content.add(new JSeparator());
        content.add(lblSvName);
        content.add(lblSvEmail);

        add(content, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadInternshipDetails() {
        // Find the match for this student
        List<String[]> matches = DatabaseHelper.getAllMatches(); // UPDATED
        String[] myMatch = null;
        
        for(String[] m : matches) {
            if(m[1].equals(studentId)) {
                myMatch = m;
                break;
            }
        }

        if(myMatch == null) {
            lblComp.setText("Status: Not Placed yet.");
            lblRole.setText("");
            lblAdd.setText("");
            return;
        }

        // Match found: [0]ID, [1]S-ID, [2]S-Name, [3]RegNo, [4]CompName, [5]Job, [6]Date, [7]AcadID, [8]CompID
        lblComp.setText("Company: " + myMatch[4]);
        lblRole.setText("Role: " + myMatch[5]);
        lblAdd.setText("Start Date: " + myMatch[6]);

        String compSvId = myMatch[8];
        if(compSvId != null && !compSvId.equals("N/A")) {
            String[] sv = DatabaseHelper.getUserById(compSvId); // UPDATED
            if(sv != null) {
                lblSvName.setText("Supervisor: " + sv[3]);
                lblSvEmail.setText("Email: " + (sv.length > 6 ? sv[6] : "N/A"));
            }
        }
    }
}