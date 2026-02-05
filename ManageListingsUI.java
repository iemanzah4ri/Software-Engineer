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
    
    private JTextField txtRegNo, txtCompany, txtLocation;
    private JTextField txtJobDesc; 

    public ManageListingsUI() {
        setTitle("Manage Company Account & Internship Listings");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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

        String[] cols = {"Registration Number", "Company Name", "Location", "Job Description", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtRegNo.setText(model.getValueAt(row, 0).toString());
                    txtCompany.setText(model.getValueAt(row, 1).toString());
                    txtLocation.setText(model.getValueAt(row, 2).toString());
                    txtJobDesc.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 20, 0, 20));
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx=0; gbc.gridy=0; formPanel.add(new JLabel("Registration Number"), gbc);
        gbc.gridx=1; txtRegNo = new JTextField(15); formPanel.add(txtRegNo, gbc);

        gbc.gridx=0; gbc.gridy=1; formPanel.add(new JLabel("Company Name"), gbc);
        gbc.gridx=1; txtCompany = new JTextField(15); formPanel.add(txtCompany, gbc);

        gbc.gridx=0; gbc.gridy=2; formPanel.add(new JLabel("Location"), gbc);
        gbc.gridx=1; txtLocation = new JTextField(15); formPanel.add(txtLocation, gbc);

        gbc.gridx=0; gbc.gridy=3; formPanel.add(new JLabel("Job Description"), gbc);
        gbc.gridx=1; txtJobDesc = new JTextField(15); formPanel.add(txtJobDesc, gbc);

        JPanel actionBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionBtns.setBackground(Color.WHITE);
        
        JButton btnApprove = new JButton("Approve Listing");
        btnApprove.setBackground(new Color(200, 200, 200));
        
        JButton btnReject = new JButton("Reject");
        btnReject.setBackground(new Color(255, 100, 100));

        actionBtns.add(btnApprove);
        actionBtns.add(btnReject);

        gbc.gridx=1; gbc.gridy=4; 
        formPanel.add(actionBtns, gbc);

        // -- Right Side: Back Button --
        JPanel rightBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightBtnPanel.setBackground(Color.WHITE);
        JButton btnBack = new JButton("Back Home");
        btnBack.setBackground(new Color(200, 200, 200));
        rightBtnPanel.add(btnBack);

        bottomPanel.add(formPanel, BorderLayout.WEST);
        bottomPanel.add(rightBtnPanel, BorderLayout.SOUTH);
        
        add(bottomPanel, BorderLayout.SOUTH);

        loadListings();

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

        // Back
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminHome().setVisible(true);
        });
    }

    private void loadListings() {
        model.setRowCount(0);
        List<String[]> list = DBHelper.getAllListings();
        for(String[] row : list) {
            model.addRow(row);
        }
    }
}