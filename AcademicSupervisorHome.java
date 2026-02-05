import javax.swing.*;
import java.awt.*;

public class AcademicSupervisorHome extends JFrame {

    public AcademicSupervisorHome() {
        initComponents();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Academic Supervisor Dashboard");
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        JLabel lblWelcome = new JLabel("Welcome Academic Supervisor");
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        header.add(lblWelcome);
        add(header, BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(6, 1, 10, 10));
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        JButton b1 = new JButton("View Assigned Student");
        JButton b2 = new JButton("View Student Logbook");
        JButton b3 = new JButton("Submit Academic Evaluation");
        JButton b4 = new JButton("View Internship Progress Dashboard");
        JButton b5 = new JButton("View Notifications");
        JButton bOut = new JButton("Logout");

        bOut.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
        
        b1.addActionListener(e -> JOptionPane.showMessageDialog(this, "Feature in development"));
        b2.addActionListener(e -> new AcademicViewLogbooksUI().setVisible(true));
        b3.addActionListener(e -> new AcademicEvaluation().setVisible(true));

        buttons.add(b1); buttons.add(b2); buttons.add(b3); 
        buttons.add(b4); buttons.add(b5); buttons.add(bOut);

        add(buttons, BorderLayout.CENTER);
    }
}