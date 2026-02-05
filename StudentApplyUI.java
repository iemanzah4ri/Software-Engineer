import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class StudentApplyUI extends JFrame {

    private String studentId, regNo;
    private File selectedFile;
    private JLabel lblFileName;
    private JTextArea txtDesc;
    
    public StudentApplyUI(String stdId, String reg, String comp, String loc, String job, String desc) {
        this.studentId = stdId;
        this.regNo = reg;
        
        setTitle("Apply & Upload Documents");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Apply & Upload Documents", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.PLAIN, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(null); 
        mainPanel.setBackground(Color.WHITE);

        JButton btnImport = new JButton("Import File");
        btnImport.setBounds(50, 30, 150, 30);
        btnImport.setBackground(new Color(220, 220, 220));
        mainPanel.add(btnImport);

        lblFileName = new JLabel("No file selected");
        lblFileName.setBounds(210, 30, 200, 30);
        mainPanel.add(lblFileName);

        JPanel filePanel = new JPanel();
        filePanel.setBounds(50, 80, 350, 150);
        filePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        mainPanel.add(filePanel);

        JButton btnUpload = new JButton("Upload");
        btnUpload.setBounds(50, 250, 150, 30);
        btnUpload.setBackground(new Color(220, 220, 220));
        mainPanel.add(btnUpload);

        JLabel lblComp = new JLabel("Company Name");
        lblComp.setBounds(450, 30, 120, 25);
        mainPanel.add(lblComp);
        
        JTextField txtComp = new JTextField(comp);
        txtComp.setEditable(false);
        txtComp.setBounds(580, 30, 180, 25);
        mainPanel.add(txtComp);

        JLabel lblLoc = new JLabel("Location");
        lblLoc.setBounds(450, 70, 120, 25);
        mainPanel.add(lblLoc);

        JTextField txtLoc = new JTextField(loc);
        txtLoc.setEditable(false);
        txtLoc.setBounds(580, 70, 180, 25);
        mainPanel.add(txtLoc);
        
        JLabel lblJob = new JLabel("Job Title");
        lblJob.setBounds(450, 110, 120, 25);
        mainPanel.add(lblJob);

        JTextField txtJob = new JTextField(job);
        txtJob.setEditable(false);
        txtJob.setBounds(580, 110, 180, 25);
        mainPanel.add(txtJob);

        JLabel lblDesc = new JLabel("Job Description");
        lblDesc.setBounds(450, 150, 120, 25);
        mainPanel.add(lblDesc);

        txtDesc = new JTextArea(desc);
        txtDesc.setEditable(false);
        txtDesc.setLineWrap(true);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        txtDesc.setBounds(580, 150, 180, 100);
        mainPanel.add(txtDesc);

        JButton btnApply = new JButton("Apply");
        btnApply.setBounds(580, 270, 180, 40);
        btnApply.setBackground(new Color(200, 200, 200));
        mainPanel.add(btnApply);

        JButton btnBack = new JButton("Back Home");
        btnBack.setBounds(600, 380, 140, 30);
        btnBack.setBackground(new Color(220, 220, 220));
        mainPanel.add(btnBack);

        add(mainPanel, BorderLayout.CENTER);

        btnImport.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Documents & Images", "jpg", "jpeg", "png", "pdf");
            chooser.setFileFilter(filter);
            
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File tempFile = chooser.getSelectedFile();
                String name = tempFile.getName().toLowerCase();
                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".pdf")) {
                    selectedFile = tempFile;
                    lblFileName.setText(selectedFile.getName());
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid file format. Please select JPG, PNG, or PDF.");
                }
            }
        });

        btnUpload.addActionListener(e -> {
            if (selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please select a file first.");
                return;
            }
            if (DBHelper.saveResume(selectedFile, studentId)) {
                JOptionPane.showMessageDialog(this, "Resume Uploaded Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Error uploading file.");
            }
        });

        btnApply.addActionListener(e -> {
            File resumeCheck = DBHelper.getResumeFile(studentId);
            if (resumeCheck == null) {
                int confirm = JOptionPane.showConfirmDialog(this, "No resume uploaded. Apply anyway?");
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            
            DBHelper.applyForInternship(studentId, regNo, comp);
            JOptionPane.showMessageDialog(this, "Application Submitted!");
            dispose();
        });

        btnBack.addActionListener(e -> dispose());
    }
}