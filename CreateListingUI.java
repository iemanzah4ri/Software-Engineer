import javax.swing.*;
import java.awt.*;

public class CreateListingUI extends JFrame {

    private JTextField txtRegNo, txtCompany, txtLocation, txtJobName;
    private JTextArea txtJobDesc;
    private String userRole; 
    private String prefilledCompany;

    // Constructor for Company Supervisor (Auto-fills company)
    public CreateListingUI(String role, String companyName) {
        this.userRole = role;
        this.prefilledCompany = companyName;
        initComponents();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // Default Constructor for Admin (No auto-fill)
    public CreateListingUI(String role) {
        this(role, "");
    }

    private void initComponents() {
        setTitle("Create Internship Listing");
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Post New Internship", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRegNo = new JTextField(20);
        txtCompany = new JTextField(20);
        txtLocation = new JTextField(20);
        txtJobName = new JTextField(20); // NEW FIELD
        txtJobDesc = new JTextArea(5, 20);
        txtJobDesc.setLineWrap(true);

        // Logic to auto-fill and lock company name
        if (userRole.equals("Company Supervisor") && !prefilledCompany.isEmpty()) {
            txtCompany.setText(prefilledCompany);
            txtCompany.setEditable(false);
        }

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Company Reg. No:"), gbc);
        gbc.gridx=1; form.add(txtRegNo, gbc);

        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Company Name:"), gbc);
        gbc.gridx=1; form.add(txtCompany, gbc);

        gbc.gridx=0; gbc.gridy=2; form.add(new JLabel("Location:"), gbc);
        gbc.gridx=1; form.add(txtLocation, gbc);

        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("Job Title:"), gbc);
        gbc.gridx=1; form.add(txtJobName, gbc);

        gbc.gridx=0; gbc.gridy=4; form.add(new JLabel("Job Description:"), gbc);
        gbc.gridx=1; form.add(new JScrollPane(txtJobDesc), gbc);

        add(form, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnPost = new JButton("Submit Listing");
        JButton btnCancel = new JButton("Cancel");

        btnPost.setBackground(new Color(100, 200, 100));
        
        btnPost.addActionListener(e -> saveListing());
        btnCancel.addActionListener(e -> dispose());

        footer.add(btnPost);
        footer.add(btnCancel);
        add(footer, BorderLayout.SOUTH);
    }

    private void saveListing() {
        String reg = txtRegNo.getText();
        String comp = txtCompany.getText();
        String loc = txtLocation.getText();
        String jName = txtJobName.getText();
        String jDesc = txtJobDesc.getText();

        if(reg.isEmpty() || comp.isEmpty() || loc.isEmpty() || jName.isEmpty() || jDesc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        String status = userRole.equals("Admin") ? "Approved" : "Pending";
        String message = userRole.equals("Admin") ? "Listing Published!" : "Listing Submitted for Approval.";

        // Correct 6-argument call
        DBHelper.saveListing(reg, comp, loc, jName, jDesc, status);
        JOptionPane.showMessageDialog(this, message);
        dispose();
    }
}