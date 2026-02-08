package admin;
// importing necessary libraries
import common.*;
import javax.swing.*;
import java.awt.*;

// this class is the window to add a new academic supervisor
public class AdminAddAcademicUI extends JFrame {
    // text fields for user input
    private JTextField userField, nameField;
    // password field so password is hidden
    private JPasswordField passField;

    // constructor to set up the window
    public AdminAddAcademicUI() {
        // setting the window title
        setTitle("Add Academic Supervisor");
        // window size
        setSize(500, 400);
        // dispose means close just this window, not the whole app
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // using border layout for the main frame
        setLayout(new BorderLayout());

        // title label at the top
        JLabel titleLabel = new JLabel("Add Academic Supervisor", SwingConstants.CENTER);
        // making the font bold and bigger
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        // adding some padding
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        // add title to the north
        add(titleLabel, BorderLayout.NORTH);

        // panel for the form inputs
        JPanel formPanel = new JPanel(new GridBagLayout());
        // grid bag constraints for layout control
        GridBagConstraints gbc = new GridBagConstraints();
        // adding margin around components
        gbc.insets = new Insets(10, 10, 10, 10);
        // fill the horizontal space
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // row 0: username
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        userField = new JTextField(15);
        formPanel.add(userField, gbc);

        // row 1: password
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passField = new JPasswordField(15);
        formPanel.add(passField, gbc);

        // row 2: full name
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // row 3: submit button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        // center the button
        gbc.anchor = GridBagConstraints.CENTER;
        JButton addBtn = new JButton("Add Academic Supervisor");
        // light grey button color
        addBtn.setBackground(new Color(220, 220, 220));
        // call register function when clicked
        addBtn.addActionListener(e -> registerSupervisor());
        formPanel.add(addBtn, gbc);

        // add form panel to center
        add(formPanel, BorderLayout.CENTER);

        // footer panel for back button
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        // close window when clicked
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        // add footer to bottom
        add(footerPanel, BorderLayout.SOUTH);

        // center window on screen
        setLocationRelativeTo(null);
        // show the window
        setVisible(true);
    }

    // function to handle registration logic
    private void registerSupervisor() {
        // get values from fields
        String user = userField.getText();
        String pass = new String(passField.getPassword());
        String name = nameField.getText();

        // check if any field is empty
        if(user.isEmpty() || pass.isEmpty() || name.isEmpty()) {
            // show error message
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        // save to database using helper class
        // "N/A" is for intake/group which academic supervisor doesn't have
        DatabaseHelper.saveUser(user, pass, name, "N/A", "Academic Supervisor"); 

        // show success message
        JOptionPane.showMessageDialog(this, "Academic Supervisor Added Successfully!");
        // close the window
        dispose();
    }
}