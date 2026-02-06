import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentProgress extends JFrame {
    
    public StudentProgress(String studentId, String studentName) {
        initComponents(studentId);
    }

    private void initComponents(String studentId) {
        setTitle("Internship Progress & Feedback");
        setSize(850, 700); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainContent.setBackground(Color.WHITE);

        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Internship Hours (Goal: 400h)"));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setMaximumSize(new Dimension(2000, 80)); 
        
        double totalHours = DBHelper.getTotalVerifiedHours(studentId);
        int targetHours = 400;
        int progressPercent = (int) ((totalHours / targetHours) * 100);
        if (progressPercent > 100) progressPercent = 100;

        JProgressBar bar = new JProgressBar(0, targetHours);
        bar.setValue((int) totalHours);
        bar.setStringPainted(true);
        bar.setString((int)totalHours + " / " + targetHours + " Hours (" + progressPercent + "%)");
        bar.setForeground(new Color(60, 179, 113)); 
        bar.setFont(new Font("Arial", Font.BOLD, 14));
        bar.setPreferredSize(new Dimension(0, 35));

        progressPanel.add(bar, BorderLayout.CENTER);
        
        mainContent.add(progressPanel);
        mainContent.add(Box.createRigidArea(new Dimension(0, 20))); 

        String[] data = DBHelper.getStudentFeedback(studentId);
        
        String cScore = "Pending";
        String cFeed = "No feedback submitted yet.";
        String aScore = "Pending";
        String aFeed = "No feedback submitted yet.";

        if (data != null) {
            if (!data[5].equals("N/A") && !data[5].isEmpty()) cScore = data[5] + "/100";
            if (!data[6].equals("N/A") && !data[6].isEmpty()) aScore = data[6] + "/100";
            if (!data[7].equals("N/A") && !data[7].isEmpty()) cFeed = data[7];
            if (!data[8].equals("N/A") && !data[8].isEmpty()) aFeed = data[8];
        }

        JPanel pnlCompany = createFeedbackPanel("Company Supervisor Evaluation", cScore, cFeed, new Color(235, 245, 255));
        mainContent.add(pnlCompany);
        mainContent.add(Box.createRigidArea(new Dimension(0, 15))); 

        JPanel pnlAcademic = createFeedbackPanel("Academic Supervisor Evaluation", aScore, aFeed, new Color(240, 255, 235));
        mainContent.add(pnlAcademic);

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Close");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.addActionListener(e -> dispose());
        btnPanel.add(btnClose);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    private JPanel createFeedbackPanel(String title, String score, String feedback, Color bg) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.setBackground(bg);
        panel.setMaximumSize(new Dimension(2000, 250)); 

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        header.setBorder(new EmptyBorder(10, 15, 5, 15));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.DARK_GRAY);
        
        JLabel lblScore = new JLabel("Score: " + score);
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblScore.setForeground(score.equals("Pending") ? new Color(200, 50, 50) : new Color(34, 139, 34));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblScore, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JTextArea txtDesc = new JTextArea(feedback);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        
        txtDesc.setFocusable(false);
        txtDesc.setHighlighter(null);
        txtDesc.setBackground(bg);
        txtDesc.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(txtDesc);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY), 
            "Comments / Description"
        ));
        scroll.setBackground(bg);
        scroll.getViewport().setBackground(bg);
        scroll.setPreferredSize(new Dimension(0, 120)); 
        
        JPanel bodyContainer = new JPanel(new BorderLayout());
        bodyContainer.setBackground(bg);
        bodyContainer.setBorder(new EmptyBorder(0, 10, 10, 10));
        bodyContainer.add(scroll);

        panel.add(bodyContainer, BorderLayout.CENTER);

        return panel;
    }
}