import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StudentTrack extends JFrame {
    private final String studentId;
    private DefaultTableModel tableModel;
    private JTable tblApps;

    public StudentTrack(String studentId, String studentName) {
        this.studentId = studentId;
        initComponents();
        loadApplications();
    }

    private void initComponents() {
        setTitle("Track Applications");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        String[] cols = {"App ID", "Company", "Internship ID", "Status"};
        tableModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        tblApps = new JTable(tableModel);
        
        add(new JScrollPane(tblApps), BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnViewContract = new JButton("View Contract");
        btnViewContract.setBackground(new Color(135, 206, 250));
        btnViewContract.addActionListener(e -> viewContract());
        
        JButton btnAccept = new JButton("Accept Offer");
        btnAccept.setBackground(new Color(144, 238, 144));
        btnAccept.addActionListener(e -> acceptOffer());
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadApplications());
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        
        bottomPanel.add(btnViewContract);
        bottomPanel.add(btnAccept);
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);
        
        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        List<String[]> apps = DBHelper.getApplicationsByStudent(studentId);
        for (String[] a : apps) {
            tableModel.addRow(new Object[]{a[0], a[3], a[2], a[4]});
        }
    }
    
    private void viewContract() {
        int row = tblApps.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an offer to view."); return; }
        
        String status = tableModel.getValueAt(row, 3).toString();
        if (!status.equalsIgnoreCase("Offered") && !status.equalsIgnoreCase("Accepted")) {
            JOptionPane.showMessageDialog(this, "This application does not have a contract yet.");
            return;
        }
        
        String appId = tableModel.getValueAt(row, 0).toString();
        File contract = DBHelper.getContractFile(appId);
        
        if (contract != null && contract.exists()) {
            try {
                Desktop.getDesktop().open(contract);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening contract: " + ex.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Contract file not found.");
        }
    }
    
    private void acceptOffer() {
        int row = tblApps.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an offer to accept."); return; }
        
        String status = tableModel.getValueAt(row, 3).toString();
        if (status.equalsIgnoreCase("Position Filled")) {
            JOptionPane.showMessageDialog(this, "Cannot accept. This position has been filled by another candidate.");
            return;
        }
        if (!status.equalsIgnoreCase("Offered")) {
            JOptionPane.showMessageDialog(this, "You can only accept 'Offered' applications.");
            return;
        }
        
        String[] student = DBHelper.getUserById(studentId);
        if (student != null && student.length > 9 && student[9].equalsIgnoreCase("Placed")) {
            JOptionPane.showMessageDialog(this, "You are already placed in an internship!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to accept this offer?\nThis will finalize your placement.", "Confirm Acceptance", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String appId = tableModel.getValueAt(row, 0).toString();
            boolean success = DBHelper.acceptOffer(appId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Offer Accepted! You have been placed.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed. This position has already been filled by another candidate.");
            }
            loadApplications();
        }
    }
}