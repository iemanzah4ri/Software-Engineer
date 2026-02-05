import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAssignedStudentsUI extends JFrame {

    private String supervisorId;
    private JTable table;
    private DefaultTableModel model;

    public ViewAssignedStudentsUI(String supervisorId) {
        this.supervisorId = supervisorId;
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Assigned Students");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Students Assigned to Your Company", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Student ID", "Student Name", "Position", "Date Matched"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnBack = new JButton("Back");
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> dispose());
        footer.add(btnBack);

        add(footer, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<String[]> matches = DBHelper.getMatchesForSupervisor(supervisorId);
        
        if (matches.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No students are currently assigned to your company.");
        }

        for (String[] m : matches) {
            model.addRow(new Object[]{m[1], m[2], m[5], m[6]});
        }
    }
}