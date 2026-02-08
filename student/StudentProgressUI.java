package student;
// imports
import common.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// shows charts and grades
public class StudentProgressUI extends JFrame {
    
    // constructor
    public StudentProgressUI(String studentId, String studentName) {
        initComponents(studentId);
    }

    // setup ui
    private void initComponents(String studentId) {
        // setup window
        setTitle("Internship Progress & Feedback");
        setSize(850, 800); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainContent.setBackground(Color.WHITE);

        // get current status from db
        String[] studentData = DatabaseHelper.getUserById(studentId);
        String dbStatus = (studentData != null && studentData.length > 9) ? studentData[9] : "Unknown";

        // status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createTitledBorder("Current Status"));
        statusPanel.setMaximumSize(new Dimension(2000, 70));

        JLabel lblStatus = new JLabel();
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // check hours
        double totalHours = DatabaseHelper.getTotalVerifiedHours(studentId);
        int targetHours = 400;

        // logic to decide what to show based on status
        if (dbStatus.equalsIgnoreCase("Terminated")) {
            lblStatus.setText("Status: TERMINATED");
            lblStatus.setForeground(Color.RED); 
        } else if (totalHours >= targetHours || dbStatus.equalsIgnoreCase("Completed")) {
            lblStatus.setText("Status: COMPLETED (Internship Finished)");
            lblStatus.setForeground(new Color(34, 139, 34)); //green
        } else {
            lblStatus.setText("Status: ACTIVE INTERN (In Progress)");
            lblStatus.setForeground(Color.BLUE);
        }
        statusPanel.add(lblStatus);
        
        mainContent.add(statusPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // hours progress bar
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Internship Hours (Goal: 400h)"));
        progressPanel.setBackground(Color.WHITE);
        progressPanel.setMaximumSize(new Dimension(2000, 80)); 
        
        int progressPercent = (int) ((totalHours / targetHours) * 100);
        if (progressPercent > 100) progressPercent = 100;

        JProgressBar bar = new JProgressBar(0, targetHours);
        bar.setValue((int) totalHours);
        bar.setStringPainted(true);
        bar.setString((int)totalHours + " / " + targetHours + " Hours (" + progressPercent + "%)");
        
        progressPanel.add(bar, BorderLayout.CENTER);
        mainContent.add(progressPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // attendance progress bar
        int attendancePct = DatabaseHelper.calculateAttendancePercentage(studentId); 
        JPanel attPanel = new JPanel(new BorderLayout());
        attPanel.setBorder(BorderFactory.createTitledBorder("Attendance Reliability"));
        attPanel.setBackground(Color.WHITE);
        attPanel.setMaximumSize(new Dimension(2000, 60));
        
        JProgressBar attBar = new JProgressBar(0, 100);
        attBar.setValue(attendancePct);
        attBar.setStringPainted(true);
        // red if low attendance
        attBar.setForeground(attendancePct >= 80 ? new Color(34, 139, 34) : Color.RED);
        attPanel.add(attBar);
        mainContent.add(attPanel);
        mainContent.add(Box.createVerticalStrut(20));

        // feedback section
        String[] fb = DatabaseHelper.getStudentFeedback(studentId);
        String companyScore = "Pending", companyFeedback = "No feedback yet.";
        String academicScore = "Pending", academicFeedback = "No feedback yet.";
        int cVal = 0, aVal = 0;
        boolean cDone = false, aDone = false;
        
        // check if feedback exists and parse it
        if (fb != null) {
            if (!fb[5].equals("N/A")) { 
                companyScore = fb[5] + "/100"; 
                companyFeedback = fb[7]; 
                try { cVal = Integer.parseInt(fb[5]); cDone = true; } catch(Exception e){}
            }
            if (!fb[6].equals("N/A")) { 
                academicScore = fb[6] + "/100"; 
                academicFeedback = fb[8]; 
                try { aVal = Integer.parseInt(fb[6]); aDone = true; } catch(Exception e){}
            }
        }

        // add feedback panels
        mainContent.add(createFeedbackPanel("Company Supervisor Evaluation", companyScore, companyFeedback, new Color(240, 248, 255)));
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(createFeedbackPanel("Academic Supervisor Evaluation", academicScore, academicFeedback, new Color(255, 250, 240)));
        mainContent.add(Box.createVerticalStrut(20));

        // overall score section
        JPanel overallPanel = new JPanel(new BorderLayout());
        overallPanel.setBorder(BorderFactory.createTitledBorder("Final Internship Grade"));
        overallPanel.setBackground(Color.WHITE);
        overallPanel.setMaximumSize(new Dimension(2000, 80));

        JLabel lblFinal = new JLabel();
        lblFinal.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblFinal.setHorizontalAlignment(SwingConstants.CENTER);

        // only show final if both are done
        if (cDone && aDone) {
            int avg = (cVal + aVal) / 2;
            lblFinal.setText("Overall Score: " + avg + "/100");
            if(avg >= 50) lblFinal.setForeground(new Color(34, 139, 34)); // pass
            else lblFinal.setForeground(Color.RED); // fail
        } else {
            lblFinal.setText("Overall Score: Pending (Waiting for both evaluations)");
            lblFinal.setForeground(Color.GRAY);
        }
        
        overallPanel.add(lblFinal, BorderLayout.CENTER);
        mainContent.add(overallPanel);

        // scroll pane for main content
        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        // footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnBack = new JButton("Back to Dashboard");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.addActionListener(e -> dispose());
        
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // helper to make feedback boxes with color
    private JPanel createFeedbackPanel(String title, String score, String feedback, Color bg) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(bg);
        panel.setMaximumSize(new Dimension(2000, 150));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblScore = new JLabel("Score: " + score);
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // red text if pending
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
        txtDesc.setBackground(bg);
        txtDesc.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        panel.add(txtDesc, BorderLayout.CENTER);
        return panel;
    }
}