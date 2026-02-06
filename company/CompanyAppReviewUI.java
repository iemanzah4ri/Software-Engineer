//interface to review student applications
//allows supervisors to send offers or reject applicants
package company;
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.List;

public class CompanyAppReviewUI extends JFrame {
    private String companyName;
    private JTable appTable;
    private DefaultTableModel appModel;

    public CompanyAppReviewUI(String supervisorId, String companyName) {
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

        String[] cols = {"App ID", "Student ID", "Student Name", "Job Title", "Listing ID", "Status"};
        appModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        appTable = new JTable(appModel);
        add(new JScrollPane(appTable), BorderLayout.CENTER);

        JPanel p = new JPanel();
        JButton btnViewResume = new JButton("View Resume");
        JButton btnDownloadResume = new JButton("Download Resume");
        JButton btnOffer = new JButton("Send Offer");
        JButton btnReject = new JButton("Reject");
        JButton btnBack = new JButton("Back");
        
        btnOffer.setBackground(new Color(152, 251, 152));
        btnReject.setBackground(new Color(255, 102, 102));

        btnViewResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r != -1) {
                String sid = appTable.getValueAt(r, 1).toString();
                File f = DatabaseHelper.getResumeFile(sid); // UPDATED
                if(f!=null && f.exists()) {
                     try { Desktop.getDesktop().open(f); } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Cannot open file."); }
                } else JOptionPane.showMessageDialog(this, "No resume found.");
            }
        });

        btnDownloadResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r != -1) {
                String sid = appTable.getValueAt(r, 1).toString();
                File f = DatabaseHelper.getResumeFile(sid); // UPDATED
                if(f!=null && f.exists()) {
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(f.getName()));
                    if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                         try { 
                             java.nio.file.Files.copy(f.toPath(), fc.getSelectedFile().toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                             JOptionPane.showMessageDialog(this, "Saved!");
                         } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error saving."); }
                    }
                } else JOptionPane.showMessageDialog(this, "No resume found.");
            }
        });

        btnOffer.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r == -1) return;
            String appId = appTable.getValueAt(r, 0).toString();
            
            JFileChooser ch = new JFileChooser();
            ch.setDialogTitle("Upload Offer Letter / Contract (PDF)");
            if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File contract = ch.getSelectedFile();
                String date = JOptionPane.showInputDialog(this, "Enter Start Date (YYYY-MM-DD):");
                if(date != null && !date.isEmpty()) {
                    DatabaseHelper.sendOffer(appId, date, contract); // UPDATED
                    JOptionPane.showMessageDialog(this, "Offer Sent!");
                    loadApplications();
                }
            }
        });

        btnReject.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r == -1) return;
            String appId = appTable.getValueAt(r, 0).toString();
            DatabaseHelper.rejectApplication(appId); // UPDATED
            JOptionPane.showMessageDialog(this, "Application Rejected.");
            loadApplications();
        });

        btnBack.addActionListener(e -> dispose());
        
        p.add(btnBack); 
        p.add(btnViewResume);
        p.add(btnDownloadResume);
        p.add(btnReject); 
        p.add(btnOffer);
        
        add(p, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        appModel.setRowCount(0);
        List<String[]> listings = DatabaseHelper.getAllListings(); // UPDATED
        List<String[]> allApps = DatabaseHelper.getAllMatches(); //placeholder

        File file = new File("database/applications.txt");
        if (!file.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // data: appId, studentId, listingId, companyName, status, ...
                if (data.length >= 5 && data[3].equalsIgnoreCase(companyName) && data[4].equalsIgnoreCase("Pending")) {
                    String[] student = DatabaseHelper.getUserById(data[1]); // UPDATED
                    
                    String jobTitle = "Unknown Position";
                    for(String[] lst : listings) {
                        if(lst[0].equals(data[2])) {
                            jobTitle = lst[4];
                            break;
                        }
                    }
                    
                    appModel.addRow(new Object[]{data[0], data[1], (student!=null?student[3]:"Unknown"), jobTitle, data[2], data[4]});
                }
            }
        } catch (IOException e) {}
    }
}