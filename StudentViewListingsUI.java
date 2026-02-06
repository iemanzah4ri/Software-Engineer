import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
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
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Internship Opportunities", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Reg No", "Company", "Location", "Job Title", "Job Description"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        table.getColumnModel().getColumn(4).setPreferredWidth(300);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnApply = new JButton("Apply for Selected");
        btnApply.setBackground(new Color(60, 179, 113));
        btnApply.setForeground(Color.WHITE);
        
        JButton btnBack = new JButton("Back");
        
        JButton btnReset = new JButton("Reset DB (Debug)");
        btnReset.setBackground(Color.RED);
        btnReset.setForeground(Color.WHITE);

        btnApply.addActionListener(e -> apply());
        btnBack.addActionListener(e -> dispose());
        btnReset.addActionListener(e -> resetListings());

        footer.add(btnBack);
        footer.add(btnReset);
        footer.add(btnApply);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadListings() {
        model.setRowCount(0);
        List<String[]> list = DBHelper.getAllListings();
        
        for (String[] row : list) {
            if (row.length < 5) continue;

            String status = row[row.length - 1].trim();
            
            if (status.equalsIgnoreCase("Approved")) {
                String reg = row[0];
                String comp = row[1];
                String loc = row[2];
                String title = row[3];

                StringBuilder desc = new StringBuilder(row[4]);
                for (int i = 5; i < row.length - 1; i++) {
                    desc.append(", ").append(row[i]);
                }

                model.addRow(new Object[]{reg, comp, loc, title, desc.toString()});
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
        String comp = model.getValueAt(row, 1).toString();
        String loc = model.getValueAt(row, 2).toString();
        String job = model.getValueAt(row, 3).toString();
        String desc = model.getValueAt(row, 4).toString();

        if (DBHelper.hasApplied(studentId, regNo)) {
            JOptionPane.showMessageDialog(this, "You have already applied to this company!");
            return;
        }

        new StudentApplyUI(studentId, regNo, comp, loc, job, desc).setVisible(true);
        dispose();
    }

    private void resetListings() {
        int confirm = JOptionPane.showConfirmDialog(this, "Delete ALL listings? (Fixes corruption)", "Reset", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                new PrintWriter(new FileWriter("database/listings.txt")).close();
                loadListings();
                JOptionPane.showMessageDialog(this, "Database cleared. Please create a new listing.");
            } catch (Exception e) {}
        }
    }
}