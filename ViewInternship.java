import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewInternship extends JFrame {
    private final String studentId;
    private final String studentName;
    private JTable tblInternships;
    private DefaultTableModel tableModel;

    public ViewInternship(String studentId, String studentName) {
        this.studentId = studentId;
        this.studentName = studentName;
        initComponents();
        loadInternships();
    }

    private void initComponents() {
        setTitle("View Internships");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] cols = {"RegNo", "Company", "Location", "Job Description"};
        tableModel = new DefaultTableModel(cols, 0);
        tblInternships = new JTable(tableModel);
        add(new JScrollPane(tblInternships), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnApply = new JButton("Apply");
        JButton btnBack = new JButton("Back");
        
        btnApply.addActionListener(e -> applyForJob());
        btnBack.addActionListener(e -> dispose());
        
        panel.add(btnApply);
        panel.add(btnBack);
        add(panel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
    }

    private void loadInternships() {
        tableModel.setRowCount(0);
        List<String[]> listings = DBHelper.getAllListings();
        for (String[] l : listings) {
            if (l.length >= 5 && l[4].equalsIgnoreCase("Approved")) {
                tableModel.addRow(new Object[]{l[0], l[1], l[2], l[3]});
            }
        }
    }

    private void applyForJob() {
        int row = tblInternships.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an internship.");
            return;
        }

        String regNo = tableModel.getValueAt(row, 0).toString();
        String company = tableModel.getValueAt(row, 1).toString();

        if (DBHelper.hasApplied(studentId, regNo)) {
            JOptionPane.showMessageDialog(this, "You have already applied for this job.");
            return;
        }

        DBHelper.applyForInternship(studentId, regNo, company);
        JOptionPane.showMessageDialog(this, "Application Submitted!");
    }
}