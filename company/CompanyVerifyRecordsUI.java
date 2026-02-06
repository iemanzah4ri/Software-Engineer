package company;
import common.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CompanyVerifyRecordsUI extends JFrame {

    private String supervisorId;
    
    //logbook stuff
    private JTable tblLog;
    private DefaultTableModel modelLog;
    private JTextField txtLogDate, txtLogTime;
    private JTextArea txtLogActivity;
    private JComboBox<String> cmbLogStatus; 
    private JButton btnEditLog;

    //attendance stuff
    private JTable tblAtt;
    private DefaultTableModel modelAtt;
    private JSpinner spinAttDate, spinAttIn, spinAttOut;
    private JComboBox<String> cmbAttStatus;
    private JButton btnEditAtt;

    public CompanyVerifyRecordsUI(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadLogbooks(); 
        loadAttendance();
    }

    private void initComponents() {
        //setup window
        setTitle("Verify Student Records");
        setSize(1100, 700); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //header
        JLabel titleLabel = new JLabel("Verify Logbooks & Attendance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        //tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel logPanel = createLogbookPanel();
        tabbedPane.addTab("Daily Logbooks", logPanel);

        JPanel attPanel = createAttendancePanel();
        tabbedPane.addTab("Attendance Records", attPanel);

        add(tabbedPane, BorderLayout.CENTER);
        
        //back button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(180, 40));
        btnBack.addActionListener(e -> dispose());
        
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createLogbookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600); 

        //table setup
        String[] cols = {"LogID", "Student", "Date", "Activity", "Hours", "Status"};
        modelLog = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLog = new JTable(modelLog);
        tblLog.setRowHeight(25);
        tblLog.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        //click listener
        tblLog.getSelectionModel().addListSelectionListener(e -> {
            int row = tblLog.getSelectedRow();
            if (row != -1) {
                txtLogDate.setText(modelLog.getValueAt(row, 2).toString());
                txtLogActivity.setText(modelLog.getValueAt(row, 3).toString());
                txtLogTime.setText(modelLog.getValueAt(row, 4).toString());
                
                //set dropdown to match
                String status = modelLog.getValueAt(row, 5).toString();
                cmbLogStatus.setSelectedItem(status);
            }
        });

        splitPane.setLeftComponent(new JScrollPane(tblLog));

        //right side form
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //detail header
        JLabel lblDetail = new JLabel("Log Entry Details");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(lblDetail, gbc);
        
        gbc.gridwidth = 1;

        //inputs
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        rightPanel.add(new JLabel("Date:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtLogDate = new JTextField(); 
        txtLogDate.setEditable(false);
        rightPanel.add(txtLogDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        rightPanel.add(new JLabel("Hours:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtLogTime = new JTextField();
        txtLogTime.setEditable(false);
        rightPanel.add(txtLogTime, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; 
        gbc.anchor = GridBagConstraints.NORTHWEST;
        rightPanel.add(new JLabel("Activity:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        txtLogActivity = new JTextArea();
        txtLogActivity.setLineWrap(true);
        txtLogActivity.setWrapStyleWord(true);
        txtLogActivity.setEditable(false);
        txtLogActivity.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        rightPanel.add(new JScrollPane(txtLogActivity), gbc);

        //status dropdown
        gbc.gridx = 0; gbc.gridy = 4; gbc.weighty = 0;
        rightPanel.add(new JLabel("Status:"), gbc);

        gbc.gridx = 1; 
        String[] statuses = {"Pending", "Verified", "Rejected"};
        cmbLogStatus = new JComboBox<>(statuses);
        cmbLogStatus.setEnabled(false); //locked by default
        rightPanel.add(cmbLogStatus, gbc);

        //buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        btnEditLog = new JButton("Edit");
        btnEditLog.setBackground(new Color(255, 250, 205)); 
        btnEditLog.addActionListener(e -> toggleEditLog());
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(144, 238, 144)); 
        btnVerify.addActionListener(e -> verifyLogEntry());
        
        btnPanel.add(btnEditLog);
        btnPanel.add(btnVerify);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600);

        String[] cols = {"Att ID", "Student", "Date", "In", "Out", "Status"};
        modelAtt = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblAtt = new JTable(modelAtt);
        tblAtt.setRowHeight(25);
        tblAtt.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        tblAtt.getSelectionModel().addListSelectionListener(e -> {
            int row = tblAtt.getSelectedRow();
            if (row != -1) {
                try {
                    String d = modelAtt.getValueAt(row, 2).toString();
                    String in = modelAtt.getValueAt(row, 3).toString();
                    String out = modelAtt.getValueAt(row, 4).toString();
                    
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    
                    if(!d.isEmpty()) spinAttDate.setValue(sdfDate.parse(d));
                    if(!in.isEmpty() && !in.equals("Pending")) spinAttIn.setValue(sdfTime.parse(in));
                    if(!out.isEmpty() && !out.equals("Pending")) spinAttOut.setValue(sdfTime.parse(out));
                    
                    cmbAttStatus.setSelectedItem(modelAtt.getValueAt(row, 5).toString());
                } catch(Exception ex) {}
            }
        });
        
        splitPane.setLeftComponent(new JScrollPane(tblAtt));

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //title
        JLabel lblTitle = new JLabel("Attendance Details");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        rightPanel.add(lblTitle, gbc);
        
        gbc.gridwidth = 1;

        //spinners
        spinAttDate = new JSpinner(new SpinnerDateModel());
        spinAttDate.setEditor(new JSpinner.DateEditor(spinAttDate, "yyyy-MM-dd"));
        spinAttDate.setEnabled(false);

        spinAttIn = new JSpinner(new SpinnerDateModel());
        spinAttIn.setEditor(new JSpinner.DateEditor(spinAttIn, "HH:mm"));
        spinAttIn.setEnabled(false);

        spinAttOut = new JSpinner(new SpinnerDateModel());
        spinAttOut.setEditor(new JSpinner.DateEditor(spinAttOut, "HH:mm"));
        spinAttOut.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttDate, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Clock In:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttIn, gbc);

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Clock Out:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttOut, gbc);

        gbc.gridx = 0; gbc.gridy = 4; rightPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; 
        cmbAttStatus = new JComboBox<>(new String[]{"Pending", "Verified", "Medical Certificate", "Public Holiday", "Absent"});
        cmbAttStatus.setEnabled(false);
        rightPanel.add(cmbAttStatus, gbc);

        //buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        btnEditAtt = new JButton("Edit");
        btnEditAtt.setBackground(new Color(255, 250, 205));
        btnEditAtt.addActionListener(e -> toggleEditAtt());
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(144, 238, 144));
        btnVerify.addActionListener(e -> verifyAttendanceEntry());
        
        btnPanel.add(btnEditAtt);
        btnPanel.add(btnVerify);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.weighty = 1.0; gbc.anchor = GridBagConstraints.NORTH;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadLogbooks() {
        modelLog.setRowCount(0);
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        List<String[]> allLogs = DatabaseHelper.getAllLogbooks();
        
        //match student to logs
        for (String[] m : matches) {
            String studentId = m[1];
            
            //filter: skip if completed/terminated
            String[] user = DatabaseHelper.getUserById(studentId);
            String status = (user != null && user.length > 9) ? user[9] : "Unknown";
            
            if (!status.equalsIgnoreCase("Placed")) {
                continue; //skip this student
            }

            String studentName = m[2];
            for (String[] log : allLogs) {
                if (log[1].equals(studentId)) {
                    modelLog.addRow(new Object[]{log[0], studentName, log[2], log[3], log[4], log[5]});
                }
            }
        }
    }
    
    private void loadAttendance() {
        modelAtt.setRowCount(0);
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        for (String[] match : matches) {
            String studentId = match[1];
            
            //filter: skip if completed/terminated
            String[] user = DatabaseHelper.getUserById(studentId);
            String status = (user != null && user.length > 9) ? user[9] : "Unknown";
            
            if (!status.equalsIgnoreCase("Placed")) {
                continue; //skip
            }

            List<String[]> atts = DatabaseHelper.getStudentAttendance(studentId);
            for (String[] a : atts) {
                if(a.length >= 7) modelAtt.addRow(new Object[]{a[0], a[2], a[3], a[4], a[5], a[6]});
            }
        }
    }

    private void toggleEditLog() {
        int row = tblLog.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a logbook entry first."); return; }

        if (btnEditLog.getText().equals("Edit")) {
            //unlock fields
            setLogEditable(true);
            btnEditLog.setText("Save");
        } else {
            //save data
            String id = modelLog.getValueAt(row, 0).toString();
            String newStatus = cmbLogStatus.getSelectedItem().toString(); 
            
            //update text fields
            DatabaseHelper.updateLogbookEntry(id, txtLogDate.getText(), txtLogActivity.getText(), txtLogTime.getText());
            //update status
            DatabaseHelper.updateLogbookStatus(id, newStatus);
            
            JOptionPane.showMessageDialog(this, "Changes Saved!");
            setLogEditable(false);
            btnEditLog.setText("Edit");
            loadLogbooks(); 
        }
    }
    
    private void toggleEditAtt() {
        int row = tblAtt.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an attendance entry first."); return; }

        if (btnEditAtt.getText().equals("Edit")) {
            setAttEditable(true);
            btnEditAtt.setText("Save");
        } else {
            //save data
            String id = modelAtt.getValueAt(row, 0).toString();
            String st = (cmbAttStatus.getSelectedItem() != null) ? cmbAttStatus.getSelectedItem().toString() : "Pending";
            
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            
            String dateStr = sdfDate.format(spinAttDate.getValue());
            String inStr = sdfTime.format(spinAttIn.getValue());
            String outStr = sdfTime.format(spinAttOut.getValue());
            
            DatabaseHelper.updateAttendanceEntry(id, dateStr, inStr, outStr, st);
            JOptionPane.showMessageDialog(this, "Changes Saved!");
            setAttEditable(false);
            btnEditAtt.setText("Edit");
            loadAttendance(); 
        }
    }

    private void setLogEditable(boolean b) {
        txtLogDate.setEditable(b);
        txtLogTime.setEditable(b);
        txtLogActivity.setEditable(b);
        cmbLogStatus.setEnabled(b); 
    }
    
    private void setAttEditable(boolean b) {
        spinAttDate.setEnabled(b);
        spinAttIn.setEnabled(b);
        spinAttOut.setEnabled(b);
        cmbAttStatus.setEnabled(b);
    }

    private void verifyLogEntry() {
        int row = tblLog.getSelectedRow();
        if (row != -1) {
            String id = modelLog.getValueAt(row, 0).toString();
            DatabaseHelper.updateLogbookStatus(id, "Verified");
            JOptionPane.showMessageDialog(this, "Logbook Verified!");
            loadLogbooks();
        } else {
            JOptionPane.showMessageDialog(this, "Select a logbook to verify.");
        }
    }
    
    private void verifyAttendanceEntry() {
        int row = tblAtt.getSelectedRow();
        if (row != -1) {
            String id = modelAtt.getValueAt(row, 0).toString();
            DatabaseHelper.updateAttendanceStatus(id, "Verified");
            JOptionPane.showMessageDialog(this, "Attendance Verified!");
            loadAttendance();
        } else {
            JOptionPane.showMessageDialog(this, "Select an attendance record to verify.");
        }
    }
}