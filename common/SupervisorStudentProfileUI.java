package common;
// imports
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

// allows supervisor to see student details
public class SupervisorStudentProfileUI extends JFrame {

    private String supervisorId;
    private JTextField searchField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel lblImage;
    private JTextField txtName, txtEmail, txtContact, txtAddress, txtIntake;

    // constructor
    public SupervisorStudentProfileUI(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadStudentList("");
    }

    // build layout
    private void initComponents() {
        setTitle("Assigned Students & Profiles");
        setSize(1000, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header
        JLabel titleLabel = new JLabel("Assigned Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // split screen for list and details
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(550); 
        add(splitPane, BorderLayout.CENTER);

        // --- Left Panel: Student List ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton btnSearch = new JButton("Search Name");
        btnSearch.addActionListener(e -> loadStudentList(searchField.getText()));
        
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        // table
        String[] columns = {"ID", "Name", "Position", "Start Date"};
        tableModel = new DefaultTableModel(columns, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        
        // click listener to update right panel
        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row != -1) {
                    loadProfileDetails(studentTable.getValueAt(row, 0).toString());
                }
            }
        });

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);

        // --- Right Panel: Profile Details ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // image placeholder
        lblImage = new JLabel("Select Student", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(120, 120));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        // form fields (read only)
        JPanel detailsForm = new JPanel(new GridLayout(6, 2, 10, 10));
        txtName = new JTextField(); txtName.setEditable(false);
        txtIntake = new JTextField(); txtIntake.setEditable(false);
        txtEmail = new JTextField(); txtEmail.setEditable(false);
        txtContact = new JTextField(); txtContact.setEditable(false);
        txtAddress = new JTextField(); txtAddress.setEditable(false);
        
        addDetail(detailsForm, "Full Name:", txtName);
        addDetail(detailsForm, "Intake Code:", txtIntake);
        addDetail(detailsForm, "Email:", txtEmail);
        addDetail(detailsForm, "Contact:", txtContact);
        addDetail(detailsForm, "Address:", txtAddress);
        
        // layout fix
        JPanel imgContainer = new JPanel();
        imgContainer.add(lblImage);
        
        JPanel infoContainer = new JPanel(new BorderLayout());
        infoContainer.add(imgContainer, BorderLayout.NORTH);
        infoContainer.add(detailsForm, BorderLayout.CENTER);
        
        rightPanel.add(infoContainer, BorderLayout.CENTER);
        
        splitPane.setRightComponent(rightPanel);

        // footer
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // helper to add fields
    private void addDetail(JPanel p, String label, JTextField field) {
        p.add(new JLabel(label));
        p.add(field);
    }

    // populate list
    private void loadStudentList(String query) {
        tableModel.setRowCount(0);
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId); 
        
        for (String[] match : matches) {
            String id = match[1];
            String name = match[2];
            String position = match[5];
            String date = match[6];
            
            // basic search filter
            boolean matchesSearch = query.isEmpty() || name.toLowerCase().contains(query.toLowerCase());
            
            if (matchesSearch) {
                tableModel.addRow(new Object[]{id, name, position, date});
            }
        }
    }

    // populate details
    private void loadProfileDetails(String id) {
        String[] user = DatabaseHelper.getUserById(id); 
        if (user != null) {
            txtName.setText(user[3]);
            txtIntake.setText(user.length > 4 ? user[4] : "N/A");
            txtEmail.setText(user.length > 6 ? user[6] : "N/A"); 
            txtContact.setText(user.length > 7 ? user[7] : "N/A");
            txtAddress.setText(user.length > 8 ? user[8] : "N/A"); 

            // image logic
            File pfp = DatabaseHelper.getProfileImage(id); 
            if (pfp != null && pfp.exists()) {
                ImageIcon icon = new ImageIcon(pfp.getAbsolutePath());
                if (icon.getIconWidth() > 0) {
                    // scale it to fit
                    Image img = icon.getImage();
                    Image scaled = img.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    lblImage.setIcon(new ImageIcon(scaled));
                    lblImage.setText("");
                } else {
                    lblImage.setIcon(null);
                    lblImage.setText("Error");
                }
            } else {
                lblImage.setIcon(null);
                lblImage.setText("No Image");
            }
        }
    }
}