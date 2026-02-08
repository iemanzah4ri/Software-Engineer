package academic;
// importing the stuff i need
import common.*;
import javax.swing.*;
import java.awt.*;

// this is the main screen for the academic supervisor
public class AcademicDashboardUI extends JFrame {
    // variables to store who is logged in
    private String supervisorId;
    private String supervisorName;

    // constructor called when opening the window
    public AcademicDashboardUI(String id) {
        // save the id passed in
        this.supervisorId = id;
        // get the user details from the database
        String[] d = DatabaseHelper.getUserById(id); 
        // if user found, get name (index 3), else use default
        this.supervisorName = (d!=null)?d[3]:"Academic Supervisor";
        // setup the ui components
        initComponents();
        // window size
        setSize(800, 600);
        // center the window
        setLocationRelativeTo(null);
        // see if there are new alerts
        checkNotifications();
    }
    
    // function to check for unread messages
    private void checkNotifications() {
        // check helper to see if true
        if (NotificationHelper.hasUnreadNotifications(supervisorId)) {
            // run this later to not freeze ui
            SwingUtilities.invokeLater(() -> {
                // get the number of unreads
                int unreadCount = countUnreadNotifications();
                // show a popup message
                JOptionPane.showMessageDialog(this, 
                    "You have " + unreadCount + " unread notification(s)!", 
                    "New Notifications", 
                    JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    // helper to count how many messages are "unread"
    private int countUnreadNotifications() {
        // start at zero
        int count = 0;
        // loop through all notifications for this id
        for (String[] notif : NotificationHelper.getNotifications(supervisorId)) {
            // check if the status column says Unread
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                // add to count
                count++;
            }
        }
        // return the total
        return count;
    }

    // setting up all the buttons and panels
    private void initComponents() {
        // close app when x is clicked
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // title bar text
        setTitle("Academic Supervisor Dashboard");
        // use border layout for main frame
        setLayout(new BorderLayout());

        // header panel for welcome message
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        // welcome label
        JLabel lblWelcome = new JLabel("Welcome " + supervisorName);
        // make the font bigger
        lblWelcome.setFont(new Font("Dialog", Font.BOLD, 24));
        // add label to header
        header.add(lblWelcome);
        // put header at the top
        add(header, BorderLayout.NORTH);

        // panel for all the menu buttons
        JPanel buttons = new JPanel(new GridLayout(6, 1, 10, 10));
        // add some padding on the sides so buttons arent huge
        buttons.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));

        // button 1: view profile
        JButton b1 = new JButton("View Assigned Student Profile");
        // open the profile window
        b1.addActionListener(e -> new SupervisorStudentProfileUI(supervisorId).setVisible(true)); 

        // button 2: logbooks
        JButton b2 = new JButton("View Student Logbook");
        // open logbook window
        b2.addActionListener(e -> new AcademicLogReviewUI().setVisible(true)); 
        
        // button 3: evaluation
        JButton b3 = new JButton("Submit Academic Evaluation");
        // open grading window
        b3.addActionListener(e -> new AcademicEvaluationUI(supervisorId).setVisible(true)); 

        // button 4: progress dashboard
        JButton b4 = new JButton("View Internship Progress Dashboard");
        // open the progress graphs
        b4.addActionListener(e -> new AcademicProgressUI(supervisorId).setVisible(true)); 

        // button 5: notifications
        JButton b5 = new JButton("View Notifications");
        // open message list
        b5.addActionListener(e -> new NotificationViewUI(supervisorId).setVisible(true));

        // logout button
        JButton bOut = new JButton("Logout");
        // close this and go back to login
        bOut.addActionListener(e -> { dispose(); new LoginUI().setVisible(true); }); 

        // add all buttons to the panel
        buttons.add(b1); buttons.add(b2); buttons.add(b3); 
        buttons.add(b4); buttons.add(b5); buttons.add(bOut);

        // put button panel in the middle
        add(buttons, BorderLayout.CENTER);
    }
}