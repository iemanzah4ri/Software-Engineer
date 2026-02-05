import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyManageApplicationsUI extends JFrame {

    private String companyName;
    private JTable appTable;
    private DefaultTableModel appModel;

    public CompanyManageApplicationsUI(String companyName) {
        this.companyName = (companyName == null) ? "" : companyName.trim();
        initComponents();
        loadApplications();
    }

    private void initComponents() {
        setTitle("Manage Student Applications - " + (companyName.isEmpty() ? "All Companies" : companyName));
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Pending Student Requests", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setBorder(new EmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = {"App ID", "Student ID", "Student Name", "Listing RegNo", "Status"};
        appModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        appTable = new JTable(appModel);
        add(new JScrollPane(appTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnBack = new JButton("Back");
        JButton btnReject = new JButton("Reject");
        JButton btnAccept = new JButton("Accept Student");

        btnBack.addActionListener(e -> dispose());
        btnReject.setBackground(new Color(255, 102, 102));
        btnAccept.setBackground(new Color(144, 238, 144));

        btnAccept.addActionListener(e -> handleAction(true));
        btnReject.addActionListener(e -> handleAction(false));

        bottomPanel.add(btnBack);
        bottomPanel.add(btnReject);
        bottomPanel.add(btnAccept);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadApplications() {
        appModel.setRowCount(0);
        File file = new File("database/applications.txt");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) {
                    String targetCompany = data[3].trim();
                    String status = data[4].trim();

                    if (targetCompany.equalsIgnoreCase(companyName) && status.equalsIgnoreCase("Pending")) {
                        String[] student = DBHelper.getUserById(data[1].trim());
                        String studentName = (student != null) ? student[3] : "Unknown Student";
                        appModel.addRow(new Object[]{data[0], data[1], studentName, data[2], status});
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void handleAction(boolean approve) {
        int row = appTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an application first.");
            return;
        }

        String appId = appModel.getValueAt(row, 0).toString();
        if (approve) {
            DBHelper.approveApplication(appId);
            JOptionPane.showMessageDialog(this, "Application Approved and Student Placed!");
        } else {
            updateApplicationStatus(appId, "Rejected");
            JOptionPane.showMessageDialog(this, "Application Rejected.");
        }
        loadApplications();
    }

    private void updateApplicationStatus(String appId, String status) {
        File file = new File("database/applications.txt");
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(appId)) {
                    lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + status);
                } else { lines.add(line); }
            }
        } catch (IOException e) { e.printStackTrace(); }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String l : lines) out.println(l);
        } catch (IOException e) { e.printStackTrace(); }
    }
}