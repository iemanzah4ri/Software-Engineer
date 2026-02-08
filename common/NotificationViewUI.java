package common;
// imports
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// window to see my messages
public class NotificationViewUI extends JFrame {
    private String userId;
    private JTable table;
    private DefaultTableModel model;
    
    // constructor
    public NotificationViewUI(String userId) {
        this.userId = userId;
        initComponents();
        loadNotifications();
    }

    // setup gui components
    private void initComponents() {
        setTitle("My Notifications");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header
        JLabel titleLabel = new JLabel("Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // table setup
        String[] cols = {"ID", "Date", "Message", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        
        // hide the ID column cause its ugly
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        // set sizes
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // buttons
        JPanel btnPanel = new JPanel();
        JButton btnRead = new JButton("Mark as Read");
        JButton btnUnread = new JButton("Mark as Unread");
        JButton btnAll = new JButton("Mark All Read");
        JButton btnBack = new JButton("Back");

        // listeners
        btnRead.addActionListener(e -> markSelectedAsRead());
        btnUnread.addActionListener(e -> markSelectedAsUnread());
        btnAll.addActionListener(e -> markAllAsRead());
        btnBack.addActionListener(e -> dispose());
        
        btnPanel.add(btnRead);
        btnPanel.add(btnUnread);
        btnPanel.add(btnAll);
        btnPanel.add(btnBack);
        
        add(btnPanel, BorderLayout.SOUTH);
    }

    // fill the table
    private void loadNotifications() {
        model.setRowCount(0);
        List<String[]> notifs = NotificationHelper.getNotifications(userId); 
        
        if (notifs.isEmpty()) {
            model.addRow(new Object[]{"", "", "No notifications.", ""});
        } else {
            for (String[] n : notifs) {
                if (n.length >= 5) {
                    model.addRow(new Object[]{n[0], n[3], n[2], n[4]});
                }
            }
        }
    }

    // update status for selected row
    private void markSelectedAsRead() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification.");
            return;
        }

        String notifId = model.getValueAt(row, 0).toString();
        if (notifId.isEmpty()) return;

        NotificationHelper.markAsRead(notifId); 
        loadNotifications();
    }

    // update status back to unread
    private void markSelectedAsUnread() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification.");
            return;
        }

        String notifId = model.getValueAt(row, 0).toString();
        if (notifId.isEmpty()) return;

        NotificationHelper.markAsUnread(notifId); 
        loadNotifications();
    }

    // clear all
    private void markAllAsRead() {
        NotificationHelper.markAllAsRead(userId); 
        loadNotifications();
        JOptionPane.showMessageDialog(this, "All marked as read.");
    }
}