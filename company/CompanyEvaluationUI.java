package company;
// imports
import common.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// screen to grade interns at the end
public class CompanyEvaluationUI extends JFrame {
    private String supervisorId;
    
    // ui components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtScore;
    private JTextArea txtFeedback;
    private JComboBox<String> cmbFinalStatus; // dropdown
    private JLabel lblSelectedStudent;

    // constructor
    public CompanyEvaluationUI(String id) {
        this.supervisorId = id;
        initComponents();
        loadStudents();
    }

    // build layout
    private void initComponents() {
        // setup frame
        setTitle("Final Evaluation & Completion");
        setSize(950, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header
        JLabel title = new JLabel("Intern Performance & Conclusion", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // split pane
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(480);

        // -- LEFT: Student List --
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Active Interns"));
        
        String[] cols = {"Intern ID", "Name", "Role"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        
        // click listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int r = studentTable.getSelectedRow();
            if(r != -1) {
                String name = tableModel.getValueAt(r, 1).toString();
                lblSelectedStudent.setText("Grading: " + name);
                lblSelectedStudent.setForeground(new Color(0, 102, 204));
            }
        });
        
        leftPanel.add(new JScrollPane(studentTable));
        splitPane.setLeftComponent(leftPanel);

        // -- RIGHT: Grading Form --
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        rightPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // label showing who is being graded
        lblSelectedStudent = new JLabel("Select an intern from the list...");
        lblSelectedStudent.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSelectedStudent.setForeground(Color.GRAY);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(lblSelectedStudent, gbc);
        
        // 1. score input
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        rightPanel.add(new JLabel("Performance Score (0-100):"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtScore = new JTextField();
        rightPanel.add(txtScore, gbc);

        // 2. status dropdown (completed or terminated)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        rightPanel.add(new JLabel("Final Conclusion:"), gbc);
        
        gbc.gridx = 1; 
        String[] statuses = {"Completed (Successfully Finished)", "Terminated (Failed/Fired)"};
        cmbFinalStatus = new JComboBox<>(statuses);
        rightPanel.add(cmbFinalStatus, gbc);
        
        // 3. feedback input
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.anchor = GridBagConstraints.NORTHWEST;
        rightPanel.add(new JLabel("Final Comments:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        txtFeedback = new JTextArea();
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);
        txtFeedback.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        rightPanel.add(new JScrollPane(txtFeedback), gbc);
        
        // buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton btnSubmit = new JButton("Conclude Internship");
        btnSubmit.setBackground(new Color(60, 179, 113)); 
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSubmit.addActionListener(e -> submit());
        
        btnPanel.add(btnSubmit);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // load active interns only
    private void loadStudents() {
        tableModel.setRowCount(0);
        java.util.List<String[]> list = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        
        for(String[] m : list) {
            String studentId = m[1];
            
            // check status
            String[] user = DatabaseHelper.getUserById(studentId);
            String status = (user != null && user.length > 9) ? user[9] : "Unknown";
            
            // ONLY show students who are "Placed" (Active). 
            // If they are already Completed/Terminated, they don't need another final evaluation.
            if (status.equalsIgnoreCase("Placed")) {
                tableModel.addRow(new Object[]{studentId, m[2], m[5]});
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            lblSelectedStudent.setText("No active interns to evaluate.");
        }
    }

    // save grade and update status
    private void submit() {
        int r = studentTable.getSelectedRow();
        if(r == -1) { 
            JOptionPane.showMessageDialog(this, "Please select an intern first.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }
        
        String scoreStr = txtScore.getText().trim();
        String feed = txtFeedback.getText().trim();
        String statusSelection = (String) cmbFinalStatus.getSelectedItem();
        
        // validation
        if(scoreStr.isEmpty() || feed.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Score and Comments.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int score = Integer.parseInt(scoreStr);
            if(score < 0 || score > 100) {
                JOptionPane.showMessageDialog(this, "Score must be between 0-100.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Score must be a number.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // determine final status (Completed or Terminated)
        String newStatusCode = "Completed"; // Default
        if (statusSelection.startsWith("Terminated")) {
            newStatusCode = "Terminated";
        }
        
        // get details
        String[] sv = DatabaseHelper.getUserById(supervisorId);
        String cname = (sv!=null && sv.length>8) ? sv[8] : "Unknown";
        String sid = tableModel.getValueAt(r, 0).toString();
        String sname = tableModel.getValueAt(r, 1).toString();
        
        // 1. Save Feedback
        DatabaseHelper.saveCompanyFeedback(sid, sname, cname, scoreStr, feed);
        
        // 2. Update Status (Ends the internship)
        DatabaseHelper.updateStudentPlacement(sid, newStatusCode);
        
        JOptionPane.showMessageDialog(this, "Internship Concluded.\nStudent marked as: " + newStatusCode);
        
        // Reset inputs
        txtScore.setText("");
        txtFeedback.setText("");
        cmbFinalStatus.setSelectedIndex(0);
        lblSelectedStudent.setText("Select an intern...");
        lblSelectedStudent.setForeground(Color.GRAY);
        
        // Reloads table, removing the finished student
        loadStudents(); 
    }
}