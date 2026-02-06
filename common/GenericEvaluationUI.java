//generic evaluation interface
//(base class or alternative for evaluations)
package common;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GenericEvaluationUI extends JFrame {

    private JComboBox<String> studentBox;
    private JTextArea feedbackArea;
    private JTextField scoreField;

    public GenericEvaluationUI() {
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setTitle("Submit Performance Evaluation");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        studentBox = new JComboBox<>();
        feedbackArea = new JTextArea(5, 20);
        scoreField = new JTextField(10);

        gbc.gridx=0; gbc.gridy=0; formPanel.add(new JLabel("Select Student:"), gbc);
        gbc.gridx=1; formPanel.add(studentBox, gbc);

        gbc.gridx=0; gbc.gridy=1; formPanel.add(new JLabel("Feedback:"), gbc);
        gbc.gridx=1; formPanel.add(new JScrollPane(feedbackArea), gbc);

        gbc.gridx=0; gbc.gridy=2; formPanel.add(new JLabel("Score (%):"), gbc);
        gbc.gridx=1; formPanel.add(scoreField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnSubmit = new JButton("Submit");
        JButton btnBack = new JButton("Back Home");

        btnSubmit.addActionListener(e -> JOptionPane.showMessageDialog(this, "Evaluation Submitted!"));
        
        btnBack.addActionListener(e -> {
            this.dispose();
        });

        footer.add(btnSubmit);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }
    
    private void loadStudents() {
        // Placeholder loading logic
        List<String[]> students = DatabaseHelper.getUsersByRole("Student", ""); // UPDATED
        for(String[] s : students) {
            studentBox.addItem(s[2] + " (" + s[0] + ")");
        }
    }
}