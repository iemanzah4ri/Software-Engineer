import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnalyticsUI extends JFrame {

    private JLabel lblTotal, lblPlaced, lblUnplaced;
    private JProgressBar progressBar;
    private JTable table;
    private DefaultTableModel model;

    public AnalyticsUI() {
        initComponents();
        refreshData();
    }

    private void initComponents() {
        setTitle("System Analytics & Reports");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Internship Placement Analytics", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        statsPanel.setPreferredSize(new Dimension(900, 100));

        lblTotal = new JLabel("0", SwingConstants.CENTER);
        lblPlaced = new JLabel("0", SwingConstants.CENTER);
        lblUnplaced = new JLabel("0", SwingConstants.CENTER);
        
        statsPanel.add(createStatCard("Total Students", lblTotal, new Color(230, 240, 255)));
        statsPanel.add(createStatCard("Students Placed", lblPlaced, new Color(220, 255, 220)));
        statsPanel.add(createStatCard("Not Placed", lblUnplaced, new Color(255, 230, 230)));
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createTitledBorder("Placement Rate"));
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 14));
        progressPanel.add(progressBar, BorderLayout.CENTER);
        statsPanel.add(progressPanel);

        add(statsPanel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.add(statsPanel, BorderLayout.NORTH);
        
        String[] cols = {"Student ID", "Full Name", "Intake", "Current Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detailed Student List"));
        
        centerWrapper.add(scrollPane, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.addActionListener(e -> refreshData());
        
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminHome().setVisible(true);
        });

        footer.add(btnRefresh);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

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

    private void refreshData() {
        model.setRowCount(0);
        List<String[]> students = DBHelper.getUsersByRole("Student", "");
        
        int total = 0;
        int placed = 0;
        int notPlaced = 0;

        for (String[] s : students) {
            String id = s[0];
            String[] details = DBHelper.getUserById(id);
            
            if (details != null) {
                total++;
                String name = details[3];
                String intake = details[4];
                String status = details[9];
                
                if ("Placed".equalsIgnoreCase(status)) {
                    placed++;
                } else {
                    notPlaced++;
                }

                model.addRow(new Object[]{id, name, intake, status});
            }
        }

        lblTotal.setText(String.valueOf(total));
        lblPlaced.setText(String.valueOf(placed));
        lblUnplaced.setText(String.valueOf(notPlaced));

        if (total > 0) {
            int percentage = (int) (((double) placed / total) * 100);
            progressBar.setValue(percentage);
        } else {
            progressBar.setValue(0);
        }
    }
}