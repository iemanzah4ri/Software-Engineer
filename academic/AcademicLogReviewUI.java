package academic;
// imports
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// ui to read student logbooks
public class AcademicLogReviewUI extends JFrame {

    // table stuff
    private JTable table;
    private DefaultTableModel model;

    // constructor
    public AcademicLogReviewUI() {
        // setup gui
        initComponents();
        // get the data
        loadLogbooks();
    }

    // initialize components
    private void initComponents() {
        // window properties
        setTitle("View Student Logbooks");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // title label
        JLabel title = new JLabel("Student Logbook Submissions", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        // spacing
        title.setBorder(BorderFactory.createEmptyBorder(15,0,15,0));
        add(title, BorderLayout.NORTH);

        // table columns setup
        String[] cols = {"Log ID", "Student ID", "Date", "Activity", "Hours", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        // add scrolling
        add(new JScrollPane(table), BorderLayout.CENTER);

        // footer panel for buttons
        JPanel footer = new JPanel();
        JButton btnRefresh = new JButton("Refresh");
        JButton btnBack = new JButton("Back");

        // listeners
        btnRefresh.addActionListener(e -> loadLogbooks());
        btnBack.addActionListener(e -> dispose());

        // add buttons
        footer.add(btnRefresh);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // function to populate table
    private void loadLogbooks() {
        // wipe current rows
        model.setRowCount(0);
        // get all logs from db
        List<String[]> logs = DatabaseHelper.getLogbooksByStudent(null); 
        // loop and add
        for (String[] log : logs) {
            model.addRow(log);
        }
    }
}