//handles all text-based database operations
//manages reading and writing to csv files
//contains static methods for data retrieval and updates
package common;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_FOLDER = "database";
    private static final String RESUME_FOLDER = DB_FOLDER + File.separator + "resume";
    private static final String PFP_FOLDER = DB_FOLDER + File.separator + "pfp";
    private static final String CONTRACT_FOLDER = DB_FOLDER + File.separator + "contracts";
    
    private static final String FILE_NAME = DB_FOLDER + File.separator + "users.txt";
    private static final String LISTING_FILE = DB_FOLDER + File.separator + "listings.txt";
    private static final String APP_FILE = DB_FOLDER + File.separator + "applications.txt";
    private static final String LOG_FILE = DB_FOLDER + File.separator + "logbooks.txt";
    private static final String MATCH_FILE = DB_FOLDER + File.separator + "matches.txt";
    private static final String ATTENDANCE_FILE = DB_FOLDER + File.separator + "attendance.txt";
    
    private static final String COMPANY_FEEDBACK_FILE = DB_FOLDER + File.separator + "company_feedback.txt";
    private static final String ACADEMIC_FEEDBACK_FILE = DB_FOLDER + File.separator + "academic_feedback.txt";

    static {
        new File(DB_FOLDER).mkdirs();
        new File(RESUME_FOLDER).mkdirs();
        new File(PFP_FOLDER).mkdirs();
        new File(CONTRACT_FOLDER).mkdirs();
    }

    private static long generateNextId(String filePath, int idIndex) {
        long maxId = 0;
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length > idIndex) {
                        try {
                            String numPart = data[idIndex].replaceAll("\\D+", "");
                            long currentId = Long.parseLong(numPart);
                            if (currentId > maxId) maxId = currentId;
                        } catch (NumberFormatException e) {}
                    }
                }
            } catch (IOException e) {}
        }
        return maxId + 1;
    }

    private static List<String> readFile(String path) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) {}
        return lines;
    }

    private static void writeFile(String path, List<String> lines) {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            for (String line : lines) out.println(line);
        } catch (IOException e) {}
    }

    public static void saveUser(String user, String pass, String name, String intake, String role) {
        saveUserFull(user, pass, name, intake, role, "", "", "", "Not Placed");
    }
    public static void saveCompanySupervisor(String user, String pass, String name, String pos, String comp, String email) {
        saveUserFull(user, pass, name, pos, "Company Supervisor", email, "", comp, "N/A");
    }
    public static void saveAcademicSupervisor(String user, String pass, String name, String email) {
        saveUserFull(user, pass, name, "", "Academic Supervisor", email, "", "", "N/A");
    }

    private static void saveUserFull(String user, String pass, String name, String c4, String role, String c6, String c7, String c8, String c9) {
        long id = generateNextId(FILE_NAME, 0);
        
        String prefix = "U-";
        if (role.equalsIgnoreCase("Student")) prefix = "S-";
        else if (role.equalsIgnoreCase("Company Supervisor")) prefix = "C-";
        else if (role.equalsIgnoreCase("Academic Supervisor")) prefix = "A-";

        String formattedId = prefix + String.format("%05d", id);
        
        try (FileWriter fw = new FileWriter(FILE_NAME, true); PrintWriter out = new PrintWriter(fw)) {
            out.println(formattedId + "," + user + "," + pass + "," + name + "," + (c4==null?"":c4) + "," + role + "," + 
                        (c6==null?"":c6) + "," + (c7==null?"":c7) + "," + (c8==null?"":c8) + "," + (c9==null?"":c9));
        } catch (IOException e) {}
    }

    public static String[] getUserById(String id) {
        for (String line : readFile(FILE_NAME)) {
            String[] data = line.split(",");
            if (data.length > 0 && data[0].equals(id)) return data;
        }
        return null;
    }

    public static List<String[]> getUsersByRole(String role, String query) {
        List<String[]> results = new ArrayList<>();
        for (String line : readFile(FILE_NAME)) {
            String[] data = line.split(",");
            if (data.length >= 6 && data[5].equalsIgnoreCase(role)) {
                if (query.isEmpty() || data[3].toLowerCase().contains(query.toLowerCase()) || data[0].equals(query))
                    results.add(new String[]{data[0], data[1], data[3], (data.length > 8 ? data[8] : "")});
            }
        }
        return results;
    }

    public static void updateStudent(String id, String user, String pass, String name, String matric) {
        String[] c = getUserById(id);
        if(c == null) return;
        updateUser(id, user, pass, name, matric, "Student", c[6], c[7], c[8], c[9]);
    }
    public static void updateCompanySupervisor(String id, String user, String pass, String name, String pos, String comp, String email) {
        updateUser(id, user, pass, name, pos, "Company Supervisor", email, "", comp, "N/A");
    }
    public static void updateAcademicSupervisor(String id, String user, String pass, String name) {
        updateUser(id, user, pass, name, "", "Academic Supervisor", "", "", "", "");
    }
    public static void updateUser(String id, String user, String pass, String name, String c4, String role, String c6, String c7, String c8, String c9) {
        List<String> lines = readFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith(id + ",")) {
                newLines.add(id + "," + user + "," + pass + "," + name + "," + c4 + "," + role + "," + c6 + "," + c7 + "," + c8 + "," + c9);
            } else newLines.add(line);
        }
        writeFile(FILE_NAME, newLines);
    }
    public static void updateStudentPlacement(String studentId, String newStatus) {
        String[] s = getUserById(studentId);
        if(s!=null) updateUser(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], newStatus);
    }

    public static void saveListing(String reg, String comp, String loc, String job, String desc, String status) {
        long id = generateNextId(LISTING_FILE, 0);
        String listingId = "L-" + String.format("%05d", id);
        try(FileWriter fw = new FileWriter(LISTING_FILE, true); PrintWriter out = new PrintWriter(fw)){
            String safeDesc = desc.replace("\n", " ").replace(",", ";"); 
            out.println(listingId+","+reg+","+comp+","+loc+","+job+","+safeDesc+","+status);
        } catch(IOException e){}
        
        NotificationHelper.broadcastNotification("ROLE_ADMIN", "New Listing Posted by " + comp + ": " + job);
    }
    
    public static List<String[]> getAllListings() {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(LISTING_FILE)) {
            String[] d = line.split(",");
            if(d.length>=7) list.add(d);
        }
        return list;
    }
    public static void approveListing(String listingId) { updateStatus(LISTING_FILE, listingId, -1, "Approved", 0); }
    public static void rejectListing(String listingId) { updateStatus(LISTING_FILE, listingId, -1, "Rejected", 0); }

    public static void applyForInternship(String sid, String listingId, String comp) {
        long id = generateNextId(APP_FILE, 0);
        String appId = "App-" + String.format("%05d", id);
        try(FileWriter fw = new FileWriter(APP_FILE, true); PrintWriter out = new PrintWriter(fw)){
            out.println(appId+","+sid+","+listingId+","+comp+",Pending,N/A");
        } catch(IOException e){}
        
        NotificationHelper.broadcastNotification("ROLE_COMPANY", "New application received for listing " + listingId);
    }

    public static void sendOffer(String appId, String startDate, File contractFile) {
        try {
            Files.copy(contractFile.toPath(), new File(CONTRACT_FOLDER + File.separator + appId + ".pdf").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { return; }

        List<String> lines = readFile(APP_FILE);
        List<String> newLines = new ArrayList<>();
        String studentId = "";
        
        for(String line : lines) {
            String[] d = line.split(",");
            if(d[0].equals(appId)) {
                d[4] = "Offered";
                if(d.length > 5) d[5] = startDate;
                else line = String.join(",", d) + "," + startDate;
                studentId = d[1];
                newLines.add(String.join(",", d));
            } else {
                newLines.add(line);
            }
        }
        writeFile(APP_FILE, newLines);
        
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "You have received an Internship Offer! Check your applications.");
        }
    }

    public static boolean acceptOffer(String appId) {
        String[] appData = null;
        List<String> allApps = readFile(APP_FILE);
        
        for(String line : allApps) {
            String[] d = line.split(",");
            if(d[0].equals(appId)) {
                appData = d;
                break;
            }
        }
        
        if(appData == null) return false;

        String sid = appData[1];
        String listingId = appData[2];
        String startDate = (appData.length > 5) ? appData[5] : "N/A";

        for(String[] lst : getAllListings()) {
            if(lst[0].equals(listingId)) {
                if(lst[6].equalsIgnoreCase("Filled")) {
                    return false;
                }
            }
        }
        
        String[] s = getUserById(sid);
        String job = "Intern";
        String listingReg = "Unknown";
        String compSvId = "N/A"; 

        for(String[] lst : getAllListings()) {
            if(lst[0].equals(listingId)) {
                listingReg = lst[1]; 
                job = lst[4];
            }
        }
        
        List<String[]> svs = getUsersByRole("Company Supervisor", "");
        for(String[] sv : svs) {
            String[] full = getUserById(sv[0]);
            if(full.length > 8 && full[8].equalsIgnoreCase(appData[3])) {
                compSvId = full[0];
                break;
            }
        }

        if(s != null) saveMatch(sid, s[3], listingReg, appData[3], job, startDate, "N/A", compSvId);
        
        updateStatus(LISTING_FILE, listingId, -1, "Filled", 0);
        
        List<String> updatedApps = new ArrayList<>();
        for(String line : allApps) {
            String[] d = line.split(",");
            if(d.length < 2) { updatedApps.add(line); continue; }
            
            if(d[1].equals(sid)) {
                if(d[0].equals(appId)) {
                    d[4] = "Accepted"; 
                } else {
                    if(d[4].equalsIgnoreCase("Pending") || d[4].equalsIgnoreCase("Offered")) {
                        d[4] = "Withdrawn"; 
                    }
                }
                updatedApps.add(String.join(",", d));
            } 
            else if (d[2].equals(listingId)) {
                 if(d[4].equalsIgnoreCase("Pending") || d[4].equalsIgnoreCase("Offered")) {
                    d[4] = "Position Filled";
                    NotificationHelper.sendNotification(d[1], "The position for " + listingId + " has been filled by another candidate.");
                 }
                 updatedApps.add(String.join(",", d));
            }
            else {
                updatedApps.add(line);
            }
        }
        writeFile(APP_FILE, updatedApps);
        
        if(compSvId != null && !compSvId.equals("N/A")) {
            NotificationHelper.sendNotification(compSvId, "Offer Accepted! Student " + s[3] + " has joined your company.");
        }
        NotificationHelper.broadcastNotification("ROLE_ADMIN", "Match Confirmed: " + s[3] + " at " + appData[3]);
        
        return true;
    }

    public static void rejectApplication(String appId) { 
        updateStatus(APP_FILE, appId, 4, "Rejected", 0); 
        
        String studentId = "";
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d[0].equals(appId)) {
                studentId = d[1];
                break;
            }
        }
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "Your application " + appId + " was unsuccessful.");
        }
    }
    
    public static File getContractFile(String appId) {
        File f = new File(CONTRACT_FOLDER + File.separator + appId + ".pdf");
        return f.exists() ? f : null;
    }

    public static boolean hasApplied(String sid, String listingId) {
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d.length>=3 && d[1].equals(sid) && d[2].equals(listingId)) return true;
        }
        return false;
    }
    public static List<String[]> getApplicationsByStudent(String sid) {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d.length>=5 && d[1].equals(sid)) list.add(d);
        }
        return list;
    }

    public static List<String[]> getAllMatches() {
        List<String[]> list = new ArrayList<>();
        for (String line : readFile(MATCH_FILE)) {
            String[] data = line.split(",", -1);
            if (data.length < 9) {
                String[] newData = Arrays.copyOf(data, 9);
                if(data.length <= 7) newData[7] = "N/A";
                if(data.length <= 8) newData[8] = "N/A";
                list.add(newData);
            } else {
                list.add(data);
            }
        }
        return list;
    }
    public static void saveMatch(String sid, String sname, String reg, String cname, String job, String startDate, String aid, String cid) {
        long mid = generateNextId(MATCH_FILE, 0);
        try(FileWriter fw = new FileWriter(MATCH_FILE, true); PrintWriter out = new PrintWriter(fw)){
            out.println(String.format("%07d", mid)+","+sid+","+sname+","+reg+","+cname+","+job+","+startDate+","+aid+","+cid);
        } catch(IOException e){}
        updateStudentPlacement(sid, "Placed");
    }
    public static List<String[]> getMatchesForSupervisor(String supervisorId) {
        List<String[]> list = new ArrayList<>();
        String[] user = getUserById(supervisorId);
        String compName = (user != null && user.length > 8) ? user[8] : "Unknown";

        for (String[] m : getAllMatches()) {
            boolean isAcademicMatch = m[7].equals(supervisorId);
            boolean isCompanyMatch = m[8].equals(supervisorId);
            boolean isLegacyCompanyMatch = m[8].equals("N/A") && m[4].equalsIgnoreCase(compName);

            if (isAcademicMatch || isCompanyMatch || isLegacyCompanyMatch) list.add(m);
        }
        return list;
    }
    public static List<String[]> getMatchesMissingSupervisor() {
        List<String[]> list = new ArrayList<>();
        for (String[] m : getAllMatches()) {
            if (m[7].trim().isEmpty() || m[7].equalsIgnoreCase("N/A")) list.add(m);
        }
        return list;
    }
    public static void assignAcademicSupervisor(String matchId, String acadSvId) {
        List<String[]> matches = getAllMatches();
        List<String> lines = new ArrayList<>();
        String studentName = "";
        String compName = "";
        
        for (String[] m : matches) {
            if (m[0].equals(matchId)) {
                m[7] = acadSvId;
                studentName = m[2];
                compName = m[4];
            }
            lines.add(String.join(",", m));
        }
        writeFile(MATCH_FILE, lines);
        
        NotificationHelper.sendNotification(acadSvId, "You have been assigned to supervise " + studentName + " at " + compName);
    }

    public static void saveLogbookEntry(String sid, String date, String act, String hours) {
        long id = generateNextId(LOG_FILE, 0);
        try(FileWriter fw = new FileWriter(LOG_FILE, true); PrintWriter out = new PrintWriter(fw)){
            out.println(String.format("%07d", id)+","+sid+","+date+","+act.replace(",", ";")+","+hours+",Pending");
        } catch(IOException e){}
        
        NotificationHelper.broadcastNotification("ROLE_COMPANY", "New Logbook Entry from " + sid);
    }
    public static List<String[]> getAllLogbooks() {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(LOG_FILE)) list.add(line.split(","));
        return list;
    }
    public static List<String[]> getLogbooksByStudent(String sid) {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(LOG_FILE)) {
            String[] d = line.split(",");
            if(sid == null || (d.length>=2 && d[1].equals(sid))) list.add(d);
        }
        return list;
    }
    public static double getTotalVerifiedHours(String studentId) {
        double total = 0;
        List<String[]> logs = getLogbooksByStudent(studentId);
        for (String[] log : logs) {
            if (log.length >= 6 && "Verified".equalsIgnoreCase(log[5])) {
                try { total += Double.parseDouble(log[4]); } catch (NumberFormatException e) {}
            }
        }
        return total;
    }
    public static void updateLogbookStatus(String logId, String status) { 
        updateStatus(LOG_FILE, logId, -1, status, 0); 
        
        String studentId = "";
        for(String line : readFile(LOG_FILE)) {
            String[] d = line.split(",");
            if(d[0].equals(logId)) {
                studentId = d[1];
                break;
            }
        }
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "Logbook Entry " + logId + " status: " + status);
        }
    }
    public static void updateLogbookEntry(String logId, String d, String a, String h) {
        List<String> lines = readFile(LOG_FILE);
        List<String> newLines = new ArrayList<>();
        for(String l : lines) {
            String[] dt = l.split(",");
            if(dt[0].equals(logId)) newLines.add(dt[0]+","+dt[1]+","+d+","+a.replace(",", ";")+","+h+","+dt[5]);
            else newLines.add(l);
        }
        writeFile(LOG_FILE, newLines);
    }

    public static void saveAttendance(String sid, String sname, String date, String in, String out) {
        List<String> lines = readFile(ATTENDANCE_FILE);
        List<String> newLines = new ArrayList<>();
        boolean updated = false;
        for(String l : lines) {
            String[] d = l.split(",");
            if(d.length>=7 && d[1].equals(sid) && d[3].equals(date)) {
                String nOut = (out==null || out.isEmpty()) ? d[5] : out;
                String st = nOut.equals("Pending") ? "Pending" : "Completed";
                newLines.add(d[0]+","+d[1]+","+d[2]+","+d[3]+","+d[4]+","+nOut+","+st);
                updated = true;
            } else newLines.add(l);
        }
        if(!updated && in!=null) {
            long id = generateNextId(ATTENDANCE_FILE, 0);
            newLines.add(String.format("%07d", id)+","+sid+","+sname+","+date+","+in+",Pending,Pending");
        }
        writeFile(ATTENDANCE_FILE, newLines);
    }
    
    public static void updateAttendanceStatus(String attId, String status) {
        updateStatus(ATTENDANCE_FILE, attId, 6, status, 0);
        
        String studentId = "";
        for(String line : readFile(ATTENDANCE_FILE)) {
            String[] d = line.split(",");
            if(d[0].equals(attId)) {
                studentId = d[1];
                break;
            }
        }
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "Attendance record verified: " + status);
        }
    }
    
    public static void updateAttendanceEntry(String attId, String date, String in, String out, String status) {
        List<String> lines = readFile(ATTENDANCE_FILE);
        List<String> newLines = new ArrayList<>();
        for(String l : lines) {
            String[] d = l.split(",");
            if(d.length > 0 && d[0].equals(attId)) {
                newLines.add(d[0]+","+d[1]+","+d[2]+","+date+","+in+","+out+","+status);
            } else {
                newLines.add(l);
            }
        }
        writeFile(ATTENDANCE_FILE, newLines);
    }

    public static List<String[]> getStudentAttendance(String sid) {
        List<String[]> list = new ArrayList<>();
        for(String l : readFile(ATTENDANCE_FILE)) {
            String[] d = l.split(",");
            if(d.length>=7 && d[1].equals(sid)) list.add(d);
        }
        return list;
    }
    
    public static int calculateAttendancePercentage(String studentId) {
        LocalDate startDate = null;
        for(String[] m : getAllMatches()) {
            if(m[1].equals(studentId)) {
                try {
                    startDate = LocalDate.parse(m[6]);
                } catch(Exception e) {}
                break;
            }
        }
        
        if (startDate == null || startDate.isAfter(LocalDate.now())) return 100;

        LocalDate today = LocalDate.now();
        int expectedDays = 0;
        
        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                expectedDays++;
            }
        }
        
        if (expectedDays == 0) return 100;

        int attendedDays = 0;
        List<String[]> atts = getStudentAttendance(studentId);
        List<String> uniqueDates = new ArrayList<>();
        
        for (String[] a : atts) {
            if (a.length >= 7) {
                String dateStr = a[2];
                String status = a[6];
                
                if (status.equalsIgnoreCase("Verified") || 
                    status.equalsIgnoreCase("Medical Certificate") || 
                    status.equalsIgnoreCase("Public Holiday")) {
                    
                    if (!uniqueDates.contains(dateStr)) {
                        uniqueDates.add(dateStr);
                        attendedDays++;
                    }
                }
            }
        }
        
        int percentage = (int) (((double) attendedDays / expectedDays) * 100);
        return Math.min(percentage, 100);
    }
    
    public static void saveCompanyFeedback(String sid, String sname, String cname, String sc, String fb) {
        saveFeedback(COMPANY_FEEDBACK_FILE, sid, sname, cname, sc, fb);
        NotificationHelper.sendNotification(sid, "Company Supervisor has submitted your evaluation.");
        NotificationHelper.broadcastNotification("ROLE_ACADEMIC", "Company evaluation submitted for " + sname);
    }
    public static void saveAcademicFeedback(String sid, String sname, String sc, String fb) {
        saveFeedback(ACADEMIC_FEEDBACK_FILE, sid, sname, "Academic Supervisor", sc, fb);
        NotificationHelper.sendNotification(sid, "Academic Supervisor has submitted your evaluation.");
    }
    private static void saveFeedback(String fp, String sid, String sname, String by, String sc, String fb) {
        List<String> lines = readFile(fp);
        List<String> newLines = new ArrayList<>();
        boolean updated = false;
        for(String l : lines) {
            String[] d = l.split(",");
            if(d.length>=6 && d[1].equals(sid)) {
                d[4]=sc; d[5]=fb.replace(",", " "); newLines.add(String.join(",", d)); updated=true;
            } else newLines.add(l);
        }
        if(!updated) {
            long id = generateNextId(fp, 0);
            newLines.add(String.format("%07d", id)+","+sid+","+sname+","+by+","+sc+","+fb.replace(",", " "));
        }
        writeFile(fp, newLines);
    }
    public static String[] getStudentFeedback(String sid) {
        String cs="N/A", cf="N/A", cn="N/A", as="N/A", af="N/A";
        for(String l : readFile(COMPANY_FEEDBACK_FILE)) {
            String[] d = l.split(","); if(d.length>=6 && d[1].equals(sid)) { cn=d[3]; cs=d[4]; cf=d[5]; break; }
        }
        for(String l : readFile(ACADEMIC_FEEDBACK_FILE)) {
            String[] d = l.split(","); if(d.length>=6 && d[1].equals(sid)) { as=d[4]; af=d[5]; break; }
        }
        if(cs.equals("N/A") && as.equals("N/A")) return null;
        return new String[]{"0", sid, "Student", cn, "Completed", cs, as, cf, af};
    }
    public static boolean saveResume(File s, String sid) { return saveFile(s, RESUME_FOLDER + File.separator + sid); }
    public static boolean saveProfileImage(File s, String sid) { return saveFile(s, PFP_FOLDER + File.separator + sid + "_profile"); }
    public static File getResumeFile(String sid) { 
        File[] f = new File(RESUME_FOLDER).listFiles((d,n)->n.startsWith(sid+"."));
        return (f!=null && f.length>0) ? f[0] : null; 
    }
    public static File getProfileImage(String sid) { 
        File[] f = new File(PFP_FOLDER).listFiles((d,n)->n.startsWith(sid+"_profile."));
        return (f!=null && f.length>0) ? f[0] : null; 
    }
    private static boolean saveFile(File s, String d) {
        String ext = ""; int i=s.getName().lastIndexOf('.'); if(i>0) ext=s.getName().substring(i);
        try { Files.copy(s.toPath(), new File(d+ext).toPath(), StandardCopyOption.REPLACE_EXISTING); return true; } catch(IOException e){return false;}
    }

    private static void updateStatus(String fp, String id, int col, String val, int idCol) {
        List<String> ln = new ArrayList<>();
        for(String l : readFile(fp)) {
            String[] d = l.split(",");
            if(d.length > 0 && d[idCol].equals(id)) { 
                int targetCol = (col == -1) ? (d.length - 1) : col;
                
                if (targetCol < d.length) {
                    d[targetCol] = val;
                }
                ln.add(String.join(",", d));
            }
            else ln.add(l);
        }
        writeFile(fp, ln);
    }
}