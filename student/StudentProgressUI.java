//visualizes student internship progress
//shows attendance rate and supervisor feedback
package student;
import common.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StudentProgressUI extends JFrame {
    
    public StudentProgressUI(String studentId, String studentName) {
        initComponents(studentId);
    }

    private void initComponents(String studentId) {
        setTitle("Internship Progress & Feedback");
        setSize(850, 750); 
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
        
        double totalHours = DatabaseHelper.getTotalVerifiedHours(studentId); // UPDATED
        int targetHours = 400;
        int progressPercent = (int) ((totalHours / targetHours) * 100);
        if (progressPercent > 100) progressPercent = 100;

        JProgressBar bar = new JProgressBar(0, targetHours);
        bar.setValue((int) totalHours);
        bar.setStringPainted(true);
        bar.setString((int)totalHours + " / " + targetHours + " Hours (" + progressPercent + "%)");
        
        progressPanel.add(bar, BorderLayout.CENTER);
        mainContent.add(progressPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // Attendance
        int attendancePct = DatabaseHelper.calculateAttendancePercentage(studentId); // UPDATED
        JPanel attPanel = new JPanel(new BorderLayout());
        attPanel.setBorder(BorderFactory.createTitledBorder("Attendance Reliability"));
        attPanel.setBackground(Color.WHITE);
        attPanel.setMaximumSize(new Dimension(2000, 60));
        
        JProgressBar attBar = new JProgressBar(0, 100);
        attBar.setValue(attendancePct);
        attBar.setStringPainted(true);
        attBar.setForeground(attendancePct >= 80 ? new Color(34, 139, 34) : Color.RED);
        attPanel.add(attBar);
        mainContent.add(attPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // Feedback Section
        String[] fb = DatabaseHelper.getStudentFeedback(studentId); // UPDATED
        String companyScore = "Pending", companyFeedback = "No feedback yet.";
        String academicScore = "Pending", academicFeedback = "No feedback yet.";
        
        if (fb != null) {
            if (!fb[5].equals("N/A")) { companyScore = fb[5] + "/100"; companyFeedback = fb[7]; }
            if (!fb[6].equals("N/A")) { academicScore = fb[6] + "/100"; academicFeedback = fb[8]; }
        }

        mainContent.add(createFeedbackPanel("Company Supervisor Evaluation", companyScore, companyFeedback, new Color(240, 248, 255)));
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(createFeedbackPanel("Academic Supervisor Evaluation", academicScore, academicFeedback, new Color(255, 250, 240)));

        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createFeedbackPanel(String title, String score, String feedback, Color bg) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(bg);
        panel.setMaximumSize(new Dimension(2000, 200));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
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
        
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }
}