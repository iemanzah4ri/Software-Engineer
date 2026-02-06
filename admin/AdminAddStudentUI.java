//form interface to register a new student
//saves student credentials to the database
package admin;
import common.*;
import javax.swing.*;
import java.awt.*;

public class AdminAddStudentUI extends JFrame {
    private JTextField userField, nameField, intakeField;
    private JPasswordField passField;

    public AdminAddStudentUI() {
        setTitle("Add New Student");
        setSize(500, 450);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Add New Student", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userField = new JTextField(15);
        formPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Intake:"), gbc);
        gbc.gridx = 1;
        intakeField = new JTextField(15);
        formPanel.add(intakeField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Individual");
        addBtn.setBackground(new Color(220, 220, 220));
        addBtn.addActionListener(e -> registerStudent());
        formPanel.add(addBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registerStudent() {
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        String name = nameField.getText();
        String intake = intakeField.getText();

        if(user.isEmpty() || pass.isEmpty() || name.isEmpty() || intake.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        DatabaseHelper.saveUser(user, pass, name, intake, "Student"); // UPDATED
        
        JOptionPane.showMessageDialog(this, "Student Added Successfully!");
        dispose();
    }
}