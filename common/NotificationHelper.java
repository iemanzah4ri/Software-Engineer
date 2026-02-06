//manages creation and retrieval of system notifications
//handles broadcasting messages to specific user roles
//saves notification data to text file
package common;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



public class NotificationHelper {
    private static final String NOTIFICATION_FILE = "database" + File.separator + "notifications.txt";

    static {
        new File("database").mkdirs();
    }

    public static void sendNotification(String targetId, String message) {
        saveToFile(targetId, message);
    }

    public static void broadcastNotification(String roleTarget, String message) {
        saveToFile(roleTarget, message);
    }

    private static void saveToFile(String target, String msg) {
        long id = System.currentTimeMillis(); 
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String line = id + "," + target + "," + msg.replace(",", ";") + "," + date + ",Unread";
        
        try (FileWriter fw = new FileWriter(NOTIFICATION_FILE, true); PrintWriter out = new PrintWriter(fw)) {
            out.println(line);
        } catch (IOException e) {}
    }

    public static List<String[]> getNotifications(String userId) {
        List<String[]> list = new ArrayList<>();
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return list;

        String userRole = getUserRole(userId);

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length < 5) continue;

                String target = d[1];
                boolean isMatch = false;

                if (target.equals(userId)) isMatch = true;
                else if (target.equals("ALL")) isMatch = true;
                else if (target.equals("ROLE_STUDENT") && userRole.equals("Student")) isMatch = true;
                else if (target.equals("ROLE_COMPANY") && userRole.equals("Company Supervisor")) isMatch = true;
                else if (target.equals("ROLE_ACADEMIC") && userRole.equals("Academic Supervisor")) isMatch = true;
                else if (target.equals("ROLE_ADMIN") && userId.equals("admin")) isMatch = true;

                if (isMatch) {
                    list.add(d);
                }
            }
        } catch (IOException e) {}
        
        Collections.reverse(list); 
        return list;
    }
    
    public static boolean hasUnreadNotifications(String userId) {
        List<String[]> notifications = getNotifications(userId);
        for (String[] notif : notifications) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                return true;
            }
        }
        return false;
    }
    
    public static void markAsRead(String notificationId) {
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return;
        
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 5 && d[0].equals(notificationId)) {
                    d[4] = "Read";
                    lines.add(String.join(",", d));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {}
        
        if (updated) {
            try (PrintWriter out = new PrintWriter(new FileWriter(f))) {
                for (String l : lines) out.println(l);
            } catch (IOException e) {}
        }
    }

    public static void markAsUnread(String notificationId) {
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return;
        
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 5 && d[0].equals(notificationId)) {
                    d[4] = "Unread";
                    lines.add(String.join(",", d));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {}
        
        if (updated) {
            try (PrintWriter out = new PrintWriter(new FileWriter(f))) {
                for (String l : lines) out.println(l);
            } catch (IOException e) {}
        }
    }

    public static void markAllAsRead(String userId) {
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return;
        
        List<String[]> userNotifs = getNotifications(userId);
        List<String> notifIds = new ArrayList<>();
        
        for (String[] notif : userNotifs) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                notifIds.add(notif[0]);
            }
        }
        
        for (String id : notifIds) {
            markAsRead(id);
        }
    }

    private static String getUserRole(String id) {
        if (id.equals("admin")) return "Admin";
        if (id.startsWith("S-")) return "Student";
        if (id.startsWith("C-")) return "Company Supervisor";
        if (id.startsWith("A-")) return "Academic Supervisor";
        return "Unknown";
    }
}