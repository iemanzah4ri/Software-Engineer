import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        setSize(850, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("New Log Entry"));

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // Rows, Cols, Hgap, Vgap
        formPanel.setBorder(new EmptyBorder(10, 10, 5, 10)); // Inner padding

        formPanel.add(new JLabel("Activity Description:"));
        txtActivity = new JTextField();
        formPanel.add(txtActivity);
        
        formPanel.add(new JLabel("Hours Spent:"));
        txtHours = new JTextField();
        formPanel.add(txtHours);
        
        topPanel.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose()); // Closes window
        
        JButton btnSubmit = new JButton("Submit Entry");
        btnSubmit.addActionListener(e -> saveLog());
        
        
        btnPanel.add(btnSubmit);
        btnPanel.add(btnBack);
        
        topPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        String[] cols = {"Date", "Activity", "Hours", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        tblLogs = new JTable(tableModel);
        tblLogs.setRowHeight(25); // More breathing room for rows
        
        JScrollPane scrollPane = new JScrollPane(tblLogs);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Log History"));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
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