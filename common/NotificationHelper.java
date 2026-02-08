package common;
// imports
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// handles logic for sending and reading notifications
public class NotificationHelper {
    // path to file
    private static final String NOTIFICATION_FILE = "database" + File.separator + "notifications.txt";

    // ensure folder exists
    static {
        new File("database").mkdirs();
    }

    // send to one person
    public static void sendNotification(String targetId, String message) {
        saveToFile(targetId, message);
    }

    // send to a whole group
    public static void broadcastNotification(String roleTarget, String message) {
        saveToFile(roleTarget, message);
    }

    // actually writes to the file
    private static void saveToFile(String target, String msg) {
        long id = System.currentTimeMillis(); 
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // replacing commas so csv doesnt break
        String line = id + "," + target + "," + msg.replace(",", ";") + "," + date + ",Unread";
        
        try (FileWriter fw = new FileWriter(NOTIFICATION_FILE, true); PrintWriter out = new PrintWriter(fw)) {
            out.println(line);
        } catch (IOException e) {}
    }

    // get list of messages for a user
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

                // check if message is for me specifically or my role
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
        
        // newest first
        Collections.reverse(list); 
        return list;
    }
    
    // check if i have red dot
    public static boolean hasUnreadNotifications(String userId) {
        List<String[]> notifications = getNotifications(userId);
        for (String[] notif : notifications) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                return true;
            }
        }
        return false;
    }
    
    // update status to read
    public static void markAsRead(String notificationId) {
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return;
        
        List<String> lines = new ArrayList<>();
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                // find id and change status
                if (d.length >= 5 && d[0].equals(notificationId)) {
                    d[4] = "Read";
                    lines.add(String.join(",", d));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {}
        
        // save back
        if (updated) {
            try (PrintWriter out = new PrintWriter(new FileWriter(f))) {
                for (String l : lines) out.println(l);
            } catch (IOException e) {}
        }
    }

    // update status to unread
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

    // clear all notifications
    public static void markAllAsRead(String userId) {
        File f = new File(NOTIFICATION_FILE);
        if (!f.exists()) return;
        
        List<String[]> userNotifs = getNotifications(userId);
        List<String> notifIds = new ArrayList<>();
        
        // collect ids first
        for (String[] notif : userNotifs) {
            if (notif.length >= 5 && notif[4].equalsIgnoreCase("Unread")) {
                notifIds.add(notif[0]);
            }
        }
        
        // loop and mark read
        for (String id : notifIds) {
            markAsRead(id);
        }
    }

    // helper to determine role from ID prefix
    private static String getUserRole(String id) {
        if (id.equals("admin")) return "Admin";
        if (id.startsWith("S-")) return "Student";
        if (id.startsWith("C-")) return "Company Supervisor";
        if (id.startsWith("A-")) return "Academic Supervisor";
        return "Unknown";
    }
}