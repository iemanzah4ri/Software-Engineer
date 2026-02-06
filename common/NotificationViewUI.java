//displays list of notifications for the logged-in user
//allows marking messages as read or unread
package common;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class NotificationViewUI extends JFrame {
    private String userId;
    private JTable table;
    private DefaultTableModel model;
    
    public NotificationViewUI(String userId) {
        this.userId = userId;
        initComponents();
        loadNotifications();
    }

    private void initComponents() {
        setTitle("My Notifications");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Notifications", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        String[] cols = {"ID", "Date", "Message", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
        
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(500);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnRead = new JButton("Mark as Read");
        JButton btnUnread = new JButton("Mark as Unread");
        JButton btnAll = new JButton("Mark All Read");
        JButton btnBack = new JButton("Back");

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

    private void loadNotifications() {
        model.setRowCount(0);
        List<String[]> notifs = NotificationHelper.getNotifications(userId); // UPDATED
        
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

    private void markSelectedAsRead() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification.");
            return;
        }

        String notifId = model.getValueAt(row, 0).toString();
        if (notifId.isEmpty()) return;

        NotificationHelper.markAsRead(notifId); // UPDATED
        loadNotifications();
    }

    private void markSelectedAsUnread() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a notification.");
            return;
        }

        String notifId = model.getValueAt(row, 0).toString();
        if (notifId.isEmpty()) return;

        NotificationHelper.markAsUnread(notifId); // UPDATED
        loadNotifications();
    }

    private void markAllAsRead() {
        NotificationHelper.markAllAsRead(userId); // UPDATED
        loadNotifications();
        JOptionPane.showMessageDialog(this, "All marked as read.");
    }
}