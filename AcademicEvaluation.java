import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AcademicEvaluation extends javax.swing.JFrame {
    private final String supervisorType; 
    private final String supervisorName;

    private JTable tblStudents;
    private DefaultTableModel tableModel;
    private JTextArea txtFeedback;
    private JTextField txtScore;
    private JButton btnSubmit;
    private JButton btnBack;

    public AcademicEvaluation(String supervisorType, String supervisorName) {
        this.supervisorType = supervisorType;
        this.supervisorName = supervisorName;
        initComponents();
        loadStudents();
    }

    public AcademicEvaluation() {
        this("Academic", "Unknown Supervisor");
    }

    private void initComponents() {
        setTitle("Submit Academic Performance Evaluation");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);

        String[] cols = {"ID", "Name", "Intake", "Job Position"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblStudents = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(tblStudents);

        txtFeedback = new JTextArea(6, 30);
        txtFeedback.setLineWrap(true);
        txtFeedback.setWrapStyleWord(true);

        txtScore = new JTextField(10);

        btnSubmit = new JButton("Submit Evaluation");
        btnBack = new JButton("Back");

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        right.add(new JLabel("Feedback:"));
        right.add(new JScrollPane(txtFeedback));
        right.add(Box.createVerticalStrut(10));
        
        right.add(new JLabel("Score (0-100):"));
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

            int score;
            try {
                score = Integer.parseInt(scoreStr);
                if(score < 0 || score > 100) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Score must be a number between 0 and 100.");
                return;
            }

            DBHelper.saveAcademicFeedback(studentId, studentName, String.valueOf(score), feedback);

            JOptionPane.showMessageDialog(this, "Feedback submitted successfully.");
            txtFeedback.setText("");
            txtScore.setText("");
        });

        btnBack.addActionListener(e -> dispose());
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        File f = new File("database/matches.txt");
        if (!f.exists()) return;
        
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 6) continue;
                
                String id = p[1].trim(); 
                String name = p[2].trim(); 
                String position = p[5].trim(); 
                
                String[] userDetails = DBHelper.getUserById(id);
                String intake = (userDetails != null && userDetails.length > 4) ? userDetails[4] : "Unknown";

                tableModel.addRow(new Object[]{id, name, intake, position});
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}