//interface to modify company supervisor details
//allows updating company name and position
package admin;
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminEditCompanyUI extends JFrame {
    private JTextField searchField, userField, passField, nameField, companyField, positionField, emailField;
    private JTable supervisorTable;
    private DefaultTableModel tableModel;
    private String currentId;

    public AdminEditCompanyUI() {
        setTitle("Edit Company Supervisor Details");
        setSize(800, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Edit Company Supervisor Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();

        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadSupervisors(searchField.getText()));
        searchPanel.add(new JLabel("Name/ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        String[] columns = {"ID", "Username", "Name", "Company"};
        tableModel = new DefaultTableModel(columns, 0) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
        };
        supervisorTable = new JTable(tableModel);

        supervisorTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = supervisorTable.getSelectedRow();
                if (row != -1) {
                    String id = supervisorTable.getValueAt(row, 0).toString();
                    loadSupervisorDetails(id);
                }
            }
        });

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(supervisorTable), BorderLayout.CENTER);

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

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Company:"), gbc);
        gbc.gridx = 1; companyField = new JTextField(15); rightPanel.add(companyField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; rightPanel.add(new JLabel("Position:"), gbc);
        gbc.gridx = 1; positionField = new JTextField(15); rightPanel.add(positionField, gbc);

        gbc.gridx = 0; gbc.gridy = 5; rightPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; emailField = new JTextField(15); rightPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        JButton modifyBtn = new JButton("Modify");
        modifyBtn.setBackground(new Color(220, 220, 220));
        modifyBtn.addActionListener(e -> updateSupervisor());
        rightPanel.add(modifyBtn, gbc);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(350);
        add(splitPane, BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);

        loadSupervisors("");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadSupervisors(String query) {
        tableModel.setRowCount(0);
        List<String[]> users = DatabaseHelper.getUsersByRole("Company Supervisor", query); // UPDATED
        for (String[] user : users) {
            tableModel.addRow(new Object[]{user[0], user[1], user[2], user[3]});
        }
    }

    private void loadSupervisorDetails(String id) {
        String[] details = DatabaseHelper.getUserById(id); // UPDATED
        if (details != null) {
            currentId = details[0];
            userField.setText(details[1]);
            passField.setText(details[2]);
            nameField.setText(details[3]);
            positionField.setText(details[4]); 
            emailField.setText(details[6]);    
            companyField.setText(details[8]);  
        }
    }

    private void updateSupervisor() {
        if (currentId == null) return;
        
        String newUser = userField.getText();
        String newPass = passField.getText();
        String newName = nameField.getText();
        String newComp = companyField.getText();
        String newPos = positionField.getText();
        String newEmail = emailField.getText();

        DatabaseHelper.updateCompanySupervisor(currentId, newUser, newPass, newName, newPos, newComp, newEmail); // UPDATED
        
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        loadSupervisors(""); 
    }
}