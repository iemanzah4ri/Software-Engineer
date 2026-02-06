//interface for students to update personal details
//handles profile picture upload and display
package student;
import common.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class StudentProfileUI extends JFrame {
    private String studentId;
    private String studentName;
    private boolean isForced;
    private JTextField txtUser, txtPass, txtName, txtMatric, txtEmail, txtContact, txtAddress;
    private JLabel lblProfilePicPreview;
    private File tempProfileImage = null;

    public StudentProfileUI(String id, String name, boolean forced) {
        this.studentId = id;
        this.studentName = name;
        this.isForced = forced;
        initComponents();
        loadData();
    }

    public StudentProfileUI(String id) {
        this(id, "Student", false);
    }

    private void initComponents() {
        setTitle("Update My Profile");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        lblProfilePicPreview = new JLabel();
        lblProfilePicPreview.setPreferredSize(new Dimension(100, 100));
        lblProfilePicPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblProfilePicPreview.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lblProfilePicPreview);
        mainPanel.add(Box.createVerticalStrut(10));

        JButton btnUploadPic = new JButton("Upload Profile Picture");
        btnUploadPic.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUploadPic.addActionListener(e -> uploadPicture());
        mainPanel.add(btnUploadPic);
        mainPanel.add(Box.createVerticalStrut(20));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        txtUser = new JTextField();
        txtPass = new JTextField();
        txtName = new JTextField();
        txtMatric = new JTextField();
        txtEmail = new JTextField();
        txtContact = new JTextField();
        txtAddress = new JTextField();
        
        addFormField(formPanel, "Username:", txtUser);
        addFormField(formPanel, "Password:", txtPass);
        addFormField(formPanel, "Full Name:", txtName);
        addFormField(formPanel, "Matric No:", txtMatric);
        addFormField(formPanel, "Email:", txtEmail);
        addFormField(formPanel, "Contact No:", txtContact);
        addFormField(formPanel, "Address:", txtAddress);
        
        mainPanel.add(formPanel);
        
        JButton btnUploadResume = new JButton("Upload / Update Resume (PDF)");
        btnUploadResume.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnUploadResume.addActionListener(e -> uploadResume());
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnUploadResume);

        add(mainPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnSave = new JButton("Save & Update");
        btnSave.setBackground(new Color(100, 200, 100));
        btnSave.addActionListener(this::saveProfile);
        
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> handleCloseAction());

        footer.add(btnSave);
        footer.add(btnClose);
        add(footer, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel p, String label, JTextField field) {
        p.add(new JLabel(label));
        p.add(field);
    }

    private void loadData() {
        String[] data = DatabaseHelper.getUserById(studentId); // UPDATED
        if (data != null) {
            txtUser.setText(data[1]);
            txtPass.setText(data[2]);
            txtName.setText(data[3]);
            txtMatric.setText(data.length > 4 ? data[4] : "");
            txtEmail.setText(data.length > 6 ? data[6] : "");
            txtContact.setText(data.length > 7 ? data[7] : "");
            txtAddress.setText(data.length > 8 ? data[8] : "");
            
            File pfp = DatabaseHelper.getProfileImage(studentId); // UPDATED
            if (pfp != null && pfp.exists()) {
                ImageIcon icon = new ImageIcon(pfp.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                lblProfilePicPreview.setIcon(new ImageIcon(img));
            } else {
                lblProfilePicPreview.setText("No Image");
            }
        }
    }

    private void uploadPicture() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
        if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            tempProfileImage = ch.getSelectedFile();
            ImageIcon icon = new ImageIcon(tempProfileImage.getAbsolutePath());
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblProfilePicPreview.setIcon(new ImageIcon(img));
            lblProfilePicPreview.setText("");
        }
    }

    private void uploadResume() {
        JFileChooser ch = new JFileChooser();
        ch.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            boolean success = DatabaseHelper.saveResume(ch.getSelectedFile(), studentId); // UPDATED
            if(success) JOptionPane.showMessageDialog(this, "Resume Uploaded Successfully!");
            else JOptionPane.showMessageDialog(this, "Error uploading resume.");
        }
    }

    private void saveProfile(ActionEvent e) {
        if (txtName.getText().isEmpty() || txtMatric.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and Matric No cannot be empty.");
            return;
        }
        
        File resume = DatabaseHelper.getResumeFile(studentId); // UPDATED
        if(isForced && resume == null) {
             JOptionPane.showMessageDialog(this, "You must upload a resume to proceed.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             return;
        }

        boolean hasExistingPic = (DatabaseHelper.getProfileImage(studentId) != null); // UPDATED
        if (isForced && tempProfileImage == null && !hasExistingPic) {
             JOptionPane.showMessageDialog(this, "You must upload a profile picture to proceed.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        if(tempProfileImage != null) {
            DatabaseHelper.saveProfileImage(tempProfileImage, studentId); // UPDATED
        }

        String[] current = DatabaseHelper.getUserById(studentId); // UPDATED
        String placement = (current != null) ? current[9] : "Not Placed";
        
        DatabaseHelper.updateUser(studentId, // UPDATED
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
        
        JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
        dispose();
        
        if (isForced) {
            new StudentDashboardUI(studentId, studentName).setVisible(true); // RENAMED
        }
    }

    private void handleCloseAction() {
        if (isForced) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Your profile is incomplete. You will be logged out.\nAre you sure?", 
                "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }
}