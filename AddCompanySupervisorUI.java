import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AddCompanySupervisorUI extends JFrame {

    private JTextField txtUser, txtName, txtComp, txtPos, txtEmail;
    private JPasswordField txtPass;

    public AddCompanySupervisorUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Register Company Supervisor");
        setSize(500, 500); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Register Company Supervisor", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUser = new JTextField(20);
        txtPass = new JPasswordField(20);
        txtName = new JTextField(20);
        txtComp = new JTextField(20);
        txtPos = new JTextField(20);
        txtEmail = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtUser, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtComp, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtPos, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; 
        formPanel.add(txtEmail, gbc);

        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnRegister = new JButton("Register");
        btnRegister.setPreferredSize(new Dimension(100, 35));
        btnRegister.setBackground(new Color(220, 220, 220)); 
        
        btnRegister.addActionListener(e -> register());
        
        gbc.gridx = 1; gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnRegister, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void register() {
        String user = txtUser.getText();
        String pass = new String(txtPass.getPassword());
        String name = txtName.getText();
        String comp = txtComp.getText();
        String pos = txtPos.getText();
        String email = txtEmail.getText();

        if (user.isEmpty() || pass.isEmpty() || name.isEmpty() || comp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.");
            return;
        }

        DBHelper.saveCompanySupervisor(user, pass, name, pos, comp, email);
        
        JOptionPane.showMessageDialog(this, "Company Supervisor Registered Successfully!");
        dispose();
    }
}