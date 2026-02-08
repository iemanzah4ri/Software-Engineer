package admin;
// imports
import common.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// edit academic supervisor details
public class AdminEditAcademicUI extends JFrame {
    private JTextField searchField, userField, passField, nameField;
    private JTable supervisorTable;
    private DefaultTableModel tableModel;
    private String currentId; 

    public AdminEditAcademicUI() {
        //setup basic window stuff
        setTitle("Edit Academic Supervisor Details");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header
        JLabel titleLabel = new JLabel("Edit Academic Supervisor Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);
        
        // split pane for list vs details
        JSplitPane splitPane = new JSplitPane();
        
        //left panel for list
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        // search function
        searchBtn.addActionListener(e -> loadSupervisors(searchField.getText()));
        searchPanel.add(new JLabel("ID/Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        // table setup
        String[] columns = {"ID", "Username", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        supervisorTable = new JTable(tableModel);
        
        //load details when clicked
        supervisorTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = supervisorTable.getSelectedRow();
                if (row != -1) {
                    // load info into right panel
                    loadSupervisorDetails(supervisorTable.getValueAt(row, 0).toString());
                }
            }
        });

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(supervisorTable), BorderLayout.CENTER);

        //right panel for editing
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // fields
        gbc.gridx = 0; gbc.gridy = 0; rightPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; userField = new JTextField(15); rightPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; passField = new JTextField(15); rightPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1; nameField = new JTextField(15); rightPanel.add(nameField, gbc);

        // modify button
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton modifyBtn = new JButton("Modify");
        modifyBtn.addActionListener(e -> updateSupervisor());
        rightPanel.add(modifyBtn, gbc);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        // initial load
        loadSupervisors("");
        setLocationRelativeTo(null);
    }

    // fetch from db
    private void loadSupervisors(String query) {
        //refresh the list
        tableModel.setRowCount(0);
        List<String[]> users = DatabaseHelper.getUsersByRole("Academic Supervisor", query);
        for (String[] user : users) {
            tableModel.addRow(new Object[]{user[0], user[1], user[2]});
        }
    }

    // load one user
    private void loadSupervisorDetails(String id) {
        //fill text fields
        String[] details = DatabaseHelper.getUserById(id);
        if (details != null) {
            currentId = details[0];
            userField.setText(details[1]);
            passField.setText(details[2]);
            nameField.setText(details[3]);
        }
    }

    // save changes
    private void updateSupervisor() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Please select a supervisor first.");
            return;
        }

        //validate inputs
        String newUser = userField.getText().trim();
        String newPass = passField.getText().trim();
        String newName = nameField.getText().trim();

        if (newUser.isEmpty() || newPass.isEmpty() || newName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //update db
        DatabaseHelper.updateAcademicSupervisor(currentId, newUser, newPass, newName);
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        // refresh list
        loadSupervisors("");
    }
}