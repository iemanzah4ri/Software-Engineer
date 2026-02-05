import javax.swing.*;
import java.awt.*;

public class CreateListingUI extends JFrame {

    private JTextField txtRegNo, txtCompany, txtLocation;
    private JTextArea txtJobDesc;
    private String userRole; 

    public CreateListingUI(String role) {
        this.userRole = role;
        initComponents();
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        txtJobDesc = new JTextArea(5, 20);
        txtJobDesc.setLineWrap(true);

        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Company Reg. No:"), gbc);
        gbc.gridx=1; form.add(txtRegNo, gbc);

        gbc.gridx=0; gbc.gridy=1; form.add(new JLabel("Company Name:"), gbc);
        gbc.gridx=1; form.add(txtCompany, gbc);

        gbc.gridx=0; gbc.gridy=2; form.add(new JLabel("Location:"), gbc);
        gbc.gridx=1; form.add(txtLocation, gbc);

        gbc.gridx=0; gbc.gridy=3; form.add(new JLabel("Job Description:"), gbc);
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
        String job = txtJobDesc.getText();

        if(reg.isEmpty() || comp.isEmpty() || loc.isEmpty() || job.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        String status;
        String message;
        
        if (userRole.equals("Admin")) {
            status = "Approved"; 
            message = "Listing Published Successfully!";
        } else {
            status = "Pending"; 
            message = "Listing Submitted! Waiting for Admin Approval.";
        }

        DBHelper.saveListing(reg, comp, loc, job, status);
        JOptionPane.showMessageDialog(this, message);
        dispose();
    }
}