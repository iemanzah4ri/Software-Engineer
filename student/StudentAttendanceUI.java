package student;
// imports
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// ui for clocking in and out
public class StudentAttendanceUI extends JFrame {
    // student info
    private final String studentId;
    private final String studentName;
    private DefaultTableModel tableModel;

    // constructor
    public StudentAttendanceUI(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        // set up visuals
        initComponents();
        // get past records
        loadAttendance();
    }

    // build the ui
    private void initComponents() {
        setTitle("Attendance");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // buttons for clocking
        JPanel btnPanel = new JPanel();
        JButton btnIn = new JButton("Clock In");
        JButton btnOut = new JButton("Clock Out");
        
        // add listeners
        btnIn.addActionListener(e -> recordAttendance("In"));
        btnOut.addActionListener(e -> recordAttendance("Out"));
        
        btnPanel.add(btnIn);
        btnPanel.add(btnOut);
        add(btnPanel, BorderLayout.NORTH);

        // table to show history
        String[] cols = {"Date", "Clock In", "Clock Out", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    // fetch from db
    private void loadAttendance() {
        tableModel.setRowCount(0);
        // get list
        List<String[]> atts = DatabaseHelper.getStudentAttendance(studentId); 
        // loop and add rows
        for(String[] a : atts) {
            tableModel.addRow(new Object[]{a[3], a[4], a[5], a[6]});
        }
    }

    // logic to save timestamp
    private void recordAttendance(String type) {
        // get current time
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        
        // check type
        if (type.equals("In")) {
            // save clock in
            DatabaseHelper.saveAttendance(studentId, studentName, date, time, null); 
            JOptionPane.showMessageDialog(this, "Clocked In at " + time);
        } else {
            // save clock out
            DatabaseHelper.saveAttendance(studentId, studentName, date, null, time); 
            JOptionPane.showMessageDialog(this, "Clocked Out at " + time);
        }
        // refresh table
        loadAttendance();
    }
}