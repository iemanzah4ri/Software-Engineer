package admin;
// import common stuff
import common.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// screen to register a company supervisor
public class AdminAddCompanyUI extends JFrame {

    // declare text fields for inputs
    private JTextField txtUser, txtName, txtComp, txtPos, txtEmail;
    // password field
    private JPasswordField txtPass;

    // constructor
    public AdminAddCompanyUI() {
        // initialize everything
        initComponents();
    }

    // setup the ui components
    private void initComponents() {
        // set title
        setTitle("Register Company Supervisor");
        // set window size
        setSize(500, 500); 
        // center on screen
        setLocationRelativeTo(null);
        // only close this window on exit
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // layout manager
        setLayout(new BorderLayout());

        // main title label
        JLabel title = new JLabel("Register Company Supervisor", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        // add padding
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // panel for the form
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        // padding
        gbc.insets = new Insets(10, 10, 10, 10);
        // align left
        gbc.anchor = GridBagConstraints.WEST;
        // fill horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // initialize fields
        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        txtName = new JTextField(20);
        txtComp = new JTextField(20);
        txtPos = new JTextField(20);
        txtEmail = new JTextField(20);

        // row 0: username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtUser, gbc);

        // row 1: password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtPass, gbc);

        // row 2: full name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtName, gbc);

        // row 3: company name
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtComp, gbc);

        // row 4: job position
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtPos, gbc);

        // row 5: email address
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtEmail, gbc);

        // button panel
        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(100, 35));
        // grey button
        btnRegister.setBackground(new Color(220, 220, 220)); 
        
        // add action listener for submit
        btnRegister.addActionListener(e -> register());
        
        // add button to grid
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnRegister, gbc);

        // add form to center
        add(formPanel, BorderLayout.CENTER);

        // footer with back button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // function to save data
    private void register() {
        // get all text values
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String name = txtName.getText();
        String comp = txtComp.getText();
        String pos = txtPos.getText();
        String email = txtEmail.getText();

        // simple validation
        if (user.isEmpty() || pass.isEmpty() || name.isEmpty() || comp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        // save using special company supervisor function
        DatabaseHelper.saveCompanySupervisor(user, pass, name, pos, comp, email); 
        
        // success message
        JOptionPane.showMessageDialog(this, "Company Supervisor Registered Successfully!");
        // close window
        dispose();
    }
}