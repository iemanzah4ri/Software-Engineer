package academic;
// imports for db and swing
import common.DatabaseHelper;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

// screen to show visual progress bars
public class AcademicProgressUI extends JFrame {

    // vars
    private String supervisorId;
    private JComboBox<String> studentBox; // dropdown
    private List<String> studentIds; // keep track of ids
    
    // labels for scores
    private JLabel lblCompScore, lblAcadScore, lblFinalScore, lblHoursText;
    private JLabel lblCompTitle; 
    // progress bars
    private JProgressBar barComp, barAcad, barTotal, barHours;
    // feedback text areas
    private JTextArea txtCompFeed, txtAcadFeed;
    private JPanel contentPanel;

    // constructor
    public AcademicProgressUI(String supervisorId) {
        // set vars
        this.supervisorId = supervisorId;
        this.studentIds = new ArrayList<>();
        // run setup
        initComponents();
        // fill dropdown
        loadStudents();
    }

    // setup the frame
    private void initComponents() {
        // window settings
        setTitle("Student Internship Progress");
        setSize(850, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        // title
        JLabel title = new JLabel("Internship Progress Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(50, 50, 50));
        headerPanel.add(title, BorderLayout.NORTH);

        // selection area
        JPanel selectPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        selectPanel.setBackground(Color.WHITE);
        
        JLabel lblSelect = new JLabel("Select Student: ");
        lblSelect.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // dropdown box setup
        studentBox = new JComboBox<>();
        studentBox.setPreferredSize(new Dimension(300, 35));
        studentBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentBox.setBackground(Color.WHITE);
        // update everything when dropdown changes
        studentBox.addActionListener(e -> updateProgress());
        
        selectPanel.add(lblSelect);
        selectPanel.add(studentBox);
        headerPanel.add(selectPanel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);

        // main content area (scrollable)
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(10, 40, 20, 40));
        
        // scroll pane
        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(null);
        // fix slow scrolling speed
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(mainScroll, BorderLayout.CENTER);

        // section for hours
        JLabel lblHoursTitle = new JLabel("Internship Hours Completed (Goal: 400h)");
        // custom helper to make section
        JPanel pnlHours = createSectionPanel(lblHoursTitle, new Color(255, 250, 240)); 
        
        // make bar
        barHours = createProgressBar();
        barHours.setForeground(new Color(255, 140, 0)); // orange default
        
        // make label
        lblHoursText = createScoreLabel();
        
        // add stuff to panel
        pnlHours.add(barHours);
        pnlHours.add(Box.createVerticalStrut(5));
        pnlHours.add(lblHoursText);
        contentPanel.add(pnlHours);
        contentPanel.add(Box.createVerticalStrut(20));

        // section for company eval
        lblCompTitle = new JLabel("Company Supervisor Evaluation"); 
        JPanel pnlCompany = createSectionPanel(lblCompTitle, new Color(235, 245, 255));
        barComp = createProgressBar();
        lblCompScore = createScoreLabel();
        txtCompFeed = createFeedbackArea(new Color(235, 245, 255));
        
        // helper to add stuff
        addToSection(pnlCompany, barComp, lblCompScore, txtCompFeed);
        contentPanel.add(pnlCompany);
        contentPanel.add(Box.createVerticalStrut(20));

        // section for academic eval
        JLabel lblAcadTitle = new JLabel("Academic Supervisor Evaluation");
        JPanel pnlAcad = createSectionPanel(lblAcadTitle, new Color(240, 255, 235));
        barAcad = createProgressBar();
        lblAcadScore = createScoreLabel();
        txtAcadFeed = createFeedbackArea(new Color(240, 255, 235));
        
        addToSection(pnlAcad, barAcad, lblAcadScore, txtAcadFeed);
        contentPanel.add(pnlAcad);
        contentPanel.add(Box.createVerticalStrut(20));

        // section for total score
        JLabel lblTotalTitle = new JLabel("Overall Performance Score");
        JPanel pnlTotal = createSectionPanel(lblTotalTitle, new Color(250, 250, 250));
        barTotal = createProgressBar();
        barTotal.setForeground(new Color(34, 139, 34)); 
        lblFinalScore = createScoreLabel();
        
        pnlTotal.add(barTotal);
        pnlTotal.add(Box.createVerticalStrut(5));
        pnlTotal.add(lblFinalScore);
        contentPanel.add(pnlTotal);

        // footer with close button
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

    // helper function to create a panel with title and color
    private JPanel createSectionPanel(JLabel labelObj, Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        // fancy border
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // font styling
        labelObj.setFont(new Font("Segoe UI", Font.BOLD, 16));
        labelObj.setForeground(Color.DARK_GRAY);
        labelObj.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        p.add(labelObj);
        p.add(Box.createVerticalStrut(10));
        return p;
    }

    // helper to add components to the section
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

    // helper for progress bar
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

    // helper for score label
    private JLabel createScoreLabel() {
        JLabel l = new JLabel("Score: N/A");
        l.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    // helper for read only text area
    private JTextArea createFeedbackArea(Color bg) {
        JTextArea txt = new JTextArea(3, 40);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        // cant edit
        txt.setEditable(false);
        txt.setFocusable(false);
        txt.setHighlighter(null);
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txt.setBackground(bg);
        txt.setBorder(null);
        txt.setAlignmentX(Component.LEFT_ALIGNMENT);
        return txt;
    }

    // get list of students to fill dropdown
    private void loadStudents() {
        // clear old stuff
        studentBox.removeAllItems();
        studentIds.clear();

        // get matches
        List<String[]> matches = DatabaseHelper.getMatchesForSupervisor(supervisorId);
        
        // handle empty
        if (matches.isEmpty()) {
            studentBox.addItem("No Assigned Students Found");
            studentBox.setEnabled(false);
            return;
        }

        // loop and add
        for (String[] match : matches) {
            String name = match[2];
            String id = match[1];
            studentBox.addItem(name + " (" + id + ")");
            studentIds.add(id);
        }
        
        // select first one by default
        if (studentBox.getItemCount() > 0) studentBox.setSelectedIndex(0);
    }

    // logic to calculate and show progress
    private void updateProgress() {
        // get index
        int index = studentBox.getSelectedIndex();
        // safety check
        if (index < 0 || index >= studentIds.size()) return;

        // get id
        String studentId = studentIds.get(index);
        
        // get total hours
        double totalHours = DatabaseHelper.getTotalVerifiedHours(studentId);
        int targetHours = 400; // hardcoded goal
        
        // calc percent
        int hoursPercent = (int) ((totalHours / targetHours) * 100);
        // cap at 100
        if (hoursPercent > 100) hoursPercent = 100;

        // update bar
        barHours.setValue(hoursPercent);
        barHours.setString((int)totalHours + " / " + targetHours + " Hours (" + hoursPercent + "%)");
        
        // green if done, orange if not
        if (totalHours >= 400) {
            barHours.setForeground(new Color(34, 139, 34)); //green
            lblHoursText.setText("Status: Completed");
            lblHoursText.setForeground(new Color(34, 139, 34));
        } else {
            barHours.setForeground(new Color(255, 140, 0)); //orange
            lblHoursText.setText("Status: In Progress");
            lblHoursText.setForeground(Color.DARK_GRAY);
        }

        // find out who the company supervisor is
        String compSvName = "Unknown";
        List<String[]> allMatches = DatabaseHelper.getAllMatches();
        for(String[] m : allMatches) {
            // match student id
            if(m[1].equals(studentId)) {
                String compSvId = m[8]; 
                // get name if exists
                if(!compSvId.equals("N/A")) {
                    String[] u = DatabaseHelper.getUserById(compSvId);
                    if(u != null) compSvName = u[3]; 
                } else {
                    // fallback to old data
                    compSvName = m[4] + " (Legacy)";
                }
                break;
            }
        }
        
        // update label
        lblCompTitle.setText("Company Supervisor Evaluation (By: " + compSvName + ")");

        // get feedback data
        String[] feedback = DatabaseHelper.getStudentFeedback(studentId);

        // vars for scores
        int cScore = 0, aScore = 0;
        boolean cExists = false, aExists = false;
        String cText = "No written feedback provided yet.", aText = "No written feedback provided yet.";

        // check if feedback exists
        if (feedback != null) {
            // company score
            if (!feedback[5].equals("N/A") && !feedback[5].isEmpty()) {
                try { cScore = Integer.parseInt(feedback[5]); cExists = true; } catch (Exception e) {}
            }
            // academic score
            if (!feedback[6].equals("N/A") && !feedback[6].isEmpty()) {
                try { aScore = Integer.parseInt(feedback[6]); aExists = true; } catch (Exception e) {}
            }
            
            // comments
            if (!feedback[7].equals("N/A") && !feedback[7].isEmpty()) cText = feedback[7];
            if (!feedback[8].equals("N/A") && !feedback[8].isEmpty()) aText = feedback[8];
        }

        // update the UI sections
        updateSection(barComp, lblCompScore, txtCompFeed, cScore, cExists, cText, "Company Score: ");
        updateSection(barAcad, lblAcadScore, txtAcadFeed, aScore, aExists, aText, "Academic Score: ");

        // calc total score
        if (cExists && aExists) {
            // average of both
            int avg = (cScore + aScore) / 2;
            barTotal.setValue(avg);
            barTotal.setString(avg + "%");
            lblFinalScore.setText("Average: " + avg + "/100");
        } else if (cExists || aExists) {
            // only one exists
            int val = cExists ? cScore : aScore;
            barTotal.setValue(val);
            barTotal.setString(val + "% (Incomplete)");
            lblFinalScore.setText("Average: " + val + "/100 (Pending one evaluation)");
        } else {
            // nothing
            barTotal.setValue(0);
            barTotal.setString("No Data");
            lblFinalScore.setText("Average: N/A");
        }
    }

    // helper to update one section specifically
    private void updateSection(JProgressBar bar, JLabel lbl, JTextArea txt, int score, boolean exists, String feedback, String prefix) {
        if (exists) {
            // show score
            bar.setValue(score);
            bar.setString(score + "%");
            lbl.setText(prefix + score + "/100");
            lbl.setForeground(new Color(34, 139, 34)); 
        } else {
            // show pending
            bar.setValue(0);
            bar.setString("Pending");
            lbl.setText(prefix + "Not Submitted");
            lbl.setForeground(Color.RED);
        }
        // set text
        txt.setText(feedback);
    }
}