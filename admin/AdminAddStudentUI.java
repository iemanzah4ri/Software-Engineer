package admin;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;

// window to add students
public class AdminAddStudentUI extends JFrame {
    // fields for student info
    private JTextField userField, nameField, intakeField;
    private JPasswordField passField;

    // constructor
    public AdminAddStudentUI() {
        // setup basic frame properties
        setTitle("Add New Student");
        setSize(500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header title
        JLabel titleLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // spacing
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // form container
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // username input
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userField = new JTextField(15);
        formPanel.add(userField, gbc);

        // password input
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        // fullname input
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // intake code input
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Intake:"), gbc);
        gbc.gridx = 1;
        intakeField = new JTextField(15);
        formPanel.add(intakeField, gbc);

        // add button
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Individual");
        addBtn.setBackground(new Color(220, 220, 220));
        addBtn.addActionListener(e -> registerStudent());
        formPanel.add(addBtn, gbc);

        // add form to center
        add(formPanel, BorderLayout.CENTER);

        // footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);

        // center window
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // save student to db
    private void registerStudent() {
        // grab text
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        String name = nameField.getText();
        String intake = intakeField.getText();

        // empty check
        if(user.isEmpty() || pass.isEmpty() || name.isEmpty() || intake.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // save to file via helper
        DatabaseHelper.saveUser(user, pass, name, intake, "Student"); 
        
        // done
        JOptionPane.showMessageDialog(this, "Student Added Successfully!");
        dispose();
    }
}