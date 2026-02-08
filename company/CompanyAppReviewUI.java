package company;
// importing libraries
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.List;

// screen for supervisors to look at student applications
public class CompanyAppReviewUI extends JFrame {
    // variables to keep track of stuff
    private String companyName;
    private JTable appTable;
    private DefaultTableModel appModel;

    // constructor called when window opens
    public CompanyAppReviewUI(String supervisorId, String companyName) {
        // save company name, handle null just in case
        this.companyName = (companyName == null) ? "" : companyName.trim();
        // build ui
        initComponents();
        // fill table
        loadApplications();
    }

    // setting up the layout and buttons
    private void initComponents() {
        // window title
        setTitle("Manage Applications - " + companyName);
        setSize(950, 500);
        // center on screen
        setLocationRelativeTo(null);
        // close this window only
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header label
        JLabel title = new JLabel("Pending Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title, BorderLayout.NORTH);

        // table setup
        String[] cols = {"App ID", "Student ID", "Student Name", "Job Title", "Listing ID", "Status"};
        // make table not editable
        appModel = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        appTable = new JTable(appModel);
        // add scrolling
        add(new JScrollPane(appTable), BorderLayout.CENTER);

        // button panel at bottom
        JPanel p = new JPanel();
        JButton btnViewResume = new JButton("View Resume");
        JButton btnDownloadResume = new JButton("Download Resume");
        JButton btnOffer = new JButton("Send Offer");
        JButton btnReject = new JButton("Reject");
        JButton btnBack = new JButton("Back");
        
        // colors for important buttons
        btnOffer.setBackground(new Color(152, 251, 152)); // light green
        btnReject.setBackground(new Color(255, 102, 102)); // light red

        // logic for viewing resume
        btnViewResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            // check if row selected
            if(r != -1) {
                String sid = appTable.getValueAt(r, 1).toString();
                // ask db for file
                File f = DatabaseHelper.getResumeFile(sid); 
                // try to open it
                if(f!=null && f.exists()) {
                     try { Desktop.getDesktop().open(f); } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Cannot open file."); }
                } else JOptionPane.showMessageDialog(this, "No resume found.");
            }
        });

        // logic for downloading resume
        btnDownloadResume.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r != -1) {
                String sid = appTable.getValueAt(r, 1).toString();
                File f = DatabaseHelper.getResumeFile(sid); 
                if(f!=null && f.exists()) {
                    // file chooser dialog
                    JFileChooser fc = new JFileChooser();
                    fc.setSelectedFile(new File(f.getName()));
                    // if user clicks save
                    if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                         try { 
                             // copy file to new location
                             java.nio.file.Files.copy(f.toPath(), fc.getSelectedFile().toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                             JOptionPane.showMessageDialog(this, "Saved!");
                         } catch(Exception ex) { JOptionPane.showMessageDialog(this, "Error saving."); }
                    }
                } else JOptionPane.showMessageDialog(this, "No resume found.");
            }
        });

        // sending an offer
        btnOffer.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r == -1) return; // do nothing if no row
            String appId = appTable.getValueAt(r, 0).toString();
            
            // upload contract pdf
            JFileChooser ch = new JFileChooser();
            ch.setDialogTitle("Upload Offer Letter / Contract (PDF)");
            if(ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File contract = ch.getSelectedFile();
                // ask for start date
                String date = JOptionPane.showInputDialog(this, "Enter Start Date (YYYY-MM-DD):");
                if(date != null && !date.isEmpty()) {
                    // save offer to db
                    DatabaseHelper.sendOffer(appId, date, contract); 
                    JOptionPane.showMessageDialog(this, "Offer Sent!");
                    // refresh list
                    loadApplications();
                }
            }
        });

        // rejecting student
        btnReject.addActionListener(e -> {
            int r = appTable.getSelectedRow();
            if(r == -1) return;
            String appId = appTable.getValueAt(r, 0).toString();
            // update status to rejected
            DatabaseHelper.rejectApplication(appId); 
            JOptionPane.showMessageDialog(this, "Application Rejected.");
            loadApplications();
        });

        // close window
        btnBack.addActionListener(e -> dispose());
        
        // add buttons to panel
        p.add(btnBack); 
        p.add(btnViewResume);
        p.add(btnDownloadResume);
        p.add(btnReject); 
        p.add(btnOffer);
        
        add(p, BorderLayout.SOUTH);
    }

    // function to populate table
    private void loadApplications() {
        // clear table
        appModel.setRowCount(0);
        // get data sources
        List<String[]> listings = DatabaseHelper.getAllListings(); 
        List<String[]> allApps = DatabaseHelper.getAllMatches(); 

        File file = new File("database/applications.txt");
        if (!file.exists()) return;
        
        // read file manually line by line
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // check formatting
                if (data.length >= 5 && data[3].equalsIgnoreCase(companyName) && data[4].equalsIgnoreCase("Pending")) {
                    // get student name
                    String[] student = DatabaseHelper.getUserById(data[1]); 
                    
                    // find job title
                    String jobTitle = "Unknown Position";
                    for(String[] lst : listings) {
                        if(lst[0].equals(data[2])) {
                            jobTitle = lst[4];
                            break;
                        }
                    }
                    
                    // add row
                    appModel.addRow(new Object[]{data[0], data[1], (student!=null?student[3]:"Unknown"), jobTitle, data[2], data[4]});
                }
            }
        } catch (IOException e) {}
    }
}