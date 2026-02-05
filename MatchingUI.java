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
        setSize(1300, 650); // Increased size slightly to fit new columns
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Match Students, Supervisors, and Internships", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Left Panel: Students ---
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Available Students"));
        studentModel = new DefaultTableModel(new String[]{"ID", "Name", "Intake"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        studentTable = new JTable(studentModel);
        leftPanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // --- Middle Panel: Supervisors ---
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.setBorder(BorderFactory.createTitledBorder("Academic Supervisors"));
        supervisorModel = new DefaultTableModel(new String[]{"ID", "Name", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        supervisorTable = new JTable(supervisorModel);
        middlePanel.add(new JScrollPane(supervisorTable), BorderLayout.CENTER);

        // --- Right Panel: Listings (UPDATED) ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Active Internship Listings"));
        
        // Updated Columns: Added Job Title & Job Desc, removed generic "Job"
        String[] listingCols = {"RegNo", "Company", "Job Title", "Job Desc"};
        listingModel = new DefaultTableModel(listingCols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        listingTable = new JTable(listingModel);
        rightPanel.add(new JScrollPane(listingTable), BorderLayout.CENTER);

        centerPanel.add(leftPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(rightPanel);
        add(centerPanel, BorderLayout.CENTER);

        // --- Bottom Panel ---
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

        // Load Students
        List<String[]> students = DBHelper.getAvailableStudents();
        for (String[] s : students) {
            studentModel.addRow(s);
        }

        // Load Supervisors
        List<String[]> supervisors = DBHelper.getUsersByRole("Academic Supervisor", "");
        for (String[] sup : supervisors) {
            supervisorModel.addRow(new Object[]{sup[0], sup[2], "Academic"});
        }

        // Load Listings (Updated for new columns)
        List<String[]> listings = DBHelper.getAllListings();
        for (String[] l : listings) {
            // Check status is Approved and ensure we have enough data columns
            // l indices: 0=RegNo, 1=Company, 2=Location, 3=JobName, 4=JobDesc, 5=Status
            if (l.length >= 6 && l[5].equalsIgnoreCase("Approved")) {
                listingModel.addRow(new Object[]{l[0], l[1], l[3], l[4]});
            }
        }
    }

    private void performMatch() {
        int studentRow = studentTable.getSelectedRow();
        int supervisorRow = supervisorTable.getSelectedRow();
        int listingRow = listingTable.getSelectedRow();

        if (studentRow == -1 || supervisorRow == -1 || listingRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a Student, a Supervisor, and a Listing.");
            return;
        }

        // Get Data from Tables
        String studentId = studentModel.getValueAt(studentRow, 0).toString();
        String studentName = studentModel.getValueAt(studentRow, 1).toString();
        String supervisorName = supervisorModel.getValueAt(supervisorRow, 1).toString();
        
        String regNo = listingModel.getValueAt(listingRow, 0).toString();
        String companyName = listingModel.getValueAt(listingRow, 1).toString();
        String position = listingModel.getValueAt(listingRow, 2).toString(); // Job Title is now at index 2

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Match " + studentName + " with " + companyName + " (" + position + ")?\nSupervisor: " + supervisorName, 
            "Confirm Match", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            DBHelper.saveMatch(studentId, studentName, regNo, companyName, position);
            JOptionPane.showMessageDialog(this, "Match Successful! Student marked as 'Placed'.");
            loadData(); // Refresh tables to remove placed student
        }
    }
}