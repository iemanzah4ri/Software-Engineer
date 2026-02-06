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
        setSize(900, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] cols = {"RegNo", "Company", "Location", "Job Title", "Job Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblInternships = new JTable(tableModel);
        add(new JScrollPane(tblInternships), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnApply = new JButton("Apply");
        JButton btnBack = new JButton("Back");
        
        btnApply.addActionListener(e -> openApplyWindow());
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
            if (l.length >= 6 && l[5].equalsIgnoreCase("Approved")) {
                tableModel.addRow(new Object[]{l[0], l[1], l[2], l[3], l[4]});
            }
        }
    }

    private void openApplyWindow() {
        int row = tblInternships.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an internship.");
            return;
        }

        String regNo = tableModel.getValueAt(row, 0).toString();
        String company = tableModel.getValueAt(row, 1).toString();
        String location = tableModel.getValueAt(row, 2).toString();
        String jobTitle = tableModel.getValueAt(row, 3).toString();
        String jobDesc = tableModel.getValueAt(row, 4).toString();

        if (DBHelper.hasApplied(studentId, regNo)) {
            JOptionPane.showMessageDialog(this, "You have already applied for this job.");
            return;
        }

        new StudentApplyUI(studentId, regNo, company, location, jobTitle, jobDesc).setVisible(true);
        dispose();
    }
}