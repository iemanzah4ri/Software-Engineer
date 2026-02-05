import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class StudentLogbook extends JFrame {
    private final String studentId;
    private JTextField txtActivity, txtHours;
    private JTable tblLogs;
    private DefaultTableModel tableModel;

    public StudentLogbook(String studentId, String studentName) {
        this.studentId = studentId;
        initComponents();
        loadLogs();
    }

    private void initComponents() {
        setTitle("Daily Logbook");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Activity Description:"));
        txtActivity = new JTextField();
        inputPanel.add(txtActivity);
        
        inputPanel.add(new JLabel("Hours Spent:"));
        txtHours = new JTextField();
        inputPanel.add(txtHours);
        
        JButton btnSubmit = new JButton("Submit Entry");
        btnSubmit.addActionListener(e -> saveLog());
        inputPanel.add(btnSubmit);
        
        add(inputPanel, BorderLayout.NORTH);

        String[] cols = {"Date", "Activity", "Hours", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        tblLogs = new JTable(tableModel);
        add(new JScrollPane(tblLogs), BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void loadLogs() {
        tableModel.setRowCount(0);
        List<String[]> logs = DBHelper.getLogbooksByStudent(studentId);
        for (String[] l : logs) {
            tableModel.addRow(new Object[]{l[2], l[3], l[4], l[5]});
        }
    }

    private void saveLog() {
        String activity = txtActivity.getText();
        String hours = txtHours.getText();
        String date = LocalDate.now().toString();

        if (activity.isEmpty() || hours.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields");
            return;
        }

        DBHelper.saveLogbookEntry(studentId, date, activity, hours);
        JOptionPane.showMessageDialog(this, "Log Saved!");
        loadLogs();
        txtActivity.setText("");
        txtHours.setText("");
    }
}