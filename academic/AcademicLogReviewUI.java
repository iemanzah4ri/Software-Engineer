//interface to read intern logbooks
//allows monitoring of student activities
package academic;
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AcademicLogReviewUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public AcademicLogReviewUI() {
        initComponents();
        loadLogbooks();
    }

    private void initComponents() {
        setTitle("View Student Logbooks");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Student Logbook Submissions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"Log ID", "Student ID", "Date", "Activity", "Hours", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Back");

        btnRefresh.addActionListener(e -> loadLogbooks());
        btnBack.addActionListener(e -> dispose());

        footer.add(btnRefresh);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadLogbooks() {
        model.setRowCount(0);
        List<String[]> logs = DatabaseHelper.getLogbooksByStudent(null); // UPDATED
        for (String[] log : logs) {
            model.addRow(log);
        }
    }
}