import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MatchingUI extends JFrame {

    private JTable studentTable, listingTable;
    private DefaultTableModel studentModel, listingModel;

    public MatchingUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Internship Matching System");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Match Students to Internships", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);

        // --- LEFT: AVAILABLE STUDENTS ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Students (Not Placed)"));
        
        String[] studentCols = {"ID", "Name", "Intake"};
        studentModel = new DefaultTableModel(studentCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        studentTable = new JTable(studentModel);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // --- RIGHT: AVAILABLE LISTINGS ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Active Internship Listings"));

        String[] listingCols = {"RegNo", "Company", "Job"};
        listingModel = new DefaultTableModel(listingCols, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        listingTable = new JTable(listingModel);
        rightPanel.add(new JScrollPane(listingTable), BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        // --- BOTTOM: ACTION BUTTON ---
        JButton btnMatch = new JButton("Confirm Match");
        btnMatch.setFont(new Font("Arial", Font.BOLD, 16));
        btnMatch.setBackground(new Color(144, 238, 144));
        btnMatch.addActionListener(e -> performMatch());
        add(btnMatch, BorderLayout.SOUTH);
    }

    private void loadData() {
        studentModel.setRowCount(0);
        listingModel.setRowCount(0);

        // 1. Get Available Students using DBHelper
        List<String[]> students = DBHelper.getAvailableStudents();
        for (String[] s : students) {
            studentModel.addRow(s);
        }

        // 2. Get Listings
        List<String[]> listings = DBHelper.getAllListings();
        for (String[] l : listings) {
            if (l.length >= 5 && l[4].equalsIgnoreCase("Approved")) {
                listingModel.addRow(new Object[]{l[0], l[1], l[3]});
            }
        }
    }

    private void performMatch() {
        int studentRow = studentTable.getSelectedRow();
        int listingRow = listingTable.getSelectedRow();

        if (studentRow == -1 || listingRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select ONE Student and ONE Listing.");
            return;
        }

        String studentId = studentModel.getValueAt(studentRow, 0).toString();
        String studentName = studentModel.getValueAt(studentRow, 1).toString();
        
        String regNo = listingModel.getValueAt(listingRow, 0).toString();
        String companyName = listingModel.getValueAt(listingRow, 1).toString();
        String position = listingModel.getValueAt(listingRow, 2).toString();

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Match " + studentName + " with " + companyName + "?", 
            "Confirm Match", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DBHelper.saveMatch(studentId, studentName, regNo, companyName, position);
            JOptionPane.showMessageDialog(this, "Match Successful! Student marked as 'Placed'.");
            loadData();
        }
    }
}