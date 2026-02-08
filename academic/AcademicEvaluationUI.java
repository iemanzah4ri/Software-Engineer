package academic;
// standard imports
import common.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// this screen is for grading the students
public class AcademicEvaluationUI extends JFrame {
    // id of the prof
    private String supervisorId;
    
    // table components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    
    // input fields for my grading
    private JTextField txtScore;
    private JTextArea txtFeedback;
    private JLabel lblSelectedStudent;
    
    // components to show what the company said (read only)
    private JLabel lblCompScore, lblCompStatus;
    private JTextArea txtCompFeedback;

    // constructor
    public AcademicEvaluationUI(String id) {
        // save id
        this.supervisorId = id;
        // setup ui
        initComponents();
        // fill the list
        loadStudents();
    }

    // build the ui
    private void initComponents() {
        // window settings
        setTitle("Academic Evaluation Portal");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        // only close this window not the whole app
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // title at the top
        JLabel title = new JLabel("Submit Student Evaluation", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        // add some spacing
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // split pane to have list on left and details on right
        JSplitPane splitPane = new JSplitPane();
        // set divider position
        splitPane.setDividerLocation(400);

        // left panel setup
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pending Evaluations")); 
        
        // table columns
        String[] cols = {"Student ID", "Name", "Status"};
        // make table model and disable editing cells
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        // create table
        studentTable = new JTable(tableModel);
        // make rows taller
        studentTable.setRowHeight(25);
        
        // what happens when you click a row
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            // get selected row index
            int r = studentTable.getSelectedRow();
            // check if something is actually selected
            if(r != -1) {
                // get id and name from table
                String sid = tableModel.getValueAt(r, 0).toString();
                String name = tableModel.getValueAt(r, 1).toString();
                // call function to show details
                loadStudentDetails(sid, name);
            }
        });
        
        // add table to scroll pane
        leftPanel.add(new JScrollPane(studentTable));
        // put left panel in split pane
        splitPane.setLeftComponent(leftPanel);

        // right panel setup
        JPanel rightPanel = new JPanel();
        // vertical box layout
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        // padding
        rightPanel.setBorder(new EmptyBorder(10, 15, 10, 15));
        // grey bg
        rightPanel.setBackground(new Color(245, 245, 245)); 
        
        // label for which student is selected
        lblSelectedStudent = new JLabel("Select a student...");
        lblSelectedStudent.setFont(new Font("Segoe UI", Font.BOLD, 18));
        // align left
        lblSelectedStudent.setAlignmentX(Component.LEFT_ALIGNMENT); 
        rightPanel.add(lblSelectedStudent);
        // spacer
        rightPanel.add(Box.createVerticalStrut(15));

        // section for company feedback
        JPanel pnlCompany = new JPanel(new GridBagLayout());
        pnlCompany.setBorder(BorderFactory.createTitledBorder("Company Supervisor Assessment"));
        pnlCompany.setBackground(Color.WHITE); 
        // restrict height
        pnlCompany.setMaximumSize(new Dimension(2000, 180));
        pnlCompany.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        // grid bag constraints for layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // row 0: label
        gbc.gridx=0; gbc.gridy=0; 
        pnlCompany.add(new JLabel("Status Set By Company:"), gbc);
        
        // row 0: value
        gbc.gridx=1; 
        lblCompStatus = new JLabel("N/A");
        lblCompStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCompany.add(lblCompStatus, gbc);
        
        // row 1: label
        gbc.gridx=0; gbc.gridy=1; 
        pnlCompany.add(new JLabel("Company Score:"), gbc);
        
        // row 1: value
        gbc.gridx=1; 
        lblCompScore = new JLabel("Not Graded Yet");
        lblCompScore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        pnlCompany.add(lblCompScore, gbc);
        
        // row 2: label (top aligned)
        gbc.gridx=0; gbc.gridy=2; gbc.anchor = GridBagConstraints.NORTHWEST;
        pnlCompany.add(new JLabel("Company Comments:"), gbc);
        
        // row 2: text area
        gbc.gridx=1; gbc.weightx=1.0; gbc.weighty=1.0; gbc.fill=GridBagConstraints.BOTH;
        txtCompFeedback = new JTextArea(3, 20);
        // cannot edit this
        txtCompFeedback.setEditable(false); 
        txtCompFeedback.setLineWrap(true);
        txtCompFeedback.setWrapStyleWord(true);
        txtCompFeedback.setBackground(new Color(240, 240, 240));
        pnlCompany.add(new JScrollPane(txtCompFeedback), gbc);
        
        // add company panel to right side
        rightPanel.add(pnlCompany);
        rightPanel.add(Box.createVerticalStrut(20));

        // my grading section
        JPanel pnlAcademic = new JPanel(new GridBagLayout());
        pnlAcademic.setBorder(BorderFactory.createTitledBorder("Your Evaluation (Academic)"));
        pnlAcademic.setAlignmentX(Component.LEFT_ALIGNMENT);
        pnlAcademic.setBackground(Color.WHITE);
        pnlAcademic.setMaximumSize(new Dimension(2000, 200));
        
        // reset constraints
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // score input row
        gbc.gridx=0; gbc.gridy=0; gbc.weightx=0;
        pnlAcademic.add(new JLabel("Your Score (0-100):"), gbc);
        
