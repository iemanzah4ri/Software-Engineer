import javax.swing.*;
import java.awt.*;

public class StudentProfile extends JFrame {
    private String studentId;
    private JTextField txtUser, txtPass, txtName, txtMatric, txtEmail, txtContact, txtAddress;

    public StudentProfile(String id) {
        this.studentId = id;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Update My Profile");
        setSize(500, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 2, 10, 10));

        add(new JLabel("Username:"));
        txtUser = new JTextField();
        add(txtUser);

        add(new JLabel("Password:"));
        txtPass = new JTextField();
        add(txtPass);

        add(new JLabel("Full Name:"));
        txtName = new JTextField();
        add(txtName);

        add(new JLabel("Matric No:"));
        txtMatric = new JTextField(); 
        txtMatric.setEditable(false); 
        add(txtMatric);

        add(new JLabel("Email:"));
        txtEmail = new JTextField();
        add(txtEmail);

        add(new JLabel("Contact No:"));
        txtContact = new JTextField();
        add(txtContact);

        add(new JLabel("Address:"));
        txtAddress = new JTextField();
        add(txtAddress);

        JButton btnSave = new JButton("Save Changes");
        btnSave.addActionListener(e -> saveData());
        add(btnSave);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose);
        
        setLocationRelativeTo(null);
    }

    private void loadData() {
        String[] data = DBHelper.getUserById(studentId);
        if(data != null) {
            txtUser.setText(data[1]);
            txtPass.setText(data[2]);
            txtName.setText(data[3]);
            txtMatric.setText(data[4]);
            txtEmail.setText(data[6]);
            txtContact.setText(data[7]);
            txtAddress.setText(data[8]);
        }
    }

    private void saveData() {
        String[] current = DBHelper.getUserById(studentId);
        String placement = (current != null) ? current[9] : "Not Placed";
        
        DBHelper.updateUser(studentId, 
            txtUser.getText(), 
            txtPass.getText(), 
            txtName.getText(), 
            txtMatric.getText(), 
            "Student", 
            txtEmail.getText(), 
            txtContact.getText(), 
            txtAddress.getText(), 
            placement
        );
        JOptionPane.showMessageDialog(this, "Profile Updated!");
        dispose();
    }
}