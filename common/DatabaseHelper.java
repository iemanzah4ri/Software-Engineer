package common;
// importing all the io stuff for files
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// this class handles all the saving and loading from text files
public class DatabaseHelper {
    // folder names so i dont have to type "database" everywhere
    private static final String DB_FOLDER = "database";
    private static final String RESUME_FOLDER = DB_FOLDER + File.separator + "resume";
    private static final String PFP_FOLDER = DB_FOLDER + File.separator + "pfp";
    private static final String CONTRACT_FOLDER = DB_FOLDER + File.separator + "contracts";
    
    // file paths for all the data
    private static final String FILE_NAME = DB_FOLDER + File.separator + "users.txt";
    private static final String LISTING_FILE = DB_FOLDER + File.separator + "listings.txt";
    private static final String APP_FILE = DB_FOLDER + File.separator + "applications.txt";
    private static final String LOG_FILE = DB_FOLDER + File.separator + "logbooks.txt";
    private static final String MATCH_FILE = DB_FOLDER + File.separator + "matches.txt";
    private static final String ATTENDANCE_FILE = DB_FOLDER + File.separator + "attendance.txt";
    
    // feedback files
    private static final String COMPANY_FEEDBACK_FILE = DB_FOLDER + File.separator + "company_feedback.txt";
    private static final String ACADEMIC_FEEDBACK_FILE = DB_FOLDER + File.separator + "academic_feedback.txt";

    // run this once to make sure folders exist or it crashes
    static {
        new File(DB_FOLDER).mkdirs();
        new File(RESUME_FOLDER).mkdirs();
        new File(PFP_FOLDER).mkdirs();
        new File(CONTRACT_FOLDER).mkdirs();
    }

