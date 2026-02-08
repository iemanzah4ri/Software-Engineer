package common;
// imports
import javax.swing.*;
import java.awt.*;
import java.util.List;

// just a basic form for evaluations
public class GenericEvaluationUI extends JFrame {

    private JComboBox<String> studentBox;
    private JTextArea feedbackArea;
    private JTextField scoreField;

    // constructor
    public GenericEvaluationUI() {
        initComponents();
        loadStudents();
    }

    // building the ui
    private void initComponents() {
        // window setup
        setTitle("Submit Performance Evaluation");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // using gridbag because layout managers are annoying
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // inputs
        studentBox = new JComboBox<>();
        feedbackArea = new JTextArea(5, 20);
        scoreField = new JTextField(10);

        // adding stuff to panel
        gbc.gridx=0; gbc.gridy=0; formPanel.add(new JLabel("Select Student:"), gbc);
        gbc.gridx=1; formPanel.add(studentBox, gbc);

        gbc.gridx=0; gbc.gridy=1; formPanel.add(new JLabel("Feedback:"), gbc);
        gbc.gridx=1; formPanel.add(new JScrollPane(feedbackArea), gbc);

        gbc.gridx=0; gbc.gridy=2; formPanel.add(new JLabel("Score (%):"), gbc);
        gbc.gridx=1; formPanel.add(scoreField, gbc);

        add(formPanel, BorderLayout.CENTER);

        // buttons at bottom
        JPanel footer = new JPanel();
        JButton btnSubmit = new JButton("Submit");
        JButton btnBack = new JButton("Back Home");

        // fake submit for now
        btnSubmit.addActionListener(e -> JOptionPane.showMessageDialog(this, "Evaluation Submitted!"));
        
        btnBack.addActionListener(e -> {
            this.dispose();
        });

        footer.add(btnSubmit);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }
    
    // fill the dropdown
    private void loadStudents() {
        // getting student list from db
        List<String[]> students = DatabaseHelper.getUsersByRole("Student", ""); 
        for(String[] s : students) {
            studentBox.addItem(s[2] + " (" + s[0] + ")");
        }
    }
}