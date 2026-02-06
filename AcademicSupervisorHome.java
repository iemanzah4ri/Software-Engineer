import javax.swing.*;
import java.awt.*;

public class AcademicSupervisorHome extends JFrame {
    private String supervisorId;
    private String supervisorName;

    public AcademicSupervisorHome(String id) {
        this.supervisorId = id;
        String[] d = DBHelper.getUserById(id);
        this.supervisorName = (d!=null)?d[3]:"Academic Supervisor";
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Academic Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome " + supervisorName);
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(6, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // b1
        JButton b1 = new JButton("View Assigned Student Profile"); // Placeholder from your original code? 
        b1.addActionListener(e -> new ViewStudentProfileUI(supervisorId).setVisible(true));

        // b2
        JButton b2 = new JButton("View Student Logbook");
        b2.addActionListener(e -> new AcademicViewLogbooksUI().setVisible(true));
        
        // b3
        JButton b3 = new JButton("Submit Academic Evaluation");
        b3.addActionListener(e -> new AcademicEvaluation(supervisorId).setVisible(true));

        // b4 - THIS IS THE NEW FEATURE
        JButton b4 = new JButton("View Internship Progress Dashboard");
        b4.addActionListener(e -> new AcademicViewProgressUI(supervisorId).setVisible(true));

        // b5
        JButton b5 = new JButton("View Notifications");
        b5.addActionListener(e -> JOptionPane.showMessageDialog(this, "No new notifications."));

        // bOut
        JButton bOut = new JButton("Logout");
        bOut.addActionListener(e -> { dispose(); new LoginForm().setVisible(true); });

        buttons.add(b1); buttons.add(b2); buttons.add(b3); 
        buttons.add(b4); buttons.add(b5); buttons.add(bOut);

        add(buttons, BorderLayout.CENTER);
    }
}