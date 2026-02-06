//interface to send mass notifications
//targets specific groups or all users
package admin;
import common.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminBroadcastUI extends JFrame {

    private JList<String> userList;
    private DefaultListModel<String> listModel;
    private JComboBox<String> cmbTarget;
    private JScrollPane listScrollPane;

    public AdminBroadcastUI() {
        initComponents();
        loadUserList();
    }

    private void initComponents() {
        setTitle("Broadcast Notification");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("Create System Notification", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblTarget = new JLabel("Target Audience:");
        String[] targets = {"All Students", "All Company Supervisors", "All Academic Supervisors", "EVERYONE", "Specific Users (Select Below)"};
        cmbTarget = new JComboBox<>(targets);

        JLabel lblMsg = new JLabel("Message:");
        JTextArea txtMsg = new JTextArea(4, 20);
        txtMsg.setLineWrap(true);
        txtMsg.setWrapStyleWord(true);
        JScrollPane msgScroll = new JScrollPane(txtMsg);

        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        userList.setVisibleRowCount(6);
        listScrollPane = new JScrollPane(userList);
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Select Specific Users (Hold Ctrl/Cmd to pick multiple)"));
        listScrollPane.setVisible(false); 

        cmbTarget.addActionListener(e -> {
            String selected = (String) cmbTarget.getSelectedItem();
            if ("Specific Users (Select Below)".equals(selected)) {
                listScrollPane.setVisible(true);
                pack(); 
            } else {
                listScrollPane.setVisible(false);
                pack(); 
            }
        });

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(lblTarget, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(cmbTarget, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        form.add(lblMsg, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weighty = 0.2; 
        gbc.fill = GridBagConstraints.BOTH;
        form.add(msgScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.weighty = 0.5; 
        form.add(listScrollPane, gbc);

        add(form, BorderLayout.CENTER);

        JButton btnSend = new JButton("Broadcast Message");
        btnSend.setBackground(new Color(100, 149, 237));
        btnSend.setForeground(Color.WHITE);
        btnSend.setFont(new Font("Arial", Font.BOLD, 14));
        btnSend.addActionListener(e -> {
            String msg = txtMsg.getText().trim();
            if(msg.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a message.");
                return;
            }

            String selected = (String) cmbTarget.getSelectedItem();
            
            if ("Specific Users (Select Below)".equals(selected)) {
                List<String> selectedValues = userList.getSelectedValuesList();
                if (selectedValues.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please select at least one user from the list.");
                    return;
                }
                for (String entry : selectedValues) {
                    String targetId = entry.split(" - ")[0]; 
                    NotificationHelper.sendNotification(targetId, msg);
                }
            } else {
                String targetCode = "";
                if(selected.equals("All Students")) targetCode = "ROLE_STUDENT";
                else if(selected.equals("All Company Supervisors")) targetCode = "ROLE_COMPANY";
                else if(selected.equals("All Academic Supervisors")) targetCode = "ROLE_ACADEMIC";
                else if(selected.equals("EVERYONE")) targetCode = "ALL";
                
                NotificationHelper.broadcastNotification(targetCode, msg);
            }

            JOptionPane.showMessageDialog(this, "Notification Sent Successfully!");
            dispose();
        });
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.add(btnSend);
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadUserList() {
        listModel.clear();
        
        List<String[]> students = DatabaseHelper.getUsersByRole("Student", ""); // UPDATED
        for(String[] s : students) listModel.addElement(s[0] + " - " + s[2] + " (Student)");

        List<String[]> compSvs = DatabaseHelper.getUsersByRole("Company Supervisor", ""); // UPDATED
        for(String[] c : compSvs) listModel.addElement(c[0] + " - " + c[2] + " (Company SV)");

        List<String[]> acadSvs = DatabaseHelper.getUsersByRole("Academic Supervisor", ""); // UPDATED
        for(String[] a : acadSvs) listModel.addElement(a[0] + " - " + a[2] + " (Academic SV)");
    }
}