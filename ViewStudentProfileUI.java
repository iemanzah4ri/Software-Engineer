import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ViewStudentProfileUI extends JFrame {

    private String supervisorId;
    private JTextField searchField;
    private JTable studentTable;
    private DefaultTableModel tableModel;

    private JLabel lblImage;
    private JTextField txtName, txtEmail, txtContact, txtAddress;

    public ViewStudentProfileUI(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadStudentList("");
    }

    private void initComponents() {
        setTitle("View Student Profile");
        setSize(900, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("View Student Profile", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(400); 
        add(splitPane, BorderLayout.CENTER);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnSearch.addActionListener(e -> loadStudentList(searchField.getText()));
        
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Intake"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        studentTable = new JTable(tableModel);
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblImage = new JLabel("Profile Picture", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(120, 120));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(lblImage, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; txtName = new JTextField(15); txtName.setEditable(false); rightPanel.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; txtEmail = new JTextField(15); txtEmail.setEditable(false); rightPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Contact Number:"), gbc);
        gbc.gridx = 1; txtContact = new JTextField(15); txtContact.setEditable(false); rightPanel.add(txtContact, gbc);

        gbc.gridx = 0; gbc.gridy = 4; rightPanel.add(new JLabel("Address:"), gbc);
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
            String studentId = match[1]; 
            
            String[] studentUser = DBHelper.getUserById(studentId);
            
            if (studentUser != null) {
                boolean matchesSearch = query.isEmpty() || 
                                      studentUser[3].toLowerCase().contains(query.toLowerCase());
                
                if (matchesSearch) {
                    tableModel.addRow(new Object[]{studentUser[0], studentUser[3], studentUser[4]});
                }
            }
        }
    }

    private void loadProfileDetails(String id) {
        String[] user = DBHelper.getUserById(id);
        if (user != null) {
            txtName.setText(user[3]);
            txtEmail.setText(user[6]); 
            txtContact.setText(user[7]);
            txtAddress.setText(user[8]); 
        }
    }
}