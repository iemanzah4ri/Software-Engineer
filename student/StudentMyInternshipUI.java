package student;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// shows details about my current job
public class StudentMyInternshipUI extends JFrame {

    private String studentId;
    private JLabel lblComp, lblRole, lblAdd, lblSvName, lblSvEmail;

    // constructor
    public StudentMyInternshipUI(String id) {
        this.studentId = id;
        initComponents();
        loadInternshipDetails();
    }

    // ui build
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

        // labels
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

    // find my data
    private void loadInternshipDetails() {
        // search matches
        List<String[]> matches = DatabaseHelper.getAllMatches(); 
        String[] myMatch = null;
        
        // find row with my id
        for(String[] m : matches) {
            if(m[1].equals(studentId)) {
                myMatch = m;
                break;
            }
        }

        // if no match found
        if(myMatch == null) {
            lblComp.setText("Status: Not Placed yet.");
            lblRole.setText("");
            lblAdd.setText("");
            return;
        }

        // update labels
        lblComp.setText("Company: " + myMatch[4]);
        lblRole.setText("Role: " + myMatch[5]);
        lblAdd.setText("Start Date: " + myMatch[6]);

        // get supervisor details
        String compSvId = myMatch[8];
        if(compSvId != null && !compSvId.equals("N/A")) {
            String[] sv = DatabaseHelper.getUserById(compSvId); 
            if(sv != null) {
                lblSvName.setText("Supervisor: " + sv[3]);
                lblSvEmail.setText("Email: " + (sv.length > 6 ? sv[6] : "N/A"));
            }
        }
    }
}