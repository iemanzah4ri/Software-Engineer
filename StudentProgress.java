import javax.swing.*;
import java.awt.*;

public class StudentProgress extends JFrame {
    public StudentProgress(String studentId, String studentName) {
        setTitle("Internship Progress");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 1));
        
        String[] feedback = DBHelper.getStudentFeedback(studentId);
        int val = 0;
        
        if (feedback != null) {
            try {
                int c = Integer.parseInt(feedback[5]);
                int a = Integer.parseInt(feedback[6]);
                val = (c + a) / 2;
                add(new JLabel("Company Evaluation: " + c + "/100", SwingConstants.CENTER));
                add(new JLabel("Academic Evaluation: " + a + "/100", SwingConstants.CENTER));
            } catch (Exception e) {
                add(new JLabel("Evaluation pending...", SwingConstants.CENTER));
            }
        } else {
            add(new JLabel("No data available.", SwingConstants.CENTER));
        }

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(val);
        bar.setStringPainted(true);
        add(new JLabel("Overall Progress:", SwingConstants.CENTER));
        add(bar);
        
        setLocationRelativeTo(null);
    }
}