import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentTrack extends JFrame {
    private final String studentId;
    private DefaultTableModel tableModel;

    public StudentTrack(String studentId, String studentName) {
        this.studentId = studentId;
        initComponents();
        loadApplications();
    }

    private void initComponents() {
        setTitle("Track Applications");
        setSize(700, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        String[] cols = {"App ID", "Company", "Internship ID", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        JTable tblApps = new JTable(tableModel);
        
        add(new JScrollPane(tblApps), BorderLayout.CENTER);
        
        // Bottom Panel for Buttons (Aligned Right)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadApplications());
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());
        
        bottomPanel.add(btnRefresh);
        bottomPanel.add(btnBack);
        
        add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void loadApplications() {
        tableModel.setRowCount(0);
        List<String[]> apps = DBHelper.getApplicationsByStudent(studentId);
        for (String[] a : apps) {
            tableModel.addRow(new Object[]{a[0], a[3], a[2], a[4]});
        }
    }
}