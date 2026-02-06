//interface for clocking in and out
//records daily attendance timestamps
package student;
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentAttendanceUI extends JFrame {
    private final String studentId;
    private final String studentName;
    private DefaultTableModel tableModel;

    public StudentAttendanceUI(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        initComponents();
        loadAttendance();
    }

    private void initComponents() {
        setTitle("Attendance");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel btnPanel = new JPanel();
        JButton btnIn = new JButton("Clock In");
        JButton btnOut = new JButton("Clock Out");
        
        btnIn.addActionListener(e -> recordAttendance("In"));
        btnOut.addActionListener(e -> recordAttendance("Out"));
        
        btnPanel.add(btnIn);
        btnPanel.add(btnOut);
        add(btnPanel, BorderLayout.NORTH);

        String[] cols = {"Date", "Clock In", "Clock Out", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        add(new JScrollPane(new JTable(tableModel)), BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    private void loadAttendance() {
        tableModel.setRowCount(0);
        List<String[]> atts = DatabaseHelper.getStudentAttendance(studentId); // UPDATED
        for(String[] a : atts) {
            tableModel.addRow(new Object[]{a[3], a[4], a[5], a[6]});
        }
    }

    private void recordAttendance(String type) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        
        if (type.equals("In")) {
            DatabaseHelper.saveAttendance(studentId, studentName, date, time, null); // UPDATED
            JOptionPane.showMessageDialog(this, "Clocked In at " + time);
        } else {
            DatabaseHelper.saveAttendance(studentId, studentName, date, null, time); // UPDATED
            JOptionPane.showMessageDialog(this, "Clocked Out at " + time);
        }
        loadAttendance();
    }
}