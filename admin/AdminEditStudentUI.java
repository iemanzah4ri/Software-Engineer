package admin;
import common.*; 

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// edit student info
public class AdminEditStudentUI extends JFrame {
    private JTextField searchField, userField, passField, nameField, matricField;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private String currentId;

    public AdminEditStudentUI() {
        //setup frame properties
        setTitle("Edit Student Details");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Edit Student Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);
        JSplitPane splitPane = new JSplitPane();

        //left panel search
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadStudents(searchField.getText()));
        searchPanel.add(new JLabel("Matric/Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        //table config
        String[] columns = {"ID", "Username", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(tableModel);

        //listen for clicks
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

        //right panel inputs
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

    // load student list
    private void loadStudents(String query) {
        //refresh list
        tableModel.setRowCount(0);
        List<String[]> users = DatabaseHelper.getUsersByRole("Student", query);
        for (String[] user : users) {
            tableModel.addRow(new Object[]{user[0], user[1], user[2]});
        }
    }

    // get student info
    private void loadStudentDetails(String id) {
        //get data
        String[] details = DatabaseHelper.getUserById(id);
        if (details != null) {
            currentId = details[0];
            userField.setText(details[1]);
            passField.setText(details[2]);
            nameField.setText(details[3]);
            matricField.setText(details[4]); 
        }
    }

    // save changes
    private void updateStudent() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Please select a student first.");
            return;
        }

        //check validation
        String newUser = userField.getText().trim();
        String newPass = passField.getText().trim();
        String newName = nameField.getText().trim();
        String newMatric = matricField.getText().trim();

        if (newUser.isEmpty() || newPass.isEmpty() || newName.isEmpty() || newMatric.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //write to file
        DatabaseHelper.updateStudent(currentId, newUser, newPass, newName, newMatric);
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        loadStudents(""); 
    }
}