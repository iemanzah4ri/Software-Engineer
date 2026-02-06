import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CompanyEvaluation extends JFrame {
    private String supervisorId;
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtScore;
    private JTextArea txtFeedback;

    public CompanyEvaluation(String id) {
        this.supervisorId = id;
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setTitle("Company Evaluation");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"ID", "Name", "Position"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // South Panel for inputs
        JPanel p = new JPanel(new GridLayout(3, 1));
        
        // Row 1: Score
        txtScore = new JTextField(); 
        JPanel scoreP = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scoreP.add(new JLabel("Score (0-100): ")); 
        scoreP.add(txtScore); 
        txtScore.setColumns(10);
        p.add(scoreP);
        
        // Row 2: Feedback
        txtFeedback = new JTextArea();
        p.add(new JScrollPane(txtFeedback));
        
        // Row 3: Buttons (Back and Submit)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose()); // Closes window
        
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(e -> submit());
        
        btnPanel.add(btnBack);
        btnPanel.add(btnSubmit);
        
        p.add(btnPanel);
        
        add(p, BorderLayout.SOUTH);
    }

    private void loadStudents() {
        model.setRowCount(0);
        // Using the same unified method
        for(String[] m : DBHelper.getMatchesForSupervisor(supervisorId)) {
            model.addRow(new Object[]{m[1], m[2], m[5]});
        }
    }

    private void submit() {
        int r = table.getSelectedRow();
        if(r==-1) { JOptionPane.showMessageDialog(this, "Select a student."); return; }
        
        String[] sv = DBHelper.getUserById(supervisorId);
        String cname = (sv!=null && sv.length>8) ? sv[8] : "Unknown";
        
        DBHelper.saveCompanyFeedback(
            model.getValueAt(r, 0).toString(), 
            model.getValueAt(r, 1).toString(), 
            cname, 
            txtScore.getText(), 
            txtFeedback.getText()
        );
        JOptionPane.showMessageDialog(this, "Saved!");
    }
}