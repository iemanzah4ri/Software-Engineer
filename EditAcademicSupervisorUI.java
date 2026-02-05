import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EditAcademicSupervisorUI extends JFrame {
    private JTextField searchField, userField, passField, nameField;
    private JTable supervisorTable;
    private DefaultTableModel tableModel;
    private String currentId; 

    public EditAcademicSupervisorUI() {
        setTitle("Edit Academic Supervisor Details");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Edit Academic Supervisor Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);


          // --- Footer ---
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton backBtn = new JButton("Back Home");
        backBtn.addActionListener(e -> dispose());
        footerPanel.add(backBtn);
        add(footerPanel, BorderLayout.SOUTH);
        
        JSplitPane splitPane = new JSplitPane();
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> loadSupervisors(searchField.getText()));
        searchPanel.add(new JLabel("ID/Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        String[] columns = {"ID", "Username", "Full Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        supervisorTable = new JTable(tableModel);
        
        supervisorTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = supervisorTable.getSelectedRow();
                if (row != -1) {
                    loadSupervisorDetails(supervisorTable.getValueAt(row, 0).toString());
                }
            }
        });

        leftPanel.add(searchPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(supervisorTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0; rightPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; userField = new JTextField(15); rightPanel.add(userField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; passField = new JTextField(15); rightPanel.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Fullname:"), gbc);
        gbc.gridx = 1; nameField = new JTextField(15); rightPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton modifyBtn = new JButton("Modify");
        modifyBtn.addActionListener(e -> updateSupervisor());
        rightPanel.add(modifyBtn, gbc);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);
        add(splitPane, BorderLayout.CENTER);

        loadSupervisors("");
        setLocationRelativeTo(null);
    }

    private void loadSupervisors(String query) {
        tableModel.setRowCount(0);
        List<String[]> users = DBHelper.getUsersByRole("Academic Supervisor", query);
        for (String[] user : users) {
            tableModel.addRow(new Object[]{user[0], user[1], user[2]});
        }
    }

    private void loadSupervisorDetails(String id) {
        String[] details = DBHelper.getUserById(id);
        if (details != null) {
            currentId = details[0];
            userField.setText(details[1]);
            passField.setText(details[2]);
            nameField.setText(details[3]);
        }
    }

    private void updateSupervisor() {
        if (currentId == null) {
            JOptionPane.showMessageDialog(this, "Please select a supervisor first.");
            return;
        }
        // Use the new wrapper
        DBHelper.updateAcademicSupervisor(currentId, userField.getText(), passField.getText(), nameField.getText());
        JOptionPane.showMessageDialog(this, "Updated Successfully!");
        loadSupervisors("");
    }
}