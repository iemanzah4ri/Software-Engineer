import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerifyLogbookUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private String currentSupervisorName;

    private JTextField txtDate, txtTime;
    private JTextArea txtActivity;
    private JButton btnEdit;

    public VerifyLogbookUI(String supervisorName) {
        this.currentSupervisorName = supervisorName;
        initComponents();
        loadLogbooks(); 
    }

    private void initComponents() {
        setTitle("Verify Logbook");
        setSize(1000, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Verify Logbook", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600); 
        add(splitPane, BorderLayout.CENTER);

        String[] cols = {"LogID", "StudentID", "Date", "Activity", "Hours", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtDate.setText(model.getValueAt(row, 2).toString());
                txtActivity.setText(model.getValueAt(row, 3).toString());
                txtTime.setText(model.getValueAt(row, 4).toString());
            }
        });

        splitPane.setLeftComponent(new JScrollPane(table));

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        rightPanel.add(new JLabel("Date:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtDate = new JTextField(); 
        txtDate.setEditable(false);
        rightPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        rightPanel.add(new JLabel("Hours/Time:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtTime = new JTextField();
        txtTime.setEditable(false);
        rightPanel.add(txtTime, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; 
        gbc.anchor = GridBagConstraints.NORTHWEST;
        rightPanel.add(new JLabel("Activity:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        txtActivity = new JTextArea();
        txtActivity.setLineWrap(true);
        txtActivity.setWrapStyleWord(true);
        txtActivity.setEditable(false);
        rightPanel.add(new JScrollPane(txtActivity), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        
        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> toggleEdit());
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(144, 238, 144)); 
        btnVerify.addActionListener(e -> verifyEntry());
        
        btnPanel.add(btnBack);
        btnPanel.add(btnEdit);
        btnPanel.add(btnVerify);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
    }

    private void loadLogbooks() {
        model.setRowCount(0);
        List<String[]> logs = DBHelper.getAllLogbooks();
        for (String[] l : logs) {
            model.addRow(l);
        }
    }

    private void toggleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an entry first.");
            return;
        }

        if (btnEdit.getText().equals("Edit")) {
            setFieldsEditable(true);
            btnEdit.setText("Save");
        } else {
            String logId = model.getValueAt(row, 0).toString();
            DBHelper.updateLogbookEntry(logId, txtDate.getText(), txtActivity.getText(), txtTime.getText());
            JOptionPane.showMessageDialog(this, "Changes Saved!");
            setFieldsEditable(false);
            btnEdit.setText("Edit");
            loadLogbooks(); 
        }
    }

    private void setFieldsEditable(boolean editable) {
        txtDate.setEditable(editable);
        txtTime.setEditable(editable);
        txtActivity.setEditable(editable);
    }

    private void verifyEntry() {
        int row = table.getSelectedRow();
        if (row != -1) {
            String logId = model.getValueAt(row, 0).toString();
            DBHelper.updateLogbookStatus(logId, "Verified");
            JOptionPane.showMessageDialog(this, "Logbook Entry Verified!");
            loadLogbooks();
        } else {
            JOptionPane.showMessageDialog(this, "Select an entry to verify.");
        }
    }
}