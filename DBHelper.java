import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBHelper {
    private static final String DB_FOLDER = "database";
    private static final String RESUME_FOLDER = "resume";
    private static final String FILE_NAME = DB_FOLDER + File.separator + "users.txt";
    private static final String LISTING_FILE = DB_FOLDER + File.separator + "listings.txt";
    private static final String APP_FILE = DB_FOLDER + File.separator + "applications.txt";
    private static final String LOG_FILE = DB_FOLDER + File.separator + "logbooks.txt";
    private static final String MATCH_FILE = DB_FOLDER + File.separator + "matches.txt";
    private static final String ATTENDANCE_FILE = DB_FOLDER + File.separator + "attendance.txt";
    private static final String FEEDBACK_FILE = DB_FOLDER + File.separator + "feedback.txt";

    static {
        File folder = new File(DB_FOLDER);
        if (!folder.exists()) folder.mkdir();
        
        File resFolder = new File(RESUME_FOLDER);
        if (!resFolder.exists()) resFolder.mkdir();
    }

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
                if (data.length >= 10 && data[5].equalsIgnoreCase("Student") && !data[9].equalsIgnoreCase("Placed")) {
                    list.add(new String[]{data[0], data[3], data[4]}); 
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

    public static void updateStudent(String id, String user, String pass, String name, String matric) {
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

    public static void saveListing(String regNumber, String company, String location, String jobName, String jobDesc, String status) {
        try (FileWriter fw = new FileWriter(LISTING_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            String safeDesc = jobDesc.replace("\n", " ").replace(",", ";"); 
            String safeName = jobName.replace(",", " ");
            out.println(regNumber + "," + company + "," + location + "," + safeName + "," + safeDesc + "," + status);
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static List<String[]> getAllListings() {
        List<String[]> list = new ArrayList<>();
        File file = new File(LISTING_FILE);
        if (!file.exists()) return list;

        Set<String> filledRegNos = new HashSet<>();
        File matchFile = new File(MATCH_FILE);
        if (matchFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(matchFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 4) filledRegNos.add(data[3]); 
                }
            } catch (IOException e) { e.printStackTrace(); }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6) {
                    if (filledRegNos.contains(data[0])) {
                        data[5] = "Filled"; 
                    }
                    list.add(data);
                }
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
                if (data.length >= 6 && data[0].equals(regNumber)) {
                    out.println(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + "," + data[4] + "," + newStatus);
                } else {
                    out.println(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void applyForInternship(String studentId, String regNumber, String companyName) {
        try (FileWriter fw = new FileWriter(APP_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long appId = System.currentTimeMillis() % 100000;
            out.println(appId + "," + studentId + "," + regNumber + "," + companyName + ",Pending");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public static void approveApplication(String appId) {
        File file = new File(APP_FILE);
        List<String> lines = new ArrayList<>();
        String studentId = "", regNo = "", companyName = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 5 && data[0].equals(appId)) {
                    studentId = data[1];
                    regNo = data[2];
                    companyName = data[3];
                    lines.add(data[0] + "," + data[1] + "," + data[2] + "," + data[3] + ",Approved");
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); return; }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String l : lines) out.println(l);
        } catch (IOException e) { e.printStackTrace(); }

        if (!studentId.isEmpty()) {
            String[] student = getUserById(studentId);
            List<String[]> listings = getAllListings();
            String position = "Intern";
            for (String[] l : listings) {
                if (l[0].equals(regNo)) {
                    position = l[3]; 
                    break;
                }
            }
            if (student != null) {
                saveMatch(studentId, student[3], regNo, companyName, position);
            }
        }
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

    public static boolean saveResume(File sourceFile, String studentId) {
        String ext = "";
        int i = sourceFile.getName().lastIndexOf('.');
        if (i > 0) {
            ext = sourceFile.getName().substring(i);
        }
        
        File destFile = new File(RESUME_FOLDER + File.separator + studentId + ext);
        
        try {
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File getResumeFile(String studentId) {
        File folder = new File(RESUME_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.startsWith(studentId + "."));
        if (files != null && files.length > 0) {
            return files[0];
        }
        return null;
    }

    public static void saveMatch(String studentId, String studentName, String regNo, String companyName, String position) {
        try (FileWriter fw = new FileWriter(MATCH_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            long matchId = System.currentTimeMillis() % 100000;
            out.println(matchId + "," + studentId + "," + studentName + "," + regNo + "," + companyName + "," + position + "," + LocalDate.now());
        } catch (IOException e) { e.printStackTrace(); }
        updateStudentPlacement(studentId, "Placed");
    }

    public static List<String[]> getMatchesForSupervisor(String supervisorId) {
        List<String[]> list = new ArrayList<>();
        String[] supervisor = getUserById(supervisorId);
        if (supervisor == null || supervisor.length <= 8) return list;
        
        String myCompany = supervisor[8];
        File file = new File(MATCH_FILE);
        if (!file.exists()) return list;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 6 && data[4].equalsIgnoreCase(myCompany)) {
                    list.add(data);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
        return list;
    }

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
                if (data.length >= 6) {
                    if (studentId == null || data[1].equals(studentId)) {
                        list.add(data);
                    }
                }
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


    public static void saveCompanyFeedback(String studentId, String studentName,
                                       String companyName, String score, String feedback) {
    File file = new File(FEEDBACK_FILE);
    List<String> lines = new ArrayList<>();
    boolean updated = false;

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(studentId)) {
                    // update company feedback
                    data[3] = companyName;
                    data[5] = score;
                    data[7] = feedback.replace(",", " ");
                    lines.add(String.join(",", data));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    if (!updated) {
        long id = System.currentTimeMillis() % 100000;
        lines.add(id + "," + studentId + "," + studentName + "," + companyName + ",Completed," +
                  score + ",N/A," + feedback.replace(",", " ") + ",N/A");
    }

    try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
        for (String l : lines) out.println(l);
    } catch (IOException e) { e.printStackTrace(); }
}

public static void saveAcademicFeedback(String studentId, String studentName,
                                        String score, String feedback) {
    File file = new File(FEEDBACK_FILE);
    List<String> lines = new ArrayList<>();
    boolean updated = false;

    if (file.exists()) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 9 && data[1].equals(studentId)) {
                    // update academic feedback
                    data[6] = score;
                    data[8] = feedback.replace(",", " ");
                    lines.add(String.join(",", data));
                    updated = true;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    if (!updated) {
        long id = System.currentTimeMillis() % 100000;
        lines.add(id + "," + studentId + "," + studentName + ",N/A,Completed,N/A," +
                  score + ",N/A," + feedback.replace(",", " "));
    }

    try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
        for (String l : lines) out.println(l);
    } catch (IOException e) { e.printStackTrace(); }
}

}

