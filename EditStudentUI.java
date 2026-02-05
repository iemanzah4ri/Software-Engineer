import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EditStudentUI extends JFrame {
    private JTextField searchField, userField, passField, nameField, matricField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private String currentId;

    public EditStudentUI() {
        setTitle("Edit Student Details");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JLabel titleLabel = new JLabel("Edit Student Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();

        // Search & List
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadStudents(searchField.getText()));
        searchPanel.add(new JLabel("Matric/Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        String[] columns = {"ID", "Username", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(tableModel);

        studentTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row != -1) {
                    loadStudentDetails(studentTable.getValueAt(row, 0).toString());
                }
            }
        });

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Edit Form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; rightPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; userField = new JTextField(15); rightPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; passField = new JTextField(15); rightPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1; nameField = new JTextField(15); rightPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Matric No:"), gbc);
        gbc.gridx = 1; matricField = new JTextField(15); rightPanel.add(matricField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton modifyBtn = new JButton("Modify");
        modifyBtn.addActionListener(e -> updateStudent());
        rightPanel.add(modifyBtn, gbc);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        loadStudents("");
        setLocationRelativeTo(null);
    }

    private void loadStudents(String query) {
        tableModel.setRowCount(0);
        List<String[]> users = DBHelper.getUsersByRole("Student", query);
        for (String[] user : users) {
            tableModel.addRow(new Object[]{user[0], user[1], user[2]});
        }
    }

    private void loadStudentDetails(String id) {
        String[] details = DBHelper.getUserById(id);
        if (details != null) {
            currentId = details[0];
            userField.setText(details[1]);
            passField.setText(details[2]);
            nameField.setText(details[3]);
            matricField.setText(details[4]); 
        }
    }

    private void updateStudent() {
        if (currentId == null) return;
        // Use the new specific wrapper in DBHelper
        DBHelper.updateStudent(currentId, userField.getText(), passField.getText(), nameField.getText(), matricField.getText());
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        loadStudents(""); 
    }
}