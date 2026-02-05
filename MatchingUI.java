import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MatchingUI extends JFrame {

    private JTable studentTable, supervisorTable, listingTable;
    private DefaultTableModel studentModel, supervisorModel, listingModel;

    public MatchingUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Internship Matching System");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Match Students, Supervisors, and Internships", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Students"));
        studentModel = new DefaultTableModel(new String[]{"ID", "Name", "Intake"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        studentTable = new JTable(studentModel);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Academic Supervisors"));
        supervisorModel = new DefaultTableModel(new String[]{"ID", "Name", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        supervisorTable = new JTable(supervisorModel);
        middlePanel.add(new JScrollPane(supervisorTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Active Listings"));
        listingModel = new DefaultTableModel(new String[]{"RegNo", "Company", "Job"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        listingTable = new JTable(listingModel);
        rightPanel.add(new JScrollPane(listingTable), BorderLayout.CENTER);

        centerPanel.add(leftPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnBack = new JButton("Back");
        btnBack.setFont(new Font("Arial", Font.BOLD, 16));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> {
            new AdminHome().setVisible(true);
            dispose();
        });
        bottomPanel.add(btnBack);

        JButton btnMatch = new JButton("Confirm Match");
        btnMatch.setFont(new Font("Arial", Font.BOLD, 16));
        btnMatch.setPreferredSize(new Dimension(200, 40));
        btnMatch.setBackground(new Color(144, 238, 144));
        btnMatch.addActionListener(e -> performMatch());
        bottomPanel.add(btnMatch);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        studentModel.setRowCount(0);
        supervisorModel.setRowCount(0);
        listingModel.setRowCount(0);

        List<String[]> students = DBHelper.getAvailableStudents();
        for (String[] s : students) {
            studentModel.addRow(s);
        }

        List<String[]> supervisors = DBHelper.getUsersByRole("Academic Supervisor", "");
        for (String[] sup : supervisors) {
            supervisorModel.addRow(new Object[]{sup[0], sup[2], "Academic"});
        }

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
            JOptionPane.showMessageDialog(this, "Please select at least a Student and a Listing.");
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