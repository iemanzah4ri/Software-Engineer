package admin;
// imports
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// screen to approve or reject job postings
public class AdminListingManagerUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtReg, txtComp, txtLoc, txtJob, txtDesc;

    // constructor
    public AdminListingManagerUI() {
        initComponents();
        loadListings();
    }

    // setup interface
    private void initComponents() {
        setTitle("Manage Company Account & Internship Listings");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // top bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("Manage Company Account & Internship Listings", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // create button
        JButton btnCreate = new JButton("Create New");
        btnCreate.addActionListener(e -> createNewListing());
        
        JPanel header = new JPanel(new BorderLayout());
        header.add(title, BorderLayout.CENTER);
        
        // toolbar buttons
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toolbar.add(new JTextField(20)); 
        toolbar.add(new JButton("Search"));
        toolbar.add(new JButton("Refresh"));
        toolbar.add(btnCreate);
        header.add(toolbar, BorderLayout.SOUTH);

        add(header, BorderLayout.NORTH);

        // table setup
        String[] cols = {"ID", "Reg No", "Company", "Location", "Job Title", "Job Desc", "Status"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        
        // set col widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(200); 
        
        // fill fields when row clicked
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtReg.setText(model.getValueAt(row, 1).toString());
                txtComp.setText(model.getValueAt(row, 2).toString());
                txtLoc.setText(model.getValueAt(row, 3).toString());
                txtJob.setText(model.getValueAt(row, 4).toString());
                txtDesc.setText(model.getValueAt(row, 5).toString());
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // bottom form
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtReg = new JTextField(15);
        txtComp = new JTextField(15);
        txtLoc = new JTextField(15);
        txtJob = new JTextField(15);
        txtDesc = new JTextField(15);

        // add fields to form
        addFormField(formPanel, gbc, 0, "Registration Number", txtReg);
        addFormField(formPanel, gbc, 1, "Company Name", txtComp);
        addFormField(formPanel, gbc, 2, "Location", txtLoc);
        addFormField(formPanel, gbc, 3, "Job Title", txtJob);
        addFormField(formPanel, gbc, 4, "Job Description", txtDesc);

        // action buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnApprove = new JButton("Approve Listing");
        btnApprove.setBackground(new Color(144, 238, 144)); // green
        btnApprove.addActionListener(e -> updateStatus("Approved"));
        
        JButton btnReject = new JButton("Reject");
        btnReject.setBackground(new Color(255, 102, 102)); // red
        btnReject.addActionListener(e -> updateStatus("Rejected"));
        
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminDashboardUI().setVisible(true);
        });

        btnPanel.add(btnApprove);
        btnPanel.add(btnReject);
        btnPanel.add(btnBack);

        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // helper to add label + textfield
    private void addFormField(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = row; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(field, gbc);
    }

    // get all listings from db
    private void loadListings() {
        model.setRowCount(0);
        List<String[]> list = DatabaseHelper.getAllListings(); 
        
        for (String[] row : list) {
            if (row.length >= 7) {
                model.addRow(new Object[]{
                    row[0], 
                    row[1], 
                    row[2], 
                    row[3], 
                    row[4], 
                    row[5], 
                    row[6]
                });
            }
        }
    }

    // approve or reject
    private void updateStatus(String newStatus) {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a listing from the table.");
            return;
        }

        String listingId = model.getValueAt(row, 0).toString(); 
        
        // call db helper
        if (newStatus.equals("Approved")) {
            DatabaseHelper.approveListing(listingId);
        } else {
            DatabaseHelper.rejectListing(listingId);
        }
        
        JOptionPane.showMessageDialog(this, "Listing marked as " + newStatus);
        // refresh
        loadListings(); 
    }

    // make a new one manually
    private void createNewListing() {
        String reg = txtReg.getText();
        String comp = txtComp.getText();
        String loc = txtLoc.getText();
        String job = txtJob.getText();
        String desc = txtDesc.getText();

        if(reg.isEmpty() || comp.isEmpty() || loc.isEmpty() || job.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all details before creating.");
            return;
        }

        // save with pending status
        DatabaseHelper.saveListing(reg, comp, loc, job, desc, "Pending"); 
        JOptionPane.showMessageDialog(this, "New Listing Created!");
        loadListings();
    }
}