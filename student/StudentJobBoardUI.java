//displays available internship opportunities
//allows students to filter and apply for jobs
package student;
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class StudentJobBoardUI extends JFrame {

    private String studentId;
    private JTable table;
    private DefaultTableModel model;

    public StudentJobBoardUI(String id) {
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

        String[] cols = {"ListingID", "Reg No", "Company", "Location", "Job Title", "Job Description"};
        model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(25);
        
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0); 
        table.getColumnModel().getColumn(5).setPreferredWidth(300);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton btnApply = new JButton("Apply Now");
        btnApply.setBackground(new Color(135, 206, 250));
        btnApply.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnBack = new JButton("Back Home");
        
        btnApply.addActionListener(e -> apply());
        btnBack.addActionListener(e -> dispose());
        
        panel.add(btnApply);
        panel.add(btnBack);
        add(panel, BorderLayout.SOUTH);
    }

    private void loadListings() {
        model.setRowCount(0);
        List<String[]> listings = DatabaseHelper.getAllListings(); // UPDATED
        for (String[] l : listings) {
            if (l.length >= 7 && l[6].equalsIgnoreCase("Approved")) {
                String listingId = l[0];
                String reg = l[1];
                String comp = l[2];
                String loc = l[3];
                String title = l[4];
                Object desc = l[5];
                
                model.addRow(new Object[]{listingId, reg, comp, loc, title, desc.toString()});
            }
        }
    }

    private void apply() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a listing.");
            return;
        }

        String listingId = model.getValueAt(row, 0).toString();
        String regNo = model.getValueAt(row, 1).toString();
        String comp = model.getValueAt(row, 2).toString();
        String loc = model.getValueAt(row, 3).toString();
        String job = model.getValueAt(row, 4).toString();
        String desc = model.getValueAt(row, 5).toString();

        if (DatabaseHelper.hasApplied(studentId, listingId)) { // UPDATED
            JOptionPane.showMessageDialog(this, "You have already applied to this specific job!");
            return;
        }

        new StudentApplicationUI(studentId, listingId, comp, loc, job, desc).setVisible(true); // RENAMED
        dispose();
    }
}