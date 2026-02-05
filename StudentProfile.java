import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StudentProfile extends JFrame {
    private String studentId;
    private String studentName;
    private boolean isForced;
    private JTextField txtUser, txtPass, txtName, txtMatric, txtEmail, txtContact, txtAddress;
    private JLabel lblProfilePicPreview;
    // Temporary holder for selected image until save is clicked
    private File tempProfileImage = null;

    public StudentProfile(String id, String name, boolean forced) {
        this.studentId = id;
        this.studentName = name;
        this.isForced = forced;
        initComponents();
        loadData();
    }

    public StudentProfile(String id) {
        this(id, "Student", false);
    }

    private void initComponents() {
        setTitle("Update My Profile");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        // Use BorderLayout to put form at top so fields don't stretch vertically
        setLayout(new BorderLayout());

        // Main Panel to hold form content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- Profile Picture Section ---
        JPanel picPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblProfilePicPreview = new JLabel();
        lblProfilePicPreview.setPreferredSize(new Dimension(150, 150));
        lblProfilePicPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblProfilePicPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblProfilePicPreview.setText("No Image");

        JButton btnUploadPic = new JButton("Upload Photo");
        btnUploadPic.addActionListener(this::handleUploadAction);
        
        JPanel picContainer = new JPanel(new BorderLayout());
        picContainer.add(lblProfilePicPreview, BorderLayout.CENTER);
        picContainer.add(btnUploadPic, BorderLayout.SOUTH);
        picContainer.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        mainPanel.add(picContainer);
        // -------------------------------


        // --- Form Fields Section (using GridBagLayout for neat alignment) ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        txtUser = addFormField(formPanel, gbc, "Username:", row++);
        txtPass = addFormField(formPanel, gbc, "Password:", row++);
        txtName = addFormField(formPanel, gbc, "Full Name:", row++);
        txtMatric = addFormField(formPanel, gbc, "Matric No:", row++);
        txtMatric.setEditable(false);
        txtEmail = addFormField(formPanel, gbc, "Email:", row++);
        txtContact = addFormField(formPanel, gbc, "Contact No:", row++);
        txtAddress = addFormField(formPanel, gbc, "Address:", row++);

        mainPanel.add(formPanel);
        // --------------------------------------------------------------------

        // Put main panel at the top (NORTH) so it doesn't stretch
        add(mainPanel, BorderLayout.NORTH);


        // --- Button Panel (South) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnSave = new JButton("Save Changes");
        btnSave.setBackground(new Color(100, 200, 100));
        btnSave.addActionListener(e -> saveData());

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> handleCloseAction());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnClose);
        add(buttonPanel, BorderLayout.SOUTH);
        // ----------------------------
    }

    // Helper to add fields neatly
    private JTextField addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.1;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField field = new JTextField(20);
        panel.add(field, gbc);
        return field;
    }

    private void handleUploadAction(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        // Filter for image files only
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images (JPG, JPEG, PNG)", "jpg", "jpeg", "png");
        chooser.setFileFilter(filter);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            String name = f.getName().toLowerCase();
            if(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png")) {
                // Store temp, don't save yet
                this.tempProfileImage = f; 
                updatePreviewLabel(f);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid image format (JPG, JPEG, PNG).");
            }
        }
    }

    private void updatePreviewLabel(File imageFile) {
        try {
            BufferedImage img = ImageIO.read(imageFile);
            if (img != null) {
                // Scale image to fit label
                Image scaledImg = img.getScaledInstance(lblProfilePicPreview.getWidth(), lblProfilePicPreview.getHeight(), Image.SCALE_SMOOTH);
                lblProfilePicPreview.setIcon(new ImageIcon(scaledImg));
                lblProfilePicPreview.setText("");
            }
        } catch (Exception ex) {
            lblProfilePicPreview.setText("Error loading image");
            ex.printStackTrace();
        }
    }

    private void loadData() {
        String[] data = DBHelper.getUserById(studentId);
        if(data != null) {
            txtUser.setText(data[1]);
            txtPass.setText(data[2]);
            txtName.setText(data[3]);
            txtMatric.setText(data[4]);
            txtEmail.setText(data[6].equals("N/A") ? "" : data[6]);
            txtContact.setText(data[7].equals("N/A") ? "" : data[7]);
            txtAddress.setText(data[8].equals("N/A") ? "" : data[8]);

            // Load existing profile picture if it exists
            File existingPic = DBHelper.getProfileImage(studentId);
            if(existingPic != null && existingPic.exists()) {
                updatePreviewLabel(existingPic);
            }
        }
    }

    private void saveData() {
        // 1. Text Field Validation
        if (txtEmail.getText().trim().isEmpty() || 
            txtContact.getText().trim().isEmpty() || 
            txtAddress.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email, Contact, and Address cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Profile Picture Validation (Mandatory if forced update)
        File currentPic = DBHelper.getProfileImage(studentId);
        boolean hasExistingPic = (currentPic != null && currentPic.exists());

        if (isForced && tempProfileImage == null && !hasExistingPic) {
             JOptionPane.showMessageDialog(this, "You must upload a profile picture to proceed.", "Validation Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        // 3. Save Image if a new one was selected
        if(tempProfileImage != null) {
            boolean saved = DBHelper.saveProfileImage(tempProfileImage, studentId);
            if(!saved) {
                 JOptionPane.showMessageDialog(this, "Failed to save profile image.", "Error", JOptionPane.ERROR_MESSAGE);
                 // Decide if you want to stop saving text data here or continue
            }
        }

        // 4. Save Text Data
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
        
        JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
        dispose();
        
        // Re-open dashboard if this was a forced update
        if (isForced) {
            new StudentHome(studentId, studentName).setVisible(true);
        }
    }

    private void handleCloseAction() {
        if (isForced) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Your profile is incomplete. If you close now, you will be logged out.\nAre you sure?", 
                "Confirm Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                // Optionally redirect to login: new LoginForm().setVisible(true);
            }
        } else {
            dispose();
        }
    }
}