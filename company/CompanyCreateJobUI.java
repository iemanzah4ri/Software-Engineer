package company;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;

// form to create a new job posting
public class CompanyCreateJobUI extends JFrame {

    // input fields
    private JTextField txtRegNo, txtCompany, txtLocation, txtJobName;
    private JTextArea txtJobDesc;
    private String userRole; 
    private String prefilledCompany;

    // constructor
    public CompanyCreateJobUI(String role, String companyName) {
        this.userRole = role;
        this.prefilledCompany = companyName;
        initComponents();
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    // overloaded constructor
    public CompanyCreateJobUI(String role) {
        this(role, "");
    }

    // build gui
    private void initComponents() {
        setTitle("Create Internship Listing");
        setLayout(new BorderLayout());

        // header
        JLabel lblTitle = new JLabel("Post New Internship", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // form layout
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // init components
        txtRegNo = new JTextField(20);
        txtCompany = new JTextField(20);
        txtLocation = new JTextField(20);
        txtJobName = new JTextField(20);
        txtJobDesc = new JTextArea(5, 20);
        txtJobDesc.setLineWrap(true);
        
        // auto fill company name if known
        if (!prefilledCompany.isEmpty()) {
            txtCompany.setText(prefilledCompany);
            txtCompany.setEditable(false);
        }

        // adding rows to grid
        gbc.gridx=0; gbc.gridy=0; form.add(new JLabel("Registration No:"), gbc);
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

        // footer buttons
        JPanel footer = new JPanel();
        JButton btnPost = new JButton("Submit Listing");
        JButton btnCancel = new JButton("Cancel");

        btnPost.setBackground(new Color(100, 200, 100));
        
        // click listeners
        btnPost.addActionListener(e -> saveListing());
        btnCancel.addActionListener(e -> dispose());

        footer.add(btnPost);
        footer.add(btnCancel);
        add(footer, BorderLayout.SOUTH);
    }

    // save to db
    private void saveListing() {
        // get texts
        String reg = txtRegNo.getText();
        String comp = txtCompany.getText();
        String loc = txtLocation.getText();
        String jName = txtJobName.getText();
        String jDesc = txtJobDesc.getText();

        // validate empty
        if(reg.isEmpty() || comp.isEmpty() || loc.isEmpty() || jName.isEmpty() || jDesc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        // if admin creates it, its approved instantly. if company creates, its pending
        String status = userRole.equals("Admin") ? "Approved" : "Pending";
        
        // save via helper
        DatabaseHelper.saveListing(reg, comp, loc, jName, jDesc, status); 
        
        JOptionPane.showMessageDialog(this, "Listing Created! Status: " + status);
        dispose();
    }
}