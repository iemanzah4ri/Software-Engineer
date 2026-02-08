package admin;
// imports
import common.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// view stats about placements
public class AdminAnalyticsUI extends JFrame {

    // labels for counts
    private JLabel lblTotal, lblPlaced, lblUnplaced;
    // progress bar for visual
    private JProgressBar progressBar;
    // table for details
    private JTable table;
    private DefaultTableModel model;

    // constructor
    public AdminAnalyticsUI() {
        initComponents();
        // load data on start
        refreshData();
    }

    // build the ui
    private void initComponents() {
        // window setup
        setTitle("System Analytics & Reports");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header title
        JLabel title = new JLabel("Internship Placement Analytics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // panel for statistic cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        statsPanel.setPreferredSize(new Dimension(900, 100));

        // init labels to 0
        lblTotal = new JLabel("0", SwingConstants.CENTER);
        lblPlaced = new JLabel("0", SwingConstants.CENTER);
        lblUnplaced = new JLabel("0", SwingConstants.CENTER);
        
        // create cards with colors
        statsPanel.add(createStatCard("Total Students", lblTotal, new Color(230, 240, 255)));
        statsPanel.add(createStatCard("Students Placed", lblPlaced, new Color(220, 255, 220)));
        statsPanel.add(createStatCard("Not Placed", lblUnplaced, new Color(255, 230, 230)));
        
        // progress bar section
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Placement Rate"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 14));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        statsPanel.add(progressPanel);

        // add stats to north
        add(statsPanel, BorderLayout.NORTH);

        // wrapper for center content
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(statsPanel, BorderLayout.NORTH);
        
        // table setup
        String[] cols = {"Student ID", "Full Name", "Intake", "Current Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detailed Student List"));
        
        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        // footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> refreshData());
        
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminDashboardUI().setVisible(true); // go back to dashboard
        });

        footer.add(btnRefresh);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // helper to make those colored boxes
    private JPanel createStatCard(String title, JLabel valueLabel, Color bg) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(bg);
        p.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitle.setBorder(new EmptyBorder(10,0,0,0));
        
        valueLabel.setFont(new Font("Arial", Font.BOLD, 30));
        
        p.add(lblTitle, BorderLayout.NORTH);
        p.add(valueLabel, BorderLayout.CENTER);
        return p;
    }

    // calculation logic
    private void refreshData() {
        // clear table
        model.setRowCount(0);
        // get all students
        List<String[]> students = DatabaseHelper.getUsersByRole("Student", ""); 
        
        // counters
        int total = 0;
        int placed = 0;
        int notPlaced = 0;

        // loop through each student
        for (String[] s : students) {
            String id = s[0];
            // get full details
            String[] details = DatabaseHelper.getUserById(id); 
            
            if (details != null) {
                total++;
                String name = details[3];
                String intake = details[4];
                // status is at index 9
                String status = details[9];
                
                // count based on status
                if ("Placed".equalsIgnoreCase(status)) {
                    placed++;
                } else {
                    notPlaced++;
                }

                // add row to table
                model.addRow(new Object[]{id, name, intake, status});
            }
        }

        // update the labels
        lblTotal.setText(String.valueOf(total));
        lblPlaced.setText(String.valueOf(placed));
        lblUnplaced.setText(String.valueOf(notPlaced));

        // calculate percentage
        if (total > 0) {
            int percentage = (int) (((double) placed / total) * 100);
            progressBar.setValue(percentage);
        } else {
            progressBar.setValue(0);
        }
    }
}