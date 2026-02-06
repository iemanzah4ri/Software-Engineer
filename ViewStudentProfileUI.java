import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class ViewStudentProfileUI extends JFrame {

    private String supervisorId;
    private JTextField searchField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel lblImage;
    private JTextField txtName, txtEmail, txtContact, txtAddress, txtIntake;

    public ViewStudentProfileUI(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadStudentList("");
    }

    private void initComponents() {
        setTitle("Assigned Students & Profiles");
        setSize(1000, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Assigned Students", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(550); 
        add(splitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton btnSearch = new JButton("Search Name");
        btnSearch.addActionListener(e -> loadStudentList(searchField.getText()));
        
        searchPanel.add(new JLabel("Filter: "));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Position", "Date Matched"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row != -1) {
                    Object id = studentTable.getValueAt(row, 0);
                    loadProfileDetails(id.toString());
                }
            }
        });

        splitPane.setLeftComponent(leftPanel);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblImage = new JLabel("No Image", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(120, 120));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(lblImage, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; txtName = new JTextField(15); txtName.setEditable(false); rightPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Intake:"), gbc);
        gbc.gridx = 1; txtIntake = new JTextField(15); txtIntake.setEditable(false); rightPanel.add(txtIntake, gbc);

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(15); txtEmail.setEditable(false); rightPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 4; rightPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1; txtContact = new JTextField(15); txtContact.setEditable(false); rightPanel.add(txtContact, gbc);

        gbc.gridx = 0; gbc.gridy = 5; rightPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1; txtAddress = new JTextField(15); txtAddress.setEditable(false); rightPanel.add(txtAddress, gbc);

        splitPane.setRightComponent(rightPanel);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadStudentList(String query) {
        tableModel.setRowCount(0);
        List<String[]> matches = DBHelper.getMatchesForSupervisor(supervisorId);
        
        for (String[] match : matches) {
            String id = match[1];
            String name = match[2];
            String position = match[5];
            String date = match[6];
            
            boolean matchesSearch = query.isEmpty() || name.toLowerCase().contains(query.toLowerCase());
            
            if (matchesSearch) {
                tableModel.addRow(new Object[]{id, name, position, date});
            }
        }
    }

    private void loadProfileDetails(String id) {
        String[] user = DBHelper.getUserById(id);
        if (user != null) {
            txtName.setText(user[3]);
            txtIntake.setText(user.length > 4 ? user[4] : "N/A");
            txtEmail.setText(user.length > 6 ? user[6] : "N/A"); 
            txtContact.setText(user.length > 7 ? user[7] : "N/A");
            txtAddress.setText(user.length > 8 ? user[8] : "N/A"); 

            File pfp = DBHelper.getProfileImage(id);
            if (pfp != null && pfp.exists()) {
                ImageIcon icon = new ImageIcon(pfp.getAbsolutePath());
                if (icon.getIconWidth() > 0) {
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