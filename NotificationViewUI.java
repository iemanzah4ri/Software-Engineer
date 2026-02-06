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
        table.setRowHeight(25);
        
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnMarkRead = new JButton("Mark as Read");
        btnMarkRead.setBackground(new Color(144, 238, 144));
        btnMarkRead.addActionListener(e -> markSelectedAsRead());

        JButton btnMarkUnread = new JButton("Mark as Unread");
        btnMarkUnread.setBackground(new Color(255, 200, 100));
        btnMarkUnread.addActionListener(e -> markSelectedAsUnread());

        JButton btnMarkAllRead = new JButton("Mark All as Read");
        btnMarkAllRead.setBackground(new Color(200, 200, 255));
        btnMarkAllRead.addActionListener(e -> markAllAsRead());

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadNotifications());

        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(e -> dispose());

        buttonPanel.add(btnMarkRead);
        buttonPanel.add(btnMarkUnread);
        buttonPanel.add(btnMarkAllRead);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnBack);

        add(buttonPanel, BorderLayout.SOUTH);
    }

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

    private void markAllAsRead() {
        NotificationHelper.markAllAsRead(userId);
        loadNotifications();
        JOptionPane.showMessageDialog(this, "All notifications marked as read.");
    }
}
