import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ManageListingsUI extends JFrame {

    private DefaultTableModel model;
    private JTable table;
    
    private JTextField txtRegNo, txtCompany, txtLocation, txtJobTitle;
    private JTextArea txtJobDesc; // Changed to JTextArea

    public ManageListingsUI() {
        setTitle("Manage Company Account & Internship Listings");
        setSize(1000, 700); // Increased height slightly for the text area
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- TOP PANEL ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JLabel lblTitle = new JLabel("Manage Company Account and Internship Listings");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JTextField txtSearch = new JTextField(15);
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(220, 220, 220));
        
        JButton btnRefresh = new JButton("Refresh");
        JButton btnCreate = new JButton("Create New");
        
        searchPanel.add(txtSearch);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(btnSearch);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(btnRefresh);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(btnCreate);

        topPanel.add(lblTitle);
        topPanel.add(searchPanel);
        add(topPanel, BorderLayout.NORTH);

        // --- TABLE ---
        String[] cols = {"Reg No", "Company", "Location", "Job Title", "Job Desc", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(model);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtRegNo.setText(safeGet(row, 0));
                    txtCompany.setText(safeGet(row, 1));
                    txtLocation.setText(safeGet(row, 2));
                    txtJobTitle.setText(safeGet(row, 3));
                    txtJobDesc.setText(safeGet(row, 4)); // Sets text to the text area
                    // Reset scroll position to top
                    txtJobDesc.setCaretPosition(0);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 20, 0, 20));
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM FORM PANEL ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Reg No
        gbc.gridx=0; gbc.gridy=0; formPanel.add(new JLabel("Registration Number"), gbc);
        gbc.gridx=1; txtRegNo = new JTextField(15); txtRegNo.setEditable(false); formPanel.add(txtRegNo, gbc);

        // Row 1: Company
        gbc.gridx=0; gbc.gridy=1; formPanel.add(new JLabel("Company Name"), gbc);
        gbc.gridx=1; txtCompany = new JTextField(15); txtCompany.setEditable(false); formPanel.add(txtCompany, gbc);

        // Row 2: Location
        gbc.gridx=0; gbc.gridy=2; formPanel.add(new JLabel("Location"), gbc);
        gbc.gridx=1; txtLocation = new JTextField(15); txtLocation.setEditable(false); formPanel.add(txtLocation, gbc);

        // Row 3: Job Title
        gbc.gridx=0; gbc.gridy=3; formPanel.add(new JLabel("Job Title"), gbc);
        gbc.gridx=1; txtJobTitle = new JTextField(15); txtJobTitle.setEditable(false); formPanel.add(txtJobTitle, gbc);

        // Row 4: Job Description (UPDATED)
        gbc.gridx=0; gbc.gridy=4; 
        gbc.anchor = GridBagConstraints.NORTHWEST; // Align label to top
        formPanel.add(new JLabel("Job Description"), gbc);

        gbc.gridx=1; 
        gbc.fill = GridBagConstraints.BOTH; // Fill space
        gbc.weighty = 1.0; // Allow vertical growth
        
        txtJobDesc = new JTextArea(4, 20); // 4 rows, 20 cols
        txtJobDesc.setLineWrap(true);
        txtJobDesc.setWrapStyleWord(true);
        txtJobDesc.setEditable(false);
        
        JScrollPane descScroll = new JScrollPane(txtJobDesc);
        formPanel.add(descScroll, gbc);

        // Reset constraints for buttons
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 5: Buttons
        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionBtns.setBackground(Color.WHITE);
        
        JButton btnApprove = new JButton("Approve Listing");
        btnApprove.setBackground(new Color(144, 238, 144)); 
        
        JButton btnReject = new JButton("Reject");
        btnReject.setBackground(new Color(255, 102, 102));

        actionBtns.add(btnApprove);
        actionBtns.add(btnReject);

        gbc.gridx=1; gbc.gridy=5; 
        formPanel.add(actionBtns, gbc);

        // -- Right Side: Back Button --
        JPanel rightBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBtnPanel.setBackground(Color.WHITE);
        JButton btnBack = new JButton("Back Home");
        btnBack.setBackground(new Color(220, 220, 220));
        rightBtnPanel.add(btnBack);

        bottomPanel.add(formPanel, BorderLayout.CENTER); // Changed to CENTER to allow resizing
        bottomPanel.add(rightBtnPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);

        loadListings();

        // --- ACTIONS ---
        btnCreate.addActionListener(e -> new CreateListingUI("Admin").setVisible(true));
        
        btnRefresh.addActionListener(e -> loadListings());

        btnApprove.addActionListener(e -> {
            if(txtRegNo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a listing first.");
                return;
            }
            DBHelper.approveListing(txtRegNo.getText());
            loadListings();
            JOptionPane.showMessageDialog(this, "Listing Approved!");
        });

        btnReject.addActionListener(e -> {
            if(txtRegNo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a listing first.");
                return;
            }
            DBHelper.rejectListing(txtRegNo.getText());
            loadListings();
            JOptionPane.showMessageDialog(this, "Listing Rejected.");
        });

        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminHome().setVisible(true);
        });
    }

    private void loadListings() {
        model.setRowCount(0);
        List<String[]> list = DBHelper.getAllListings();
        for(String[] row : list) {
            if (row.length >= 6) {
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], row[4], row[5]});
            }
        }
    }

    private String safeGet(int row, int col) {
        if (table.getValueAt(row, col) != null) {
            return table.getValueAt(row, col).toString();
        }
        return "";
    }
}