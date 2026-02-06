package academic;
import common.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AcademicEvaluationUI extends JFrame {
    private String supervisorId;
    
    //vars
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    //my inputs
    private JTextField txtScore;
    private JTextArea txtFeedback;
    private JLabel lblSelectedStudent;
    
    //company stuff (read only)
    private JLabel lblCompScore, lblCompStatus;
    private JTextArea txtCompFeedback;

    public AcademicEvaluationUI(String id) {
        this.supervisorId = id;
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        //basic window setup
        setTitle("Academic Evaluation Portal");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //top header
        JLabel title = new JLabel("Submit Student Evaluation", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(400);

        //left panel stuff
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pending Evaluations")); 
        
        String[] cols = {"Student ID", "Name", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        
        //table click listener
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            int r = studentTable.getSelectedRow();
            if(r != -1) {
                String sid = tableModel.getValueAt(r, 0).toString();
                String name = tableModel.getValueAt(r, 1).toString();
                loadStudentDetails(sid, name);
            }
        });
        
        leftPanel.add(new JScrollPane(studentTable));
        splitPane.setLeftComponent(leftPanel);

        //right panel stuff
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        rightPanel.setBackground(new Color(245, 245, 245)); //grey background
        
        //student name label
        lblSelectedStudent = new JLabel("Select a student...");
        lblSelectedStudent.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSelectedStudent.setAlignmentX(Component.LEFT_ALIGNMENT); //align left
        rightPanel.add(lblSelectedStudent);
        rightPanel.add(Box.createVerticalStrut(15));

        //company section (cant edit this)
        JPanel pnlCompany = new JPanel(new GridBagLayout());
        pnlCompany.setBorder(BorderFactory.createTitledBorder("Company Supervisor Assessment"));
        pnlCompany.setBackground(Color.WHITE); 
        pnlCompany.setMaximumSize(new Dimension(2000, 180));
        pnlCompany.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx=0; gbc.gridy=0; 
        pnlCompany.add(new JLabel("Status Set By Company:"), gbc);
        
        gbc.gridx=1; 
        lblCompStatus = new JLabel("N/A");
        lblCompStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCompany.add(lblCompStatus, gbc);
        
        gbc.gridx=0; gbc.gridy=1; 
        pnlCompany.add(new JLabel("Company Score:"), gbc);
        
        gbc.gridx=1; 
        lblCompScore = new JLabel("Not Graded Yet");
        lblCompScore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCompany.add(lblCompScore, gbc);
        
        gbc.gridx=0; gbc.gridy=2; gbc.anchor = GridBagConstraints.NORTHWEST;
        pnlCompany.add(new JLabel("Company Comments:"), gbc);
        
        gbc.gridx=1; gbc.weightx=1.0; gbc.weighty=1.0; gbc.fill=GridBagConstraints.BOTH;
        txtCompFeedback = new JTextArea(3, 20);
        txtCompFeedback.setEditable(false); //read only
        txtCompFeedback.setLineWrap(true);
        txtCompFeedback.setWrapStyleWord(true);
        txtCompFeedback.setBackground(new Color(240, 240, 240));
        pnlCompany.add(new JScrollPane(txtCompFeedback), gbc);
        
        rightPanel.add(pnlCompany);
        rightPanel.add(Box.createVerticalStrut(20));

        //my section (editable)
        JPanel pnlAcademic = new JPanel(new GridBagLayout());
        pnlAcademic.setBorder(BorderFactory.createTitledBorder("Your Evaluation (Academic)"));
        pnlAcademic.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlAcademic.setBackground(Color.WHITE);
        pnlAcademic.setMaximumSize(new Dimension(2000, 200));
        
        //reset layout constraints
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        pnlAcademic.add(new JLabel("Your Score (0-100):"), gbc);
        
        gbc.gridx=1; gbc.weightx=1.0;
        txtScore = new JTextField();
        pnlAcademic.add(txtScore, gbc);
        
        gbc.gridx=0; gbc.gridy=1; gbc.anchor = GridBagConstraints.NORTHWEST;
        pnlAcademic.add(new JLabel("Your Feedback:"), gbc);
        
        gbc.gridx=1; gbc.weighty=1.0; gbc.fill=GridBagConstraints.BOTH;
        txtFeedback = new JTextArea(4, 20);
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);
        txtFeedback.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pnlAcademic.add(new JScrollPane(txtFeedback), gbc);
        
        rightPanel.add(pnlAcademic);
        rightPanel.add(Box.createVerticalStrut(10));
        
        //button container to fix layout
        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnContainer.setBackground(new Color(245, 245, 245)); //match bg
        btnContainer.setAlignmentX(Component.LEFT_ALIGNMENT); //panel left, button flows right
        btnContainer.setMaximumSize(new Dimension(2000, 50));
        
        JButton btnSubmit = new JButton("Submit Academic Grade");
        btnSubmit.setBackground(new Color(100, 200, 100));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.addActionListener(e -> submit());
        
        btnContainer.add(btnSubmit);
        rightPanel.add(btnContainer);

        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);
        
        //bottom footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        java.util.List<String[]> list = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        
        for(String[] m : list) {
            String studentId = m[1];
            
            //check if done already
            String[] fb = DatabaseHelper.getStudentFeedback(studentId);
            boolean alreadyGraded = false;
            
            //index 6 is my score
            if (fb != null && !fb[6].equals("N/A")) {
                alreadyGraded = true;
            }
            
            //skip if graded
            if (alreadyGraded) {
                continue; 
            }

            //get status
            String[] user = DatabaseHelper.getUserById(studentId);
            String status = (user != null && user.length > 9) ? user[9] : "Unknown";
            
            tableModel.addRow(new Object[]{studentId, m[2], status});
        }
        
        //if table empty
        if (tableModel.getRowCount() == 0) {
            lblSelectedStudent.setText("All students graded!");
        }
    }
    
    private void loadStudentDetails(String sid, String name) {
        lblSelectedStudent.setText("Grading: " + name);
        
        //1. get status
        String[] user = DatabaseHelper.getUserById(sid);
        String status = (user!=null && user.length>9) ? user[9] : "Unknown";
        lblCompStatus.setText(status.toUpperCase());
        
        //colors
        if(status.equalsIgnoreCase("Completed")) lblCompStatus.setForeground(new Color(34, 139, 34));
        else if(status.equalsIgnoreCase("Terminated")) lblCompStatus.setForeground(Color.RED);
        else lblCompStatus.setForeground(Color.BLUE);
        
        //2. get company feedback
        String[] fb = DatabaseHelper.getStudentFeedback(sid);
        if (fb != null) {
            String cScore = fb[5].equals("N/A") ? "Pending" : fb[5] + "/100";
            String cFeed = fb[7].equals("N/A") ? "No feedback provided yet." : fb[7];
            
            lblCompScore.setText(cScore);
            txtCompFeedback.setText(cFeed);
        } else {
            lblCompScore.setText("Pending");
            txtCompFeedback.setText("No evaluation data found.");
        }
    }

    private void submit() {
        int r = studentTable.getSelectedRow();
        if(r == -1) { 
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }
        
        String scoreStr = txtScore.getText().trim();
        String feed = txtFeedback.getText().trim();
        
        //check empty fields
        if(scoreStr.isEmpty() || feed.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Score and Feedback.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int score = Integer.parseInt(scoreStr);
            if(score < 0 || score > 100) {
                JOptionPane.showMessageDialog(this, "Score must be 0-100.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch(NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Score must be a number.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String sid = tableModel.getValueAt(r, 0).toString();
        String sname = tableModel.getValueAt(r, 1).toString();
        
        //save to db
        DatabaseHelper.saveAcademicFeedback(sid, sname, scoreStr, feed);
        
        JOptionPane.showMessageDialog(this, "Academic Evaluation Saved!");
        
        //reset stuff
        txtScore.setText("");
        txtFeedback.setText("");
        lblSelectedStudent.setText("Select a student...");
        txtCompFeedback.setText("");
        lblCompScore.setText("-");
        lblCompStatus.setText("-");
        studentTable.clearSelection();
        
        //refresh table
        loadStudents();
    }
}