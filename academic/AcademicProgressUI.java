package academic;
import common.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class AcademicProgressUI extends JFrame {

    private String supervisorId;
    private JComboBox<String> studentBox;
    private List<String> studentIds; 
    
    private JLabel lblCompScore, lblAcadScore, lblFinalScore, lblHoursText;
    private JLabel lblCompTitle; 
    private JProgressBar barComp, barAcad, barTotal, barHours;
    private JTextArea txtCompFeed, txtAcadFeed;
    private JPanel contentPanel;

    public AcademicProgressUI(String supervisorId) {
        this.supervisorId = supervisorId;
        this.studentIds = new ArrayList<>();
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        //setup window
        setTitle("Student Internship Progress");
        setSize(850, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        //header section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        JLabel title = new JLabel("Internship Progress Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));
        headerPanel.add(title, BorderLayout.NORTH);

        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectPanel.setBackground(Color.WHITE);
        
        JLabel lblSelect = new JLabel("Select Student: ");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        studentBox = new JComboBox<>();
        studentBox.setPreferredSize(new Dimension(300, 35));
        studentBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentBox.setBackground(Color.WHITE);
        studentBox.addActionListener(e -> updateProgress());
        
        selectPanel.add(lblSelect);
        selectPanel.add(studentBox);
        headerPanel.add(selectPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);

        //scrollable content area
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);

        //hours section
        JLabel lblHoursTitle = new JLabel("Internship Hours Completed (Goal: 400h)");
        JPanel pnlHours = createSectionPanel(lblHoursTitle, new Color(255, 250, 240)); 
        
        barHours = createProgressBar();
        barHours.setForeground(new Color(255, 140, 0)); //default orange
        
        lblHoursText = createScoreLabel();
        
        pnlHours.add(barHours);
        pnlHours.add(Box.createVerticalStrut(5));
        pnlHours.add(lblHoursText);
        contentPanel.add(pnlHours);
        contentPanel.add(Box.createVerticalStrut(20));

        //comp evaluation section
        lblCompTitle = new JLabel("Company Supervisor Evaluation"); 
        JPanel pnlCompany = createSectionPanel(lblCompTitle, new Color(235, 245, 255));
        barComp = createProgressBar();
        lblCompScore = createScoreLabel();
        txtCompFeed = createFeedbackArea(new Color(235, 245, 255));
        
        addToSection(pnlCompany, barComp, lblCompScore, txtCompFeed);
        contentPanel.add(pnlCompany);
        contentPanel.add(Box.createVerticalStrut(20));

        //acad evaluation section
        JLabel lblAcadTitle = new JLabel("Academic Supervisor Evaluation");
        JPanel pnlAcad = createSectionPanel(lblAcadTitle, new Color(240, 255, 235));
        barAcad = createProgressBar();
        lblAcadScore = createScoreLabel();
        txtAcadFeed = createFeedbackArea(new Color(240, 255, 235));
        
        addToSection(pnlAcad, barAcad, lblAcadScore, txtAcadFeed);
        contentPanel.add(pnlAcad);
        contentPanel.add(Box.createVerticalStrut(20));

        //all score section
        JLabel lblTotalTitle = new JLabel("Overall Performance Score");
        JPanel pnlTotal = createSectionPanel(lblTotalTitle, new Color(250, 250, 250));
        barTotal = createProgressBar();
        barTotal.setForeground(new Color(34, 139, 34)); 
        lblFinalScore = createScoreLabel();
        
        pnlTotal.add(barTotal);
        pnlTotal.add(Box.createVerticalStrut(5));
        pnlTotal.add(lblFinalScore);
        contentPanel.add(pnlTotal);

        //footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new EmptyBorder(10, 0, 20, 0));
        
        JButton btnBack = new JButton("Back Home");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.setBackground(new Color(240, 240, 240));
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> dispose());
        
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createSectionPanel(JLabel labelObj, Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        labelObj.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelObj.setForeground(Color.DARK_GRAY);
        labelObj.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        p.add(labelObj);
        p.add(Box.createVerticalStrut(10));
        return p;
    }

    private void addToSection(JPanel p, JProgressBar bar, JLabel score, JTextArea txt) {
        p.add(bar);
        p.add(Box.createVerticalStrut(5));
        p.add(score);
        p.add(Box.createVerticalStrut(10));
        
        JLabel lblComments = new JLabel("Comments:");
        lblComments.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblComments.setForeground(Color.GRAY);
        lblComments.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(lblComments);
        
        p.add(txt);
    }

    private JProgressBar createProgressBar() {
        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);
        bar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bar.setPreferredSize(new Dimension(500, 25));
        bar.setMaximumSize(new Dimension(2000, 25));
        bar.setAlignmentX(Component.LEFT_ALIGNMENT);
        bar.setBackground(Color.WHITE);
        return bar;
    }

    private JLabel createScoreLabel() {
        JLabel l = new JLabel("Score: N/A");
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextArea createFeedbackArea(Color bg) {
        JTextArea txt = new JTextArea(3, 40);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setEditable(false);
        txt.setFocusable(false);
        txt.setHighlighter(null);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBackground(bg);
        txt.setBorder(null);
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    private void loadStudents() {
        studentBox.removeAllItems();
        studentIds.clear();

        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        
        if (matches.isEmpty()) {
            studentBox.addItem("No Assigned Students Found");
            studentBox.setEnabled(false);
            return;
        }

        for (String[] match : matches) {
            String name = match[2];
            String id = match[1];
            studentBox.addItem(name + " (" + id + ")");
            studentIds.add(id);
        }
        
        if (studentBox.getItemCount() > 0) studentBox.setSelectedIndex(0);
    }

    private void updateProgress() {
        int index = studentBox.getSelectedIndex();
        if (index < 0 || index >= studentIds.size()) return;

        String studentId = studentIds.get(index);
        
        // --- HOURS LOGIC FIXED HERE ---
        double totalHours = DatabaseHelper.getTotalVerifiedHours(studentId);
        int targetHours = 400;
        int hoursPercent = (int) ((totalHours / targetHours) * 100);
        if (hoursPercent > 100) hoursPercent = 100;

        barHours.setValue(hoursPercent);
        barHours.setString((int)totalHours + " / " + targetHours + " Hours (" + hoursPercent + "%)");
        
        //change bar color to green if done, orange if not
        if (totalHours >= 400) {
            barHours.setForeground(new Color(34, 139, 34)); //green
            lblHoursText.setText("Status: Completed");
            lblHoursText.setForeground(new Color(34, 139, 34));
        } else {
            barHours.setForeground(new Color(255, 140, 0)); //orange
            lblHoursText.setText("Status: In Progress");
            lblHoursText.setForeground(Color.DARK_GRAY);
        }

        // --- REST OF THE LOGIC ---
        String compSvName = "Unknown";
        List<String[]> allMatches = DatabaseHelper.getAllMatches();
        for(String[] m : allMatches) {
            if(m[1].equals(studentId)) {
                String compSvId = m[8]; 
                if(!compSvId.equals("N/A")) {
                    String[] u = DatabaseHelper.getUserById(compSvId);
                    if(u != null) compSvName = u[3]; 
                } else {
                    compSvName = m[4] + " (Legacy)";
                }
                break;
            }
        }
        
        lblCompTitle.setText("Company Supervisor Evaluation (By: " + compSvName + ")");

        String[] feedback = DatabaseHelper.getStudentFeedback(studentId);

        int cScore = 0, aScore = 0;
        boolean cExists = false, aExists = false;
        String cText = "No written feedback provided yet.", aText = "No written feedback provided yet.";

        if (feedback != null) {
            if (!feedback[5].equals("N/A") && !feedback[5].isEmpty()) {
                try { cScore = Integer.parseInt(feedback[5]); cExists = true; } catch (Exception e) {}
            }
            if (!feedback[6].equals("N/A") && !feedback[6].isEmpty()) {
                try { aScore = Integer.parseInt(feedback[6]); aExists = true; } catch (Exception e) {}
            }
            
            if (!feedback[7].equals("N/A") && !feedback[7].isEmpty()) cText = feedback[7];
            if (!feedback[8].equals("N/A") && !feedback[8].isEmpty()) aText = feedback[8];
        }

        updateSection(barComp, lblCompScore, txtCompFeed, cScore, cExists, cText, "Company Score: ");
        updateSection(barAcad, lblAcadScore, txtAcadFeed, aScore, aExists, aText, "Academic Score: ");

        if (cExists && aExists) {
            int avg = (cScore + aScore) / 2;
            barTotal.setValue(avg);
            barTotal.setString(avg + "%");
            lblFinalScore.setText("Average: " + avg + "/100");
        } else if (cExists || aExists) {
            int val = cExists ? cScore : aScore;
            barTotal.setValue(val);
            barTotal.setString(val + "% (Incomplete)");
            lblFinalScore.setText("Average: " + val + "/100 (Pending one evaluation)");
        } else {
            barTotal.setValue(0);
            barTotal.setString("No Data");
            lblFinalScore.setText("Average: N/A");
        }
    }

    private void updateSection(JProgressBar bar, JLabel lbl, JTextArea txt, int score, boolean exists, String feedback, String prefix) {
        if (exists) {
            bar.setValue(score);
            bar.setString(score + "%");
            lbl.setText(prefix + score + "/100");
            lbl.setForeground(new Color(34, 139, 34)); 
        } else {
            bar.setValue(0);
            bar.setString("Pending");
            lbl.setText(prefix + "Not Submitted");
            lbl.setForeground(Color.RED);
        }
        txt.setText(feedback);
    }
}