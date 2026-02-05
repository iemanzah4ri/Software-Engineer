import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SupervisorFeedback extends JFrame {

    public SupervisorFeedback(String studentId, String studentName) {
        setTitle("Internship Performance Feedback");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        // Use a main container with padding
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);

        // Fetch Data
        // Indices: [5]=CompScore, [6]=AcadScore, [7]=CompFeed, [8]=AcadFeed
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

        // --- Company Panel (Light Blue) ---
        JPanel pnlCompany = createFeedbackPanel("Company Supervisor Evaluation", cScore, cFeed, new Color(235, 245, 255));
        mainPanel.add(pnlCompany);

        // --- Academic Panel (Light Green) ---
        JPanel pnlAcademic = createFeedbackPanel("Academic Supervisor Evaluation", aScore, aFeed, new Color(240, 255, 235));
        mainPanel.add(pnlAcademic);

        add(mainPanel);
    }

    private JPanel createFeedbackPanel(String title, String score, String feedback, Color bg) {
        JPanel panel = new JPanel(new BorderLayout());
        // Rounded-ish look with border
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.setBackground(bg);

        // --- Header Section ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bg);
        header.setBorder(new EmptyBorder(10, 15, 5, 15));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.DARK_GRAY);
        
        JLabel lblScore = new JLabel("Score: " + score);
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 16));
        // Red if pending, Dark Green if scored
        lblScore.setForeground(score.equals("Pending") ? new Color(200, 50, 50) : new Color(34, 139, 34));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblScore, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // --- Description Section (Clean & Non-Clickable) ---
        JTextArea txtDesc = new JTextArea(feedback);
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        
        // MAKE IT "NOT CLICKABLE" & BLEND IN
        txtDesc.setFocusable(false);       // No cursor focus
        txtDesc.setHighlighter(null);      // Disable text selection highlighting
        txtDesc.setBackground(bg);         // Match panel background
        txtDesc.setBorder(new EmptyBorder(10, 10, 10, 10)); // Internal padding

        // ScrollPane styling
        JScrollPane scroll = new JScrollPane(txtDesc);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY), // Top line only
            "Comments / Description"
        ));
        scroll.setBackground(bg);
        scroll.getViewport().setBackground(bg); // Make viewport transparent-ish
        
        // Add padding around the scroll pane itself
        JPanel bodyContainer = new JPanel(new BorderLayout());
        bodyContainer.setBackground(bg);
        bodyContainer.setBorder(new EmptyBorder(0, 10, 10, 10));
        bodyContainer.add(scroll);

        panel.add(bodyContainer, BorderLayout.CENTER);

        return panel;
    }
}