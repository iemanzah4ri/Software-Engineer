package company;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;

// simple chart to show stats
public class CompanyProgressUI extends JFrame {

    // constructor
    public CompanyProgressUI() {
        initComponents();
    }

    // build ui
    private void initComponents() {
        setTitle("View Internship Progress");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("Internship Progress Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        // hardcoded placeholders for now
        add(createProgressPanel("Attendance", 30));
        add(createProgressPanel("Performance", 70));
        add(createProgressPanel("Overall Progress", 25));

        // back button
        JPanel footer = new JPanel();
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
            this.dispose();
        });
        footer.add(btnBack);
        add(footer);
    }

    // helper to make a bar
    private JPanel createProgressPanel(String label, int value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lbl = new JLabel(label + ": " + value + "%");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(300, 30));
        
        p.add(lbl);
        p.add(bar);
        return p;
    }
}