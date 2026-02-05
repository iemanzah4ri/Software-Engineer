import javax.swing.*;
import java.awt.*;

public class UserManagementHub extends JFrame {

    public UserManagementHub() {
        setTitle("Register & Manage User Account");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); 

        JLabel title = new JLabel("Register & Manage User Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); 
        add(title, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 100, 30, 100));

        JButton b1 = new JButton("Register Student");
        JButton b2 = new JButton("Edit Student");
        JButton b3 = new JButton("Register Company Supervisor");
        JButton b4 = new JButton("Edit Company Supervisor");
        JButton b5 = new JButton("Register Academic Supervisor");
        JButton b6 = new JButton("Edit Academic Supervisor");
        JButton bBack = new JButton("Back Home");

        b1.addActionListener(e -> new AddStudentUI());           
        b2.addActionListener(e -> new EditStudentUI());          
        b3.addActionListener(e -> new AddCompanySupervisorUI().setVisible(true)); 
        b4.addActionListener(e -> new EditCompanySupervisorUI().setVisible(true));
        b5.addActionListener(e -> new AddAcademicSupervisorUI());
        b6.addActionListener(e -> new EditAcademicSupervisorUI());

        bBack.addActionListener(e -> {
            this.dispose();
            new AdminHome().setVisible(true);
        });

        panel.add(b1); panel.add(b2);
        panel.add(b3); panel.add(b4);
        panel.add(b5); panel.add(b6);
        panel.add(bBack);

        add(panel, BorderLayout.CENTER);
    }
}