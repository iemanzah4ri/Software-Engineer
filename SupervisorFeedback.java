import javax.swing.*;
import java.awt.*;

public class SupervisorFeedback extends JFrame {
    public SupervisorFeedback(String studentId, String studentName) {
        setTitle("Supervisor Feedback");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        String[] feedback = DBHelper.getStudentFeedback(studentId);
        String compMsg = "No feedback yet.";
        String acadMsg = "No feedback yet.";

        if (feedback != null) {
            compMsg = feedback[7];
            acadMsg = feedback[8];
        }

        JTextArea txtComp = new JTextArea("Company Feedback:\n" + compMsg);
        txtComp.setEditable(false);
        txtComp.setLineWrap(true);
        
        JTextArea txtAcad = new JTextArea("Academic Feedback:\n" + acadMsg);
        txtAcad.setEditable(false);
        txtAcad.setLineWrap(true);

        add(new JScrollPane(txtComp));
        add(new JScrollPane(txtAcad));
        
        setLocationRelativeTo(null);
    }
}