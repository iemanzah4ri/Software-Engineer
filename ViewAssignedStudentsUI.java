import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewAssignedStudentsUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    public ViewAssignedStudentsUI() {
        initComponents();
        loadStudents();
    }

    private void initComponents() {
        setTitle("View Assigned Student");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel title = new JLabel("View Assigned Student");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        header.add(title);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Intake", "Job Description", "Status"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel footer = new JPanel();
        JButton btnAccept = new JButton("Accept");
        JButton btnReject = new JButton("Reject");
        JButton btnBack = new JButton("Back Home");

        btnAccept.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = table.getValueAt(row, 0).toString();

                DBHelper.updateStudentPlacement(id, "Placed");

                JOptionPane.showMessageDialog(this, "Student Accepted! Status changed to 'Placed'.");

                loadStudents(); 
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first.");
            }
        });

        btnReject.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                String id = table.getValueAt(row, 0).toString();

                DBHelper.updateStudentPlacement(id, "Rejected");
                JOptionPane.showMessageDialog(this, "Student Rejected.");
                loadStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student first.");
            }
        });
        
        btnBack.addActionListener(e -> {
            this.dispose();
            new CompanySupervisorHome().setVisible(true);
        });

        footer.add(btnAccept);
        footer.add(btnReject);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadStudents() {
        model.setRowCount(0);
        List<String[]> students = DBHelper.getUsersByRole("Student", "");
        
        for (String[] s : students) {
            String id = s[0];
            String[] fullDetails = DBHelper.getUserById(id);
            
            if (fullDetails != null) {
                String name = fullDetails[3];
                String intake = fullDetails[4];
                String status = fullDetails[9]; // Get current status
                
                model.addRow(new Object[]{id, name, intake, "Java Developer", status});
            }
        }
    }
}