import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.nio.file.*;

public class CompanyManageApplicationsUI extends JFrame {
    private String companyName;
    private String supervisorId;
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
        JButton btnViewResume = new JButton("View Resume");
        JButton btnDownloadResume = new JButton("Download Resume");
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnBack = new JButton("Back");

        // --- VIEW RESUME ACTION ---
        btnViewResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this, "Select a student."); return; }
            
            String studentId = appModel.getValueAt(r, 1).toString();
            File resume = DBHelper.getResumeFile(studentId);
            
            if (resume != null && resume.exists()) {
                try {
                    Desktop.getDesktop().open(resume);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Could not open file: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "No resume uploaded for this student.");
            }
        });

        // --- DOWNLOAD RESUME ACTION ---
        btnDownloadResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if (r == -1) { JOptionPane.showMessageDialog(this, "Select a student."); return; }
            
            String studentId = appModel.getValueAt(r, 1).toString();
            File resume = DBHelper.getResumeFile(studentId);
            
            if (resume != null && resume.exists()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setSelectedFile(new File(resume.getName()));
                
                if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    File target = chooser.getSelectedFile();
                    try {
                        Files.copy(resume.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        JOptionPane.showMessageDialog(this, "Resume downloaded successfully!");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No resume uploaded for this student.");
            }
        });

        btnApprove.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r==-1) { JOptionPane.showMessageDialog(this, "Select an application."); return; }
            
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
        
        p.add(btnBack); 
        p.add(btnViewResume);
        p.add(btnDownloadResume);
        p.add(btnReject); 
        p.add(btnApprove);
        
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