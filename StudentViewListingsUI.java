import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentViewListingsUI extends JFrame {

    private String studentId;
    private JTable table;
    private DefaultTableModel model;

    public StudentViewListingsUI(String id) {
        this.studentId = id;
        initComponents();
        loadListings();
    }

    private void initComponents() {
        setTitle("Available Internship Listings");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Internship Opportunities", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Reg No", "Company", "Location", "Job Description", "Action"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnApply = new JButton("Apply for Selected");
        btnApply.setBackground(new Color(100, 200, 100));
        
        JButton btnBack = new JButton("Back");

        btnApply.addActionListener(e -> apply());
        btnBack.addActionListener(e -> dispose());

        footer.add(btnApply);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadListings() {
        model.setRowCount(0);
        List<String[]> list = DBHelper.getAllListings();
        for (String[] row : list) {
            if (row[4].equalsIgnoreCase("Approved")) {
                model.addRow(new Object[]{row[0], row[1], row[2], row[3], "Available"});
            }
        }
    }

    private void apply() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a listing.");
            return;
        }

        String regNo = model.getValueAt(row, 0).toString();
        String compName = model.getValueAt(row, 1).toString();

        if (DBHelper.hasApplied(studentId, regNo)) {
            JOptionPane.showMessageDialog(this, "You have already applied to this company!");
            return;
        }

        DBHelper.applyForInternship(studentId, regNo, compName);
        JOptionPane.showMessageDialog(this, "Application Sent Successfully!");
    }
}