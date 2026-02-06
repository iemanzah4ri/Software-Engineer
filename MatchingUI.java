import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MatchingUI extends JFrame {
    private JTable placedTable, svTable;
    private DefaultTableModel placedModel, svModel;

    public MatchingUI() {
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("Assign Academic Supervisor");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("Placed Students (No SV)"));
        placedModel = new DefaultTableModel(new String[]{"MatchID", "Student", "Company"}, 0);
        placedTable = new JTable(placedModel);
        p1.add(new JScrollPane(placedTable));

        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("Academic Supervisors"));
        svModel = new DefaultTableModel(new String[]{"ID", "Name"}, 0);
        svTable = new JTable(svModel);
        p2.add(new JScrollPane(svTable), BorderLayout.CENTER);
        
        centerPanel.add(p1); 
        centerPanel.add(p2);
        add(centerPanel, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton btnAssign = new JButton("Assign Supervisor");
        btnAssign.setPreferredSize(new Dimension(150, 40));
        btnAssign.addActionListener(e -> assign());
        
        JButton btnBack = new JButton("Back Home");
        btnBack.setPreferredSize(new Dimension(150, 40));
        btnBack.addActionListener(e -> {
            this.dispose();
            new AdminHome().setVisible(true);
        });

        footer.add(btnAssign);
        footer.add(btnBack);
        add(footer, BorderLayout.SOUTH);
    }

    private void loadData() {
        placedModel.setRowCount(0); svModel.setRowCount(0);
        for(String[] m : DBHelper.getMatchesMissingSupervisor()) placedModel.addRow(new Object[]{m[0], m[2], m[4]});
        for(String[] u : DBHelper.getUsersByRole("Academic Supervisor", "")) svModel.addRow(new Object[]{u[0], u[2]});
    }

    private void assign() {
        int r1 = placedTable.getSelectedRow();
        int r2 = svTable.getSelectedRow();
        if(r1==-1 || r2==-1) { JOptionPane.showMessageDialog(this, "Select a Student and a Supervisor."); return; }

        String mid = placedModel.getValueAt(r1, 0).toString();
        String aid = svModel.getValueAt(r2, 0).toString(); // Gets ID from Col 0

        DBHelper.assignAcademicSupervisor(mid, aid);
        JOptionPane.showMessageDialog(this, "Assigned Successfully!");
        loadData();
    }
}