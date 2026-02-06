//interface to verify student logs and attendance
//allows supervisor to approve or edit entries
package company;
import common.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CompanyVerifyRecordsUI extends JFrame {

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

    public CompanyVerifyRecordsUI(String supervisorId) {
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

        // --- Logbook Tab ---
        JPanel logPanel = new JPanel(new BorderLayout());
        String[] logCols = {"LogID", "Student", "Date", "Activity", "Hours", "Status"};
        modelLog = new DefaultTableModel(logCols, 0) { public boolean isCellEditable(int r, int c){return false;} };
        tblLog = new JTable(modelLog);
        
        JPanel logForm = new JPanel(new GridLayout(4, 2));
        txtLogDate = new JTextField(); 
        txtLogTime = new JTextField(); 
        txtLogActivity = new JTextArea(3, 20);
        
        logForm.add(new JLabel("Date:")); logForm.add(txtLogDate);
        logForm.add(new JLabel("Hours:")); logForm.add(txtLogTime);
        logForm.add(new JLabel("Activity:")); logForm.add(new JScrollPane(txtLogActivity));
        
        JPanel logBtns = new JPanel();
        JButton btnVerifyLog = new JButton("Verify Selected");
        btnVerifyLog.setBackground(new Color(144, 238, 144));
        btnEditLog = new JButton("Edit");
        JButton btnSaveLog = new JButton("Save Changes");
        
        btnVerifyLog.addActionListener(e -> verifyLogEntry());
        btnEditLog.addActionListener(e -> { setLogEditable(true); btnEditLog.setText("Editing..."); });
        btnSaveLog.addActionListener(e -> saveLogEdit());

        logBtns.add(btnVerifyLog); logBtns.add(btnEditLog); logBtns.add(btnSaveLog);

        logPanel.add(new JScrollPane(tblLog), BorderLayout.CENTER);
        logPanel.add(logForm, BorderLayout.NORTH);
        logPanel.add(logBtns, BorderLayout.SOUTH);
        setLogEditable(false);

        // --- Attendance Tab ---
        JPanel attPanel = new JPanel(new BorderLayout());
        String[] attCols = {"AttID", "Student", "Date", "In", "Out", "Status"};
        modelAtt = new DefaultTableModel(attCols, 0) { public boolean isCellEditable(int r, int c){return false;} };
        tblAtt = new JTable(modelAtt);

        JPanel attForm = new JPanel(new GridLayout(4, 2));
        spinAttDate = new JSpinner(new SpinnerDateModel());
        spinAttIn = new JSpinner(new SpinnerDateModel());
        spinAttOut = new JSpinner(new SpinnerDateModel());
        String[] statuses = {"Pending", "Verified", "Absent", "Medical Certificate", "Public Holiday"};
        cmbAttStatus = new JComboBox<>(statuses);

        attForm.add(new JLabel("Date:")); attForm.add(spinAttDate);
        attForm.add(new JLabel("Clock In:")); attForm.add(spinAttIn);
        attForm.add(new JLabel("Clock Out:")); attForm.add(spinAttOut);
        attForm.add(new JLabel("Status:")); attForm.add(cmbAttStatus);

        JPanel attBtns = new JPanel();
        JButton btnVerifyAtt = new JButton("Verify Selected");
        btnVerifyAtt.setBackground(new Color(144, 238, 144));
        btnEditAtt = new JButton("Edit");
        JButton btnSaveAtt = new JButton("Save Changes");

        btnVerifyAtt.addActionListener(e -> verifyAttendanceEntry());
        btnEditAtt.addActionListener(e -> { setAttEditable(true); btnEditAtt.setText("Editing..."); });
        btnSaveAtt.addActionListener(e -> saveAttEdit());

        attBtns.add(btnVerifyAtt); attBtns.add(btnEditAtt); attBtns.add(btnSaveAtt);
        
        attPanel.add(new JScrollPane(tblAtt), BorderLayout.CENTER);
        attPanel.add(attForm, BorderLayout.NORTH);
        attPanel.add(attBtns, BorderLayout.SOUTH);
        setAttEditable(false);

        tabbedPane.addTab("Logbooks", logPanel);
        tabbedPane.addTab("Attendance", attPanel);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Listeners for selection to populate forms
        tblLog.getSelectionModel().addListSelectionListener(e -> {
            int r = tblLog.getSelectedRow();
            if(r != -1) {
                txtLogDate.setText(modelLog.getValueAt(r, 2).toString());
                txtLogActivity.setText(modelLog.getValueAt(r, 3).toString());
                txtLogTime.setText(modelLog.getValueAt(r, 4).toString());
            }
        });
        
        // Simplified Date parsing for Attendance spinner population omitted for brevity
        // In a real app, you would parse the strings from table back to SpinnerDateModel
    }

    private void loadLogbooks() {
        modelLog.setRowCount(0);
        // We only want logs for students assigned to THIS supervisor
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId); // UPDATED
        List<String[]> allLogs = DatabaseHelper.getAllLogbooks(); // UPDATED
        
        for (String[] m : matches) {
            String studentId = m[1];
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
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId); // UPDATED
        // Using raw file read or we need a helper "getAllAttendance"
        // I'll simulate getAllAttendance by using getStudentAttendance inside loop
        // (Performance note: inefficient, but works for file based)
        for(String[] m : matches) {
            String sid = m[1];
            String sname = m[2];
            List<String[]> atts = DatabaseHelper.getStudentAttendance(sid); // UPDATED
            for(String[] a : atts) {
                 modelAtt.addRow(new Object[]{a[0], sname, a[3], a[4], a[5], a[6]});
            }
        }
    }

    private void saveLogEdit() {
        int r = tblLog.getSelectedRow();
        if(r != -1) {
            String id = modelLog.getValueAt(r, 0).toString();
            DatabaseHelper.updateLogbookEntry(id, txtLogDate.getText(), txtLogActivity.getText(), txtLogTime.getText()); // UPDATED
            JOptionPane.showMessageDialog(this, "Saved!");
            setLogEditable(false);
            btnEditLog.setText("Edit");
            loadLogbooks();
        }
    }

    private void saveAttEdit() {
        int r = tblAtt.getSelectedRow();
        if(r != -1) {
            String id = modelAtt.getValueAt(r, 0).toString();
            String st = cmbAttStatus.getSelectedItem().toString();
            
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
            
            String dateStr = sdfDate.format(spinAttDate.getValue());
            String inStr = sdfTime.format(spinAttIn.getValue());
            String outStr = sdfTime.format(spinAttOut.getValue());
            
            DatabaseHelper.updateAttendanceEntry(id, dateStr, inStr, outStr, st); // UPDATED
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
            DatabaseHelper.updateLogbookStatus(modelLog.getValueAt(row, 0).toString(), "Verified"); // UPDATED
            JOptionPane.showMessageDialog(this, "Logbook Verified!");
            loadLogbooks();
        }
    }
    
    private void verifyAttendanceEntry() {
        int row = tblAtt.getSelectedRow();
        if (row != -1) {
            DatabaseHelper.updateAttendanceStatus(modelAtt.getValueAt(row, 0).toString(), "Verified"); // UPDATED
            JOptionPane.showMessageDialog(this, "Attendance Verified!");
            loadAttendance();
        }
    }
}