        // text field for score
        gbc.gridx=1; gbc.weightx=1.0;
        txtScore = new JTextField();
        pnlAcademic.add(txtScore, gbc);
        
        // feedback row
        gbc.gridx=0; gbc.gridy=1; gbc.anchor = GridBagConstraints.NORTHWEST;
        pnlAcademic.add(new JLabel("Your Feedback:"), gbc);
        
        // text area for feedback
        gbc.gridx=1; gbc.weighty=1.0; gbc.fill=GridBagConstraints.BOTH;
        txtFeedback = new JTextArea(4, 20);
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);
        txtFeedback.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        pnlAcademic.add(new JScrollPane(txtFeedback), gbc);
        
        // add academic panel
        rightPanel.add(pnlAcademic);
        rightPanel.add(Box.createVerticalStrut(10));
        
        // container for submit button
        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnContainer.setBackground(new Color(245, 245, 245)); 
        btnContainer.setAlignmentX(Component.LEFT_ALIGNMENT); 
        btnContainer.setMaximumSize(new Dimension(2000, 50));
        
        // submit button styling
        JButton btnSubmit = new JButton("Submit Academic Grade");
        btnSubmit.setBackground(new Color(100, 200, 100));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // click event
        btnSubmit.addActionListener(e -> submit());
        
        // add button to container
        btnContainer.add(btnSubmit);
        rightPanel.add(btnContainer);

        // add right panel to split pane
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);
        
        // bottom footer with back button
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // load list of students assigned to me
    private void loadStudents() {
        // clear table
        tableModel.setRowCount(0);
        // fetch from db
        java.util.List<String[]> list = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        
        // loop through matches
        for(String[] m : list) {
            String studentId = m[1];
            
            // check if i already graded them
            String[] fb = DatabaseHelper.getStudentFeedback(studentId);
            boolean alreadyGraded = false;
            
            // check if index 6 (my score) is filled
            if (fb != null && !fb[6].equals("N/A")) {
                alreadyGraded = true;
            }
            
            // if done, dont show in list
            if (alreadyGraded) {
                continue; 
            }

            // get student status info
            String[] user = DatabaseHelper.getUserById(studentId);
            String status = (user != null && user.length > 9) ? user[9] : "Unknown";
            
            // add to table
            tableModel.addRow(new Object[]{studentId, m[2], status});
        }
        
        // show message if no students left
        if (tableModel.getRowCount() == 0) {
            lblSelectedStudent.setText("All students graded!");
        }
    }
    
    // show details on the right when row clicked
    private void loadStudentDetails(String sid, String name) {
        // update label
        lblSelectedStudent.setText("Grading: " + name);
        
        // get status from user table
        String[] user = DatabaseHelper.getUserById(sid);
        String status = (user!=null && user.length>9) ? user[9] : "Unknown";
        lblCompStatus.setText(status.toUpperCase());
        
        // color code the status
        if(status.equalsIgnoreCase("Completed")) lblCompStatus.setForeground(new Color(34, 139, 34));
        else if(status.equalsIgnoreCase("Terminated")) lblCompStatus.setForeground(Color.RED);
        else lblCompStatus.setForeground(Color.BLUE);
        
        // load feedback from db
        String[] fb = DatabaseHelper.getStudentFeedback(sid);
        if (fb != null) {
            // check if company graded yet
            String cScore = fb[5].equals("N/A") ? "Pending" : fb[5] + "/100";
            String cFeed = fb[7].equals("N/A") ? "No feedback provided yet." : fb[7];
            
            // update UI
            lblCompScore.setText(cScore);
            txtCompFeedback.setText(cFeed);
        } else {
            // handle missing data
            lblCompScore.setText("Pending");
            txtCompFeedback.setText("No evaluation data found.");
        }
    }

    // handle submit button click
    private void submit() {
        // get selected row
        int r = studentTable.getSelectedRow();
        if(r == -1) { 
            // error if nothing selected
            JOptionPane.showMessageDialog(this, "Please select a student.", "Error", JOptionPane.ERROR_MESSAGE); 
            return; 
        }
        
        // get inputs
        String scoreStr = txtScore.getText().trim();
        String feed = txtFeedback.getText().trim();
        
        // validate not empty
        if(scoreStr.isEmpty() || feed.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in Score and Feedback.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // parse int
            int score = Integer.parseInt(scoreStr);
            // check range
            if(score < 0 || score > 100) {
                JOptionPane.showMessageDialog(this, "Score must be 0-100.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch(NumberFormatException e) {
            // error if not a number
            JOptionPane.showMessageDialog(this, "Score must be a number.", "Invalid Score", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // get ids
        String sid = tableModel.getValueAt(r, 0).toString();
        String sname = tableModel.getValueAt(r, 1).toString();
        
        // save using helper
        DatabaseHelper.saveAcademicFeedback(sid, sname, scoreStr, feed);
        
        // success message
        JOptionPane.showMessageDialog(this, "Academic Evaluation Saved!");
        
        // clear inputs
        txtScore.setText("");
        txtFeedback.setText("");
        lblSelectedStudent.setText("Select a student...");
        txtCompFeedback.setText("");
        lblCompScore.setText("-");
        lblCompStatus.setText("-");
        // unselect row
        studentTable.clearSelection();
        
        // reload the list
        loadStudents();
    }
}