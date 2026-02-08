package admin;
// import the stuff i need
import common.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// this screen is for assigning a uni supervisor to a student
public class AdminMatchingUI extends JFrame {
    // tables to show data
    private JTable placedTable, svTable;
    private DefaultTableModel placedModel, svModel;

    // constructor
    public AdminMatchingUI() {
        initComponents();
        // load the lists when window opens
        loadData();
    }

    // setup the ui components
    private void initComponents() {
        // window title and size
        setTitle("Assign Academic Supervisor");
        setSize(1000, 600);
        // center it
        setLocationRelativeTo(null);
        // close just this window
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // panel for the two tables side by side
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        // add some padding
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // left side: students who have a job but no supervisor yet
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("Placed Students (No SV)"));
        // columns for student table
        placedModel = new DefaultTableModel(new String[]{"MatchID", "Student", "Company"}, 0);
        placedTable = new JTable(placedModel);
        // add scrolling
        p1.add(new JScrollPane(placedTable));

        // right side: list of available lecturers
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("Academic Supervisors"));
        svModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        svTable = new JTable(svModel);
        p2.add(new JScrollPane(svTable), BorderLayout.CENTER);
        
        // add both panels to the center
        centerPanel.add(p1); 
        centerPanel.add(p2);
        add(centerPanel, BorderLayout.CENTER);

        // bottom button area
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        // button to save the match
        JButton btnAssign = new JButton("Assign Supervisor");
        btnAssign.setPreferredSize(new Dimension(150, 40));
        btnAssign.addActionListener(e -> assign()); // call assign function
        
        // back button
        JButton btnBack = new JButton("Back Home");
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminDashboardUI().setVisible(true); // go back to main dashboard
        });

        // add buttons to footer
        footer.add(btnAssign);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    // function to get data from db
    private void loadData() {
        // clear existing rows
        placedModel.setRowCount(0); svModel.setRowCount(0);
        // get matches that need a supervisor and add to left table
        for(String[] m : DatabaseHelper.getMatchesMissingSupervisor()) placedModel.addRow(new Object[]{m[0], m[2], m[4]}); 
        // get all academic supervisors and add to right table
        for(String[] u : DatabaseHelper.getUsersByRole("Academic Supervisor", "")) svModel.addRow(new Object[]{u[0], u[2]}); 
    }

    // logic to link them together
    private void assign() {
        // get selected row index
        int r1 = placedTable.getSelectedRow();
        int r2 = svTable.getSelectedRow();
        
        // check if user actually selected something
        if(r1==-1 || r2==-1) { JOptionPane.showMessageDialog(this, "Select a Student and a Supervisor."); return; }

        // get the match id and supervisor id from the table
        String mid = placedModel.getValueAt(r1, 0).toString();
        String aid = svModel.getValueAt(r2, 0).toString(); // Gets ID from Col 0

        // update the database
        DatabaseHelper.assignAcademicSupervisor(mid, aid); 
        // show success
        JOptionPane.showMessageDialog(this, "Assigned Successfully!");
        // refresh list so the assigned student disappears
        loadData();
    }
}