    // helper function to auto increment ids
    private static long generateNextId(String filePath, int idIndex) {
        long maxId = 0;
        File file = new File(filePath);
        // check if file exists first
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                // loop through every line
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    // make sure line isnt empty
                    if (data.length > idIndex) {
                        try {
                            // remove non-numbers and parse
                            String numPart = data[idIndex].replaceAll("\\D+", "");
                            long currentId = Long.parseLong(numPart);
                            // keep track of the biggest one
                            if (currentId > maxId) maxId = currentId;
                        } catch (NumberFormatException e) {}
                    }
                }
            } catch (IOException e) {}
        }
        // return the next available id
        return maxId + 1;
    }

    // reads a file and returns list of strings
    private static List<String> readFile(String path) {
        List<String> lines = new ArrayList<>();
        File file = new File(path);
        // return empty list if no file
        if (!file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            // add each line to the list
            while ((line = br.readLine()) != null) lines.add(line);
        } catch (IOException e) {}
        return lines;
    }

    // overwrites a file with new lines
    private static void writeFile(String path, List<String> lines) {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            for (String line : lines) out.println(line);
        } catch (IOException e) {}
    }

    // simple wrapper to save a student
    public static void saveUser(String user, String pass, String name, String intake, String role) {
        saveUserFull(user, pass, name, intake, role, "", "", "", "Not Placed");
    }
    // wrapper for company supervisor
    public static void saveCompanySupervisor(String user, String pass, String name, String pos, String comp, String email) {
        saveUserFull(user, pass, name, pos, "Company Supervisor", email, "", comp, "N/A");
    }
    // wrapper for academic supervisor
    public static void saveAcademicSupervisor(String user, String pass, String name, String email) {
        saveUserFull(user, pass, name, "", "Academic Supervisor", email, "", "", "N/A");
    }

    // the main save function that writes to users.txt
    private static void saveUserFull(String user, String pass, String name, String c4, String role, String c6, String c7, String c8, String c9) {
        // get a new id
        long id = generateNextId(FILE_NAME, 0);
        
        // determine the prefix like U-0001 or S-0001
        String prefix = "U-";
        if (role.equalsIgnoreCase("Student")) prefix = "S-";
        else if (role.equalsIgnoreCase("Company Supervisor")) prefix = "C-";
        else if (role.equalsIgnoreCase("Academic Supervisor")) prefix = "A-";

        // format with leading zeros
        String formattedId = prefix + String.format("%05d", id);
        
        // append to file
        try (FileWriter fw = new FileWriter(FILE_NAME, true); PrintWriter out = new PrintWriter(fw)) {
            out.println(formattedId + "," + user + "," + pass + "," + name + "," + (c4==null?"":c4) + "," + role + "," + 
                        (c6==null?"":c6) + "," + (c7==null?"":c7) + "," + (c8==null?"":c8) + "," + (c9==null?"":c9));
        } catch (IOException e) {}
    }

    // find a user by their ID string
    public static String[] getUserById(String id) {
        for (String line : readFile(FILE_NAME)) {
            String[] data = line.split(",");
            // check first column
            if (data.length > 0 && data[0].equals(id)) return data;
        }
        return null;
    }

    // search for users with a specific role and keyword
    public static List<String[]> getUsersByRole(String role, String query) {
        List<String[]> results = new ArrayList<>();
        for (String line : readFile(FILE_NAME)) {
            String[] data = line.split(",");
            // check if role matches
            if (data.length >= 6 && data[5].equalsIgnoreCase(role)) {
                // simple search logic
                if (query.isEmpty() || data[3].toLowerCase().contains(query.toLowerCase()) || data[0].equals(query))
                    results.add(new String[]{data[0], data[1], data[3], (data.length > 8 ? data[8] : "")});
            }
        }
        return results;
    }

    // update functions for specific roles
    public static void updateStudent(String id, String user, String pass, String name, String matric) {
        String[] c = getUserById(id);
        if(c == null) return;
        // keep old values for stuff not changing
        updateUser(id, user, pass, name, matric, "Student", c[6], c[7], c[8], c[9]);
    }
    public static void updateCompanySupervisor(String id, String user, String pass, String name, String pos, String comp, String email) {
        updateUser(id, user, pass, name, pos, "Company Supervisor", email, "", comp, "N/A");
    }
    public static void updateAcademicSupervisor(String id, String user, String pass, String name) {
        updateUser(id, user, pass, name, "", "Academic Supervisor", "", "", "", "");
    }
    
    // reads all lines, changes the matching one, writes back
    public static void updateUser(String id, String user, String pass, String name, String c4, String role, String c6, String c7, String c8, String c9) {
        List<String> lines = readFile(FILE_NAME);
        List<String> newLines = new ArrayList<>();
        for (String line : lines) {
            // find the line starting with id
            if (line.startsWith(id + ",")) {
                newLines.add(id + "," + user + "," + pass + "," + name + "," + c4 + "," + role + "," + c6 + "," + c7 + "," + c8 + "," + c9);
            } else newLines.add(line);
        }
        // save everything back
        writeFile(FILE_NAME, newLines);
    }
    
    // just updates the student status column
    public static void updateStudentPlacement(String studentId, String newStatus) {
        String[] s = getUserById(studentId);
        if(s!=null) updateUser(s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], newStatus);
    }

    // create a new job listing
    public static void saveListing(String reg, String comp, String loc, String job, String desc, String status) {
        long id = generateNextId(LISTING_FILE, 0);
        String listingId = "L-" + String.format("%05d", id);
        try(FileWriter fw = new FileWriter(LISTING_FILE, true); PrintWriter out = new PrintWriter(fw)){
            // sanitize description to avoid csv breaking
            String safeDesc = desc.replace("\n", " ").replace(",", ";"); 
            out.println(listingId+","+reg+","+comp+","+loc+","+job+","+safeDesc+","+status);
        } catch(IOException e){}
        
        // tell admin about it
        NotificationHelper.broadcastNotification("ROLE_ADMIN", "New Listing Posted by " + comp + ": " + job);
    }
    
    // get all jobs
    public static List<String[]> getAllListings() {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(LISTING_FILE)) {
            String[] d = line.split(",");
            if(d.length>=7) list.add(d);
        }
        return list;
    }
    
    // helper methods to change listing status
    public static void approveListing(String listingId) { updateStatus(LISTING_FILE, listingId, -1, "Approved", 0); }
    public static void rejectListing(String listingId) { updateStatus(LISTING_FILE, listingId, -1, "Rejected", 0); }

    // student applying for job
    public static void applyForInternship(String sid, String listingId, String comp) {
        long id = generateNextId(APP_FILE, 0);
        String appId = "App-" + String.format("%05d", id);
        try(FileWriter fw = new FileWriter(APP_FILE, true); PrintWriter out = new PrintWriter(fw)){
            out.println(appId+","+sid+","+listingId+","+comp+",Pending,N/A");
        } catch(IOException e){}
        
        // notify company
        NotificationHelper.broadcastNotification("ROLE_COMPANY", "New application received for listing " + listingId);
    }

    // company sending offer to student
    public static void sendOffer(String appId, String startDate, File contractFile) {
        // copy the pdf file
        try {
            Files.copy(contractFile.toPath(), new File(CONTRACT_FOLDER + File.separator + appId + ".pdf").toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { return; }

        List<String> lines = readFile(APP_FILE);
        List<String> newLines = new ArrayList<>();
        String studentId = "";
        
        // update application status
        for(String line : lines) {
            String[] d = line.split(",");
            if(d[0].equals(appId)) {
                d[4] = "Offered";
                // store start date
                if(d.length > 5) d[5] = startDate;
                else line = String.join(",", d) + "," + startDate;
                studentId = d[1];
                newLines.add(String.join(",", d));
            } else {
                newLines.add(line);
            }
        }
        writeFile(APP_FILE, newLines);
        
        // ping student
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "You have received an Internship Offer! Check your applications.");
        }
    }

    // complicated logic for accepting an offer
    public static boolean acceptOffer(String appId) {
        String[] appData = null;
        List<String> allApps = readFile(APP_FILE);
        
        // find the app
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

        // check if job is already taken
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

        // get job details
        for(String[] lst : getAllListings()) {
            if(lst[0].equals(listingId)) {
                listingReg = lst[1]; 
                job = lst[4];
            }
        }
        
        // try to find the supervisor for this company
        List<String[]> svs = getUsersByRole("Company Supervisor", "");
        for(String[] sv : svs) {
            String[] full = getUserById(sv[0]);
            if(full.length > 8 && full[8].equalsIgnoreCase(appData[3])) {
                compSvId = full[0];
                break;
            }
        }

        // save the final match
        if(s != null) saveMatch(sid, s[3], listingReg, appData[3], job, startDate, "N/A", compSvId);
        
        // close the listing
        updateStatus(LISTING_FILE, listingId, -1, "Filled", 0);
        
        // clean up other applications
        List<String> updatedApps = new ArrayList<>();
        for(String line : allApps) {
            String[] d = line.split(",");
            if(d.length < 2) { updatedApps.add(line); continue; }
            
            if(d[1].equals(sid)) {
                if(d[0].equals(appId)) {
                    d[4] = "Accepted"; 
                } else {
                    // withdraw other pending apps for this student
                    if(d[4].equalsIgnoreCase("Pending") || d[4].equalsIgnoreCase("Offered")) {
                        d[4] = "Withdrawn"; 
                    }
                }
                updatedApps.add(String.join(",", d));
            } 
            else if (d[2].equals(listingId)) {
                 // reject other students for this job
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
        
        // notify everyone
        if(compSvId != null && !compSvId.equals("N/A")) {
            NotificationHelper.sendNotification(compSvId, "Offer Accepted! Student " + s[3] + " has joined your company.");
        }
        NotificationHelper.broadcastNotification("ROLE_ADMIN", "Match Confirmed: " + s[3] + " at " + appData[3]);
        
        return true;
    }

    // reject an application
    public static void rejectApplication(String appId) { 
        updateStatus(APP_FILE, appId, 4, "Rejected", 0); 
        
        String studentId = "";
        // find who owns this app
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d[0].equals(appId)) {
                studentId = d[1];
                break;
            }
        }
        // send bad news
        if(!studentId.isEmpty()) {
            NotificationHelper.sendNotification(studentId, "Your application " + appId + " was unsuccessful.");
        }
    }
    
    // checks if contract exists
    public static File getContractFile(String appId) {
        File f = new File(CONTRACT_FOLDER + File.separator + appId + ".pdf");
        return f.exists() ? f : null;
    }

    // check if already applied
    public static boolean hasApplied(String sid, String listingId) {
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d.length>=3 && d[1].equals(sid) && d[2].equals(listingId)) return true;
        }
        return false;
    }
    
    // get list of apps for a student
    public static List<String[]> getApplicationsByStudent(String sid) {
        List<String[]> list = new ArrayList<>();
        for(String line : readFile(APP_FILE)) {
            String[] d = line.split(",");
            if(d.length>=5 && d[1].equals(sid)) list.add(d);
        }
        return list;
    }

    // load all matches, handling old data formats
    public static List<String[]> getAllMatches() {
        List<String[]> list = new ArrayList<>();
        for (String line : readFile(MATCH_FILE)) {
            String[] data = line.split(",", -1);
            // fix if missing columns
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
    
    // create a new match entry
    public static void saveMatch(String sid, String sname, String reg, String cname, String job, String startDate, String aid, String cid) {
        long mid = generateNextId(MATCH_FILE, 0);
        try(FileWriter fw = new FileWriter(MATCH_FILE, true); PrintWriter out = new PrintWriter(fw)){
            out.println(String.format("%07d", mid)+","+sid+","+sname+","+reg+","+cname+","+job+","+startDate+","+aid+","+cid);
        } catch(IOException e){}
        // update user status too
        updateStudentPlacement(sid, "Placed");
    }
    
    // find students assigned to a supervisor
    public static List<String[]> getMatchesForSupervisor(String supervisorId) {
        List<String[]> list = new ArrayList<>();
        String[] user = getUserById(supervisorId);
        String compName = (user != null && user.length > 8) ? user[8] : "Unknown";

        for (String[] m : getAllMatches()) {
            // check different columns based on who the supervisor is
            boolean isAcademicMatch = m[7].equals(supervisorId);
            boolean isCompanyMatch = m[8].equals(supervisorId);
            // fallback for old data
            boolean isLegacyCompanyMatch = m[8].equals("N/A") && m[4].equalsIgnoreCase(compName);

            if (isAcademicMatch || isCompanyMatch || isLegacyCompanyMatch) list.add(m);
        }
        return list;
    }
    
    // find students with no supervisor yet
    public static List<String[]> getMatchesMissingSupervisor() {
        List<String[]> list = new ArrayList<>();
        for (String[] m : getAllMatches()) {
            if (m[7].trim().isEmpty() || m[7].equalsIgnoreCase("N/A")) list.add(m);
        }
        return list;
    }
    
    // link academic supervisor to match
    public static void assignAcademicSupervisor(String matchId, String acadSvId) {
        List<String[]> matches = getAllMatches();
        List<String> lines = new ArrayList<>();
        String studentName = "";
        String compName = "";
        
        // loop and update
        for (String[] m : matches) {
            if (m[0].equals(matchId)) {
                m[7] = acadSvId;
                studentName = m[2];
                compName = m[4];
            }
            lines.add(String.join(",", m));
        }
        writeFile(MATCH_FILE, lines);
        
        // let them know
        NotificationHelper.sendNotification(acadSvId, "You have been assigned to supervise " + studentName + " at " + compName);
    }

    // logbook stuff
    public static void saveLogbookEntry(String sid, String date, String act, String hours) {
        long id = generateNextId(LOG_FILE, 0);
        try(FileWriter fw = new FileWriter(LOG_FILE, true); PrintWriter out = new PrintWriter(fw)){
            // replacing commas again
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
    
    // calc total hours logic
    public static double getTotalVerifiedHours(String studentId) {
        double total = 0;
        List<String[]> logs = getLogbooksByStudent(studentId);
        for (String[] log : logs) {
            // only count verified entries
            if (log.length >= 6 && "Verified".equalsIgnoreCase(log[5])) {
                try { total += Double.parseDouble(log[4]); } catch (NumberFormatException e) {}
            }
        }
        return total;
    }
    
    // change log status
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
    
    // edit log entry
    public static void updateLogbookEntry(String logId, String d, String a, String h) {
        List<String> lines = readFile(LOG_FILE);
        List<String> newLines = new ArrayList<>();
        for(String l : lines) {
            String[] dt = l.split(",");
            // find matching id and replace values
            if(dt[0].equals(logId)) newLines.add(dt[0]+","+dt[1]+","+d+","+a.replace(",", ";")+","+h+","+dt[5]);
            else newLines.add(l);
        }
        writeFile(LOG_FILE, newLines);
    }

    // attendance tracking
    public static void saveAttendance(String sid, String sname, String date, String in, String out) {
        List<String> lines = readFile(ATTENDANCE_FILE);
        List<String> newLines = new ArrayList<>();
        boolean updated = false;
        
        for(String l : lines) {
            String[] d = l.split(",");
            // check if record for today exists
            if(d.length>=7 && d[1].equals(sid) && d[3].equals(date)) {
                String nOut = (out==null || out.isEmpty()) ? d[5] : out;
                String st = nOut.equals("Pending") ? "Pending" : "Completed";
                newLines.add(d[0]+","+d[1]+","+d[2]+","+d[3]+","+d[4]+","+nOut+","+st);
                updated = true;
            } else newLines.add(l);
        }
        // if not found, create new
        if(!updated && in!=null) {
            long id = generateNextId(ATTENDANCE_FILE, 0);
            newLines.add(String.format("%07d", id)+","+sid+","+sname+","+date+","+in+",Pending,Pending");
        }
        writeFile(ATTENDANCE_FILE, newLines);
    }
    
    public static void updateAttendanceStatus(String attId, String status) {
        updateStatus(ATTENDANCE_FILE, attId, 6, status, 0);
        
        // notify student
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
    
    // manual edit of attendance
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
    
    // calculating percentage is hard
    public static int calculateAttendancePercentage(String studentId) {
        LocalDate startDate = null;
        // get start date from match
        for(String[] m : getAllMatches()) {
            if(m[1].equals(studentId)) {
                try {
                    startDate = LocalDate.parse(m[6]);
                } catch(Exception e) {}
                break;
            }
        }
        
        // if no start date, default to 100
        if (startDate == null || startDate.isAfter(LocalDate.now())) return 100;

        LocalDate today = LocalDate.now();
        int expectedDays = 0;
        
        // count workdays excluding weekends
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
        
        // count valid attendance entries
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
        
        // percentage logic
        int percentage = (int) (((double) attendedDays / expectedDays) * 100);
        return Math.min(percentage, 100);
    }
    
    // feedback saving
    public static void saveCompanyFeedback(String sid, String sname, String cname, String sc, String fb) {
        saveFeedback(COMPANY_FEEDBACK_FILE, sid, sname, cname, sc, fb);
        NotificationHelper.sendNotification(sid, "Company Supervisor has submitted your evaluation.");
        NotificationHelper.broadcastNotification("ROLE_ACADEMIC", "Company evaluation submitted for " + sname);
    }
    public static void saveAcademicFeedback(String sid, String sname, String sc, String fb) {
        saveFeedback(ACADEMIC_FEEDBACK_FILE, sid, sname, "Academic Supervisor", sc, fb);
        NotificationHelper.sendNotification(sid, "Academic Supervisor has submitted your evaluation.");
    }
    
    // helper to save feedback to correct file
    private static void saveFeedback(String fp, String sid, String sname, String by, String sc, String fb) {
        List<String> lines = readFile(fp);
        List<String> newLines = new ArrayList<>();
        boolean updated = false;
        // check if existing feedback and update it
        for(String l : lines) {
            String[] d = l.split(",");
            if(d.length>=6 && d[1].equals(sid)) {
                d[4]=sc; d[5]=fb.replace(",", " "); newLines.add(String.join(",", d)); updated=true;
            } else newLines.add(l);
        }
        // if new, add line
        if(!updated) {
            long id = generateNextId(fp, 0);
            newLines.add(String.format("%07d", id)+","+sid+","+sname+","+by+","+sc+","+fb.replace(",", " "));
        }
        writeFile(fp, newLines);
    }
    
    // get both feedbacks combined
    public static String[] getStudentFeedback(String sid) {
        String cs="N/A", cf="N/A", cn="N/A", as="N/A", af="N/A";
        // get company part
        for(String l : readFile(COMPANY_FEEDBACK_FILE)) {
            String[] d = l.split(","); if(d.length>=6 && d[1].equals(sid)) { cn=d[3]; cs=d[4]; cf=d[5]; break; }
        }
        // get academic part
        for(String l : readFile(ACADEMIC_FEEDBACK_FILE)) {
            String[] d = l.split(","); if(d.length>=6 && d[1].equals(sid)) { as=d[4]; af=d[5]; break; }
        }
        // return null if nothing found
        if(cs.equals("N/A") && as.equals("N/A")) return null;
        return new String[]{"0", sid, "Student", cn, "Completed", cs, as, cf, af};
    }
    
    // simple file copy helpers for resume/pfp
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
    // generic save file function
    private static boolean saveFile(File s, String d) {
        String ext = ""; int i=s.getName().lastIndexOf('.'); if(i>0) ext=s.getName().substring(i);
        try { Files.copy(s.toPath(), new File(d+ext).toPath(), StandardCopyOption.REPLACE_EXISTING); return true; } catch(IOException e){return false;}
    }

    // generic update status helper
    private static void updateStatus(String fp, String id, int col, String val, int idCol) {
        List<String> ln = new ArrayList<>();
        for(String l : readFile(fp)) {
            String[] d = l.split(",");
            if(d.length > 0 && d[idCol].equals(id)) { 
                // calculate col index
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

    // check if student can log hours
    public static boolean isInternshipStarted(String studentId) {
        List<String> matches = readFile("matches.txt");
        java.time.LocalDate today = java.time.LocalDate.now();
        
        for (String line : matches) {
            String[] data = line.split(",");
            // find student match
            if (data.length > 6 && data[1].equals(studentId)) {
                String dateStr = data[6]; // start date column
                try {
                    java.time.LocalDate startDate = java.time.LocalDate.parse(dateStr);
                    
                    // check if date is in future
                    if (today.isBefore(startDate)) {
                        return false;
                    }
                    return true; 
                    
                } catch (Exception e) {
                    System.out.println("Date parse error for student " + studentId);
                    return true; // allow if error so we dont lock them out
                }
            }
        }
        return false; // no job found
    }
}