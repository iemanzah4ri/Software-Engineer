

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CompanyEvaluation extends javax.swing.JFrame {
    private final String supervisorType; // "Company" or "Academic"
    private final String supervisorName;

    private JTable tblStudents;
    private DefaultTableModel tableModel;
    private JTextArea txtFeedback;
    private JTextField txtScore;
    private JButton btnSubmit;
    private JButton btnBack;

    public static final String FEEDBACK_FILE = "Feedback.txt";

    public CompanyEvaluation(String supervisorType, String supervisorName) {
        this.supervisorType = supervisorType;
        this.supervisorName = supervisorName;
        initComponents();
        loadStudents();
    }

    // No-arg for testing
    public CompanyEvaluation() {
        this("Company", "Test Supervisor");
    }

    private void initComponents() {
        setTitle("Submit Performance Evaluation");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        String[] cols = {"ID", "Name", "Intake", "Job Description"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStudents = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tblStudents);

        txtFeedback = new JTextArea(6, 30);
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);

        txtScore = new JTextField(10);

        btnSubmit = new JButton("Submit");
        btnBack = new JButton("Back Home");
        btnBack.addActionListener(e -> {
        // Close this frame and go back to supervisor dashboard
        this.setVisible(false);

        if (supervisorType.equalsIgnoreCase("Company")) {
            new CompanySupervisorHome(supervisorName).setVisible(true);

    }

        dispose();
    });


        

        

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        right.add(new JLabel("Feedback"));
        right.add(new JScrollPane(txtFeedback));
        right.add(Box.createVerticalStrut(10));
        right.add(new JLabel("Score"));
        right.add(txtScore);
        right.add(Box.createVerticalStrut(10));
        right.add(btnSubmit);
        right.add(Box.createVerticalStrut(10));
        right.add(btnBack);

        getContentPane().setLayout(new BorderLayout(10,10));
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(right, BorderLayout.EAST);

        btnSubmit.addActionListener(e -> {
    int row = tblStudents.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(this, "Select a student first.");
        return;
    }

    String studentId = String.valueOf(tableModel.getValueAt(row, 0));
    String studentName = String.valueOf(tableModel.getValueAt(row, 1));
    String feedback = txtFeedback.getText().trim();
    String scoreStr = txtScore.getText().trim();

    if (feedback.isEmpty() || scoreStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter feedback and score.");
        return;
    }

    // validate score
    int score;
    try {
        score = Integer.parseInt(scoreStr);
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Score must be a number.");
        return;
    }

    boolean success;
    if (supervisorType.equalsIgnoreCase("Company")) {
        DBHelper.saveCompanyFeedback(studentId, studentName, supervisorName, String.valueOf(score), feedback);
        success = true;
    } else {
        DBHelper.saveAcademicFeedback(studentId, studentName, String.valueOf(score), feedback);
        success = true;
    }

    if (success) {
        JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
        txtFeedback.setText("");
        txtScore.setText("");
    } else {
        JOptionPane.showMessageDialog(this, "Error saving feedback.");
    }
});

    }

    private void loadStudents() {
        // Example: load from Internships.txt or Students.txt
        File f = new File("matches.txt");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 6) continue;
                String id = p[0].trim();
                String name = p[2].trim();
                String intake = p[5].trim();
                String jobDesc = p[3].trim();
                tableModel.addRow(new Object[]{id, name, intake, jobDesc});
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void submitEvaluation() {
        int row = tblStudents.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a student first.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String studentId = String.valueOf(tableModel.getValueAt(row, 0));
        String studentName = String.valueOf(tableModel.getValueAt(row, 1));
        String feedback = txtFeedback.getText().trim();
        String score = txtScore.getText().trim();

        if (feedback.isEmpty() || score.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter feedback and score.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        File f = new File(FEEDBACK_FILE);
        File temp = new File("Feedback_temp.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(f.exists() ? f : new File(""))); 
             PrintWriter pw = new PrintWriter(new FileWriter(temp))) {
            String line;
            boolean updated = false;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 9) { pw.println(line); continue; }
                if (p[1].trim().equals(studentId)) {
                    if (supervisorType.equalsIgnoreCase("Company")) {
                        p[5] = score;
                        p[7] = feedback;
                    } else {
                        p[6] = score;
                        p[8] = feedback;
                    }
                    pw.println(String.join(",", p));
                    updated = true;
                } else {
                    pw.println(line);
                }
            }
            if (!updated) {
                // new entry
                String[] p = new String[9];
                p[0] = String.valueOf(System.currentTimeMillis()); // id
                p[1] = studentId;
                p[2] = studentName;
                p[3] = supervisorName;
                p[4] = "Completed";
                p[5] = supervisorType.equalsIgnoreCase("Company") ? score : "";
                p[6] = supervisorType.equalsIgnoreCase("Academic") ? score : "";
                p[7] = supervisorType.equalsIgnoreCase("Company") ? feedback : "";
                p[8] = supervisorType.equalsIgnoreCase("Academic") ? feedback : "";
                pw.println(String.join(",", p));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving feedback.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (f.exists()) f.delete();
        temp.renameTo(f);

        JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
        txtFeedback.setText("");
        txtScore.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CompanyEvaluation("Company", "ABC Corp").setVisible(true));
    }
}
