import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DBHelper {
    private static final String DB_FOLDER = "database";
    private static final String FILE_NAME = DB_FOLDER + File.separator + "users.txt";
    private static final String LISTING_FILE = DB_FOLDER + File.separator + "listings.txt";
    private static final String APP_FILE = DB_FOLDER + File.separator + "applications.txt";
    private static final String LOG_FILE = DB_FOLDER + File.separator + "logbooks.txt";
    private static final String MATCH_FILE = DB_FOLDER + File.separator + "matches.txt";
    private static final String ATTENDANCE_FILE = DB_FOLDER + File.separator + "attendance.txt";
    private static final String FEEDBACK_FILE = DB_FOLDER + File.separator + "feedback.txt";

    static {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    // ==========================================
    // SECTION 1: USER MANAGEMENT
    // ==========================================

    public static void saveUser(String username, String password, String fullname, String intake, String role) {
        saveUserFull(username, password, fullname, intake, role, "N/A", "N/A", "N/A", "Not Placed");
    }

    public static void saveCompanySupervisor(String username, String password, String fullname, String position, String company, String email) {
        saveUserFull(username, password, fullname, position, "Company Supervisor", email, "N/A", company, "N/A");
    }

    private static void saveUserFull(String username, String password, String fullname, String col4, String role, 
                                     String col6, String col7, String col8, String col9) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long id = System.currentTimeMillis() % 100000;
            String safeUser = username.replace(",", "");
            String safePass = password.replace(",", "");
            String safeName = fullname.replace(",", "");
            
            out.println(id + "," + safeUser + "," + safePass + "," + safeName + "," + 
                       (col4==null?"N/A":col4) + "," + role + "," + 
                       (col6==null?"N/A":col6) + "," + (col7==null?"N/A":col7) + "," + 
                       (col8==null?"N/A":col8) + "," + (col9==null?"N/A":col9));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String[]> getUsersByRole(String role, String query) {
        List<String[]> results = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return results;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) { 
                    if (data[5].equalsIgnoreCase(role)) {
                        if (query.isEmpty() || data[3].toLowerCase().contains(query.toLowerCase()) || data[0].equals(query)) {
                            results.add(new String[]{data[0], data[1], data[3], (data.length > 8 ? data[8] : "N/A")});
                        }
                    }
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return results;
    }

    public static String[] getUserById(String id) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(id)) {
                    String[] fullData = new String[10];
                    for(int i=0; i<10; i++) fullData[i] = (i < data.length) ? data[i] : "N/A";
                    return fullData;
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }

    public static List<String[]> getAvailableStudents() {
        List<String[]> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Check if Role is Student AND Status (Index 9) is NOT "Placed"
                if (data.length >= 10 && data[5].equalsIgnoreCase("Student") && !data[9].equalsIgnoreCase("Placed")) {
                    list.add(new String[]{data[0], data[3], data[4]}); // ID, Name, Intake
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    // --- Specific Update Wrappers for UI ---
    
    public static void updateStudent(String id, String user, String pass, String name, String matric) {
        // Preserves other fields by reading them first or setting defaults
        String[] current = getUserById(id);
        String email="N/A", contact="N/A", addr="N/A", place="Not Placed";
        if(current != null) { email=current[6]; contact=current[7]; addr=current[8]; place=current[9]; }
        updateUser(id, user, pass, name, matric, "Student", email, contact, addr, place);
    }

    public static void updateCompanySupervisor(String id, String user, String pass, String name, String pos, String comp, String email) {
        updateUser(id, user, pass, name, pos, "Company Supervisor", email, "N/A", comp, "N/A");
    }
    
    public static void updateAcademicSupervisor(String id, String user, String pass, String name) {
        updateUser(id, user, pass, name, "N/A", "Academic Supervisor", "N/A", "N/A", "N/A", "N/A");
    }

    // Generic Update
    public static void updateUser(String id, String username, String password, String fullname, String intake, String role, String email, String contact, String address, String placement) {
        File file = new File(FILE_NAME);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) { return; }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length > 0 && data[0].equals(id)) {
                    out.println(id + "," + username + "," + password + "," + fullname + "," + intake + "," + role + "," 
                              + email + "," + contact + "," + address + "," + placement);
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
    
    public static void updateStudentPlacement(String studentId, String newStatus) {
        String[] s = getUserById(studentId);
        if(s!=null) updateUser(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], newStatus);
    }

    // ==========================================
    // SECTION 2: LISTING MANAGEMENT
    // ==========================================

    public static void saveListing(String regNumber, String company, String location, String job, String status) {
        try (FileWriter fw = new FileWriter(LISTING_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(regNumber + "," + company + "," + location + "," + job + "," + status);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String[]> getAllListings() {
        List<String[]> list = new ArrayList<>();
        File file = new File(LISTING_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5) list.add(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public static void approveListing(String regNumber) { updateListingStatus(regNumber, "Approved"); }
    public static void rejectListing(String regNumber) { updateListingStatus(regNumber, "Rejected"); }

    private static void updateListingStatus(String regNumber, String newStatus) {
        File file = new File(LISTING_FILE);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) { return; }
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length >= 5 && data[0].equals(regNumber)) {
                    out.println(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + newStatus);
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    // ==========================================
    // SECTION 3: APPLICATION MANAGEMENT
    // ==========================================

    public static void applyForInternship(String studentId, String regNumber, String companyName) {
        try (FileWriter fw = new FileWriter(APP_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long appId = System.currentTimeMillis() % 100000;
            out.println(appId + "," + studentId + "," + regNumber + "," + companyName + ",Pending");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static boolean hasApplied(String studentId, String regNumber) {
        File file = new File(APP_FILE);
        if (!file.exists()) return false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 3 && data[1].equals(studentId) && data[2].equals(regNumber)) return true;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return false;
    }

    public static List<String[]> getApplicationsByStudent(String studentId) {
        List<String[]> list = new ArrayList<>();
        File file = new File(APP_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5 && data[1].equals(studentId)) list.add(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================
    // SECTION 4: LOGBOOK & ATTENDANCE
    // ==========================================

    public static void saveLogbookEntry(String studentId, String date, String activity, String hours) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long logId = System.currentTimeMillis() % 100000;
            out.println(logId + "," + studentId + "," + date + "," + activity.replace(",", " ") + "," + hours + ",Pending");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String[]> getAllLogbooks() {
        List<String[]> list = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) list.add(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public static List<String[]> getLogbooksByStudent(String studentId) {
        List<String[]> list = new ArrayList<>();
        File file = new File(LOG_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[1].equals(studentId)) list.add(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public static void updateLogbookStatus(String logId, String newStatus) {
        File file = new File(LOG_FILE);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) { return; }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].equals(logId)) {
                    out.println(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + newStatus);
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void updateLogbookEntry(String logId, String date, String activity, String hours) {
        File file = new File(LOG_FILE);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) { return; }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[0].equals(logId)) {
                    out.println(data[0] + "," + data[1] + "," + date + "," + activity.replace(",", " ") + "," + hours + "," + data[5]);
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void saveAttendance(String studentId, String studentName, String date, String timeIn, String timeOut) {
        List<String> lines = new ArrayList<>();
        boolean updated = false;
        File file = new File(ATTENDANCE_FILE);
        if(file.exists()){
            try(BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                while((line = br.readLine()) != null){
                    String[] data = line.split(",");
                    if(data.length >= 7 && data[1].equals(studentId) && data[3].equals(date)){
                        String newOut = (timeOut == null || timeOut.isEmpty()) ? data[5] : timeOut;
                        String status = (newOut.equals("Pending")) ? "Pending" : "Completed";
                        lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + newOut + "," + status);
                        updated = true;
                    } else lines.add(line);
                }
            } catch(IOException e){ e.printStackTrace(); }
        }
        if(!updated && timeIn != null){
            long id = System.currentTimeMillis() % 100000;
            lines.add(id + "," + studentId + "," + studentName + "," + date + "," + timeIn + ",Pending,Pending");
        }
        try(PrintWriter out = new PrintWriter(new FileWriter(file))){
            for(String l : lines) out.println(l);
        } catch(IOException e){ e.printStackTrace(); }
    }

    public static List<String[]> getStudentAttendance(String studentId) {
        List<String[]> list = new ArrayList<>();
        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7 && data[1].equals(studentId)) list.add(data);
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    // ==========================================
    // SECTION 5: MATCHING & FEEDBACK
    // ==========================================

    public static void saveMatch(String studentId, String studentName, String regNo, String companyName, String position) {
        try (FileWriter fw = new FileWriter(MATCH_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long matchId = System.currentTimeMillis() % 100000;
            out.println(matchId + "," + studentId + "," + studentName + "," + regNo + "," + companyName + "," + position + "," + LocalDate.now());
        } catch (IOException e) { e.printStackTrace(); }
        updateStudentPlacement(studentId, "Placed");
    }

    public static String[] getStudentFeedback(String studentId) {
        File file = new File(FEEDBACK_FILE);
        if (!file.exists()) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(studentId)) return data;
            }
        } catch (IOException e) { e.printStackTrace(); }
        return null;
    }
}