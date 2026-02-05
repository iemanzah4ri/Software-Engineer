import javax.swing.*;
import java.awt.*;

public class CompanyViewProgressUI extends JFrame {

    public CompanyViewProgressUI() {
        initComponents();
    }

    private void initComponents() {
        setTitle("View Internship Progress");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        JLabel title = new JLabel("Internship Progress Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title);

        add(createProgressPanel("Attendance", 30));
        add(createProgressPanel("Performance", 70));
        add(createProgressPanel("Overall Progress", 25));

        JPanel footer = new JPanel();
        JButton btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
            this.dispose();
        });
        footer.add(btnBack);
        add(footer);
    }

    private JPanel createProgressPanel(String label, int value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lbl = new JLabel(label + ": " + value + "%");
        lbl.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(value);
        bar.setStringPainted(true);
        bar.setPreferredSize(new Dimension(200, 25));

        p.add(lbl);
        p.add(bar);
        return p;
    }
}