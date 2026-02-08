package admin;
// imports
import common.*;
import javax.swing.*;
import java.awt.*;

// this is the hub to manage all user accounts (add/edit)
public class AdminUserHubUI extends JFrame {

    // constructor to build the menu
    public AdminUserHubUI() {
        // window settings
        setTitle("Register & Manage User Account");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); 

        // title at the top
        JLabel title = new JLabel("Register & Manage User Account", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        // spacing
        title.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0)); 
        add(title, BorderLayout.NORTH);

        // panel for the buttons, using grid layout for a stack look
        JPanel panel = new JPanel(new GridLayout(7, 1, 10, 10)); 
        // squeeze the buttons a bit
        panel.setBorder(BorderFactory.createEmptyBorder(10, 100, 30, 100));

        // create all the buttons
        JButton b1 = new JButton("Register Student");
        JButton b2 = new JButton("Edit Student");
        JButton b3 = new JButton("Register Company Supervisor");
        JButton b4 = new JButton("Edit Company Supervisor");
        JButton b5 = new JButton("Register Academic Supervisor");
        JButton b6 = new JButton("Edit Academic Supervisor");
        JButton bBack = new JButton("Back Home");

        // add listeners to open the specific forms
        b1.addActionListener(e -> new AdminAddStudentUI()); // open add student
        b2.addActionListener(e -> new AdminEditStudentUI().setVisible(true)); // open edit student
        b3.addActionListener(e -> new AdminAddCompanyUI().setVisible(true)); // open add company sv
        b4.addActionListener(e -> new AdminEditCompanyUI().setVisible(true)); // open edit company sv
        b5.addActionListener(e -> new AdminAddAcademicUI()); // open add academic sv
        b6.addActionListener(e -> new AdminEditAcademicUI().setVisible(true)); // open edit academic sv

        // back to main dashboard
        bBack.addActionListener(e -> {
            this.dispose();
            new AdminDashboardUI().setVisible(true); 
        });

        // add buttons to panel
        panel.add(b1); panel.add(b2);
        panel.add(b3); panel.add(b4);
        panel.add(b5); panel.add(b6);
        panel.add(bBack);

        // put panel in center
        add(panel, BorderLayout.CENTER);
    }
}