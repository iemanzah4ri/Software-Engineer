import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerifyLogbookUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private String currentSupervisorName;

    private JTextField txtStudentName, txtDate, txtTime, txtActivity;
    private JButton btnEdit;

    public VerifyLogbookUI(String supervisorName) {
        this.currentSupervisorName = supervisorName;
        initComponents();
        loadLogbooks(); 
    }

    private void initComponents() {
        setTitle("Verify Logbook");
        setSize(950, 550); 
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

        // Table
        String[] cols = {"LogID", "StudentID", "Date", "Activity", "Hours", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        
        // Listener to populate fields
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtDate.setText(model.getValueAt(row, 2).toString());
                txtActivity.setText(model.getValueAt(row, 3).toString());
                txtTime.setText(model.getValueAt(row, 4).toString());
            }
        });

        splitPane.setLeftComponent(new JScrollPane(table));

        // Form
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Date:"));
        txtDate = new JTextField(); 
        txtDate.setEditable(false);
        formPanel.add(txtDate);

        formPanel.add(new JLabel("Activity:"));
        txtActivity = new JTextField();
        txtActivity.setEditable(false);
        formPanel.add(txtActivity);
        
        formPanel.add(new JLabel("Hours/Time:"));
        txtTime = new JTextField();
        txtTime.setEditable(false);
        formPanel.add(txtTime);

        btnEdit = new JButton("Edit");
        btnEdit.addActionListener(e -> toggleEdit());
        formPanel.add(btnEdit);
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(Color.GREEN);
        btnVerify.addActionListener(e -> verifyEntry());
        formPanel.add(btnVerify);

        splitPane.setRightComponent(formPanel);
    }

    private void loadLogbooks() {
        model.setRowCount(0);
        // Load all logbooks (In a real system you might filter by assigned students)
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
            // Use new DBHelper method
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
            // Use new DBHelper method
            DBHelper.updateLogbookStatus(logId, "Verified");
            JOptionPane.showMessageDialog(this, "Logbook Entry Verified!");
            loadLogbooks();
        } else {
            JOptionPane.showMessageDialog(this, "Select an entry to verify.");
        }
    }
}