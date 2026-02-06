import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VerifyLogbookAttd extends JFrame {

    private String supervisorId;
    
    private JTable tblLog;
    private DefaultTableModel modelLog;
    private JTextField txtLogDate, txtLogTime;
    private JTextArea txtLogActivity;
    private JButton btnEditLog;

    private JTable tblAtt;
    private DefaultTableModel modelAtt;
    private JSpinner spinAttDate, spinAttIn, spinAttOut;
    private JComboBox<String> cmbAttStatus;
    private JButton btnEditAtt;

    public VerifyLogbookAttd(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadLogbooks(); 
        loadAttendance();
    }

    private void initComponents() {
        setTitle("Verify Student Records");
        setSize(1000, 650); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Verify Logbooks & Attendance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel logPanel = createLogbookPanel();
        tabbedPane.addTab("Daily Logbooks", logPanel);

        JPanel attPanel = createAttendancePanel();
        tabbedPane.addTab("Attendance Records", attPanel);

        add(tabbedPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createLogbookPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600); 

        String[] cols = {"LogID", "StudentID", "Date", "Activity", "Hours", "Status"};
        modelLog = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLog = new JTable(modelLog);
        tblLog.setRowHeight(25);
        
        tblLog.getSelectionModel().addListSelectionListener(e -> {
            int row = tblLog.getSelectedRow();
            if (row != -1) {
                txtLogDate.setText(modelLog.getValueAt(row, 2).toString());
                txtLogActivity.setText(modelLog.getValueAt(row, 3).toString());
                txtLogTime.setText(modelLog.getValueAt(row, 4).toString());
            }
        });

        splitPane.setLeftComponent(new JScrollPane(tblLog));

        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        rightPanel.add(new JLabel("Date:"), gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0;
        txtLogDate = new JTextField(); 
        txtLogDate.setEditable(false);
        rightPanel.add(txtLogDate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        rightPanel.add(new JLabel("Hours/Time:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0;
        txtLogTime = new JTextField();
        txtLogTime.setEditable(false);
        rightPanel.add(txtLogTime, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; 
        gbc.anchor = GridBagConstraints.NORTHWEST;
        rightPanel.add(new JLabel("Activity:"), gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.BOTH;
        txtLogActivity = new JTextArea();
        txtLogActivity.setLineWrap(true);
        txtLogActivity.setWrapStyleWord(true);
        txtLogActivity.setEditable(false);
        rightPanel.add(new JScrollPane(txtLogActivity), gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        btnEditLog = new JButton("Edit");
        btnEditLog.addActionListener(e -> toggleEditLog());
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(144, 238, 144)); 
        btnVerify.addActionListener(e -> verifyLogEntry());
        
        btnPanel.add(btnEditLog);
        btnPanel.add(btnVerify);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(600);

        String[] cols = {"Att ID", "Student Name", "Date", "Clock In", "Clock Out", "Status"};
        modelAtt = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblAtt = new JTable(modelAtt);
        tblAtt.setRowHeight(25);
        
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
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        spinAttDate = new JSpinner(new SpinnerDateModel());
        spinAttDate.setEditor(new JSpinner.DateEditor(spinAttDate, "yyyy-MM-dd"));
        spinAttDate.setEnabled(false);

        spinAttIn = new JSpinner(new SpinnerDateModel());
        spinAttIn.setEditor(new JSpinner.DateEditor(spinAttIn, "HH:mm"));
        spinAttIn.setEnabled(false);

        spinAttOut = new JSpinner(new SpinnerDateModel());
        spinAttOut.setEditor(new JSpinner.DateEditor(spinAttOut, "HH:mm"));
        spinAttOut.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0; rightPanel.add(new JLabel("Date:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttDate, gbc);

        gbc.gridx = 0; gbc.gridy = 1; rightPanel.add(new JLabel("Clock In:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttIn, gbc);

        gbc.gridx = 0; gbc.gridy = 2; rightPanel.add(new JLabel("Clock Out:"), gbc);
        gbc.gridx = 1; rightPanel.add(spinAttOut, gbc);

        gbc.gridx = 0; gbc.gridy = 3; rightPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; 
        cmbAttStatus = new JComboBox<>(new String[]{"Pending", "Completed", "Verified", "Medical Certificate", "Public Holiday", "Absent"});
        cmbAttStatus.setEnabled(false);
        rightPanel.add(cmbAttStatus, gbc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEditAtt = new JButton("Edit");
        btnEditAtt.addActionListener(e -> toggleEditAtt());
        
        JButton btnVerify = new JButton("Verify");
        btnVerify.setBackground(new Color(144, 238, 144));
        btnVerify.addActionListener(e -> verifyAttendanceEntry());
        
        btnPanel.add(btnEditAtt);
        btnPanel.add(btnVerify);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.weighty = 1.0; gbc.anchor = GridBagConstraints.NORTH;
        rightPanel.add(btnPanel, gbc);

        splitPane.setRightComponent(rightPanel);
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void loadLogbooks() {
        modelLog.setRowCount(0);
        List<String[]> matches = DBHelper.getMatchesForSupervisor(supervisorId);
        for (String[] match : matches) {
            List<String[]> logs = DBHelper.getLogbooksByStudent(match[1]);
            for (String[] l : logs) modelLog.addRow(l);
        }
    }
    
    private void loadAttendance() {
        modelAtt.setRowCount(0);
        List<String[]> matches = DBHelper.getMatchesForSupervisor(supervisorId);
        for (String[] match : matches) {
            List<String[]> atts = DBHelper.getStudentAttendance(match[1]);
            for (String[] a : atts) {
                if(a.length >= 7) modelAtt.addRow(new Object[]{a[0], a[2], a[3], a[4], a[5], a[6]});
            }
        }
    }

    private void toggleEditLog() {
        int row = tblLog.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select a logbook entry."); return; }

        if (btnEditLog.getText().equals("Edit")) {
            setLogEditable(true);
            btnEditLog.setText("Save");
        } else {
            String id = modelLog.getValueAt(row, 0).toString();
            DBHelper.updateLogbookEntry(id, txtLogDate.getText(), txtLogActivity.getText(), txtLogTime.getText());
            JOptionPane.showMessageDialog(this, "Saved!");
            setLogEditable(false);
            btnEditLog.setText("Edit");
            loadLogbooks(); 
        }
    }
    
    private void toggleEditAtt() {
        int row = tblAtt.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Select an attendance entry."); return; }

        if (btnEditAtt.getText().equals("Edit")) {
            setAttEditable(true);
            btnEditAtt.setText("Save");
        } else {
            String id = modelAtt.getValueAt(row, 0).toString();
            String st = (cmbAttStatus.getSelectedItem() != null) ? cmbAttStatus.getSelectedItem().toString() : "Pending";
            
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            
            String dateStr = sdfDate.format(spinAttDate.getValue());
            String inStr = sdfTime.format(spinAttIn.getValue());
            String outStr = sdfTime.format(spinAttOut.getValue());
            
            DBHelper.updateAttendanceEntry(id, dateStr, inStr, outStr, st);
            JOptionPane.showMessageDialog(this, "Saved!");
            setAttEditable(false);
            btnEditAtt.setText("Edit");
            loadAttendance(); 
        }
    }

    private void setLogEditable(boolean b) {
        txtLogDate.setEditable(b);
        txtLogTime.setEditable(b);
        txtLogActivity.setEditable(b);
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
            DBHelper.updateLogbookStatus(modelLog.getValueAt(row, 0).toString(), "Verified");
            JOptionPane.showMessageDialog(this, "Logbook Verified!");
            loadLogbooks();
        }
    }
    
    private void verifyAttendanceEntry() {
        int row = tblAtt.getSelectedRow();
        if (row != -1) {
            DBHelper.updateAttendanceStatus(modelAtt.getValueAt(row, 0).toString(), "Verified");
            JOptionPane.showMessageDialog(this, "Attendance Verified!");
            loadAttendance();
        }
    }
}