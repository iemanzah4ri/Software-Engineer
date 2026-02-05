import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class CompanyManageApplicationsUI extends JFrame {
    private String companyName;
    private String supervisorId; // New Field
    private JTable appTable;
    private DefaultTableModel appModel;

    public CompanyManageApplicationsUI(String supervisorId, String companyName) {
        this.supervisorId = supervisorId;
        this.companyName = (companyName == null) ? "" : companyName.trim();
        initComponents();
        loadApplications();
    }

    private void initComponents() {
        setTitle("Manage Applications - " + companyName);
        setSize(950, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Pending Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        String[] cols = {"App ID", "Student ID", "Student Name", "Listing RegNo", "Status"};
        appModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        appTable = new JTable(appModel);
        add(new JScrollPane(appTable), BorderLayout.CENTER);

        JPanel p = new JPanel();
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnBack = new JButton("Back");

        btnApprove.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r==-1) { JOptionPane.showMessageDialog(this, "Select an application."); return; }
            
            // Pass Supervisor ID to save match correctly
            String appId = appModel.getValueAt(r, 0).toString();
            DBHelper.approveApplication(appId, supervisorId);
            
            JOptionPane.showMessageDialog(this, "Approved and Student Placed!");
            loadApplications();
        });

        btnReject.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r==-1) return;
            DBHelper.rejectApplication(appModel.getValueAt(r, 0).toString());
            loadApplications();
        });

        btnBack.addActionListener(e -> dispose());
        p.add(btnBack); p.add(btnReject); p.add(btnApprove);
        add(p, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        appModel.setRowCount(0);
        File file = new File("database/applications.txt");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5 && data[3].equalsIgnoreCase(companyName) && data[4].equalsIgnoreCase("Pending")) {
                    String[] student = DBHelper.getUserById(data[1]);
                    appModel.addRow(new Object[]{data[0], data[1], (student!=null?student[3]:"Unknown"), data[2], data[4]});
                }
            }
        } catch (Exception e) {}
    }
}