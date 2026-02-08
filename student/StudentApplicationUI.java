package student;
// importing libraries for ui and file handling
import common.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

// window to apply for a job and upload resume
public class StudentApplicationUI extends JFrame {

    // variables for application data
    private String studentId, regNo;
    private File selectedFile;
    private JLabel lblFileName;
    private JLabel lblPreview; 
    private JPanel filePanel;
    private JTextArea txtDesc;
    
    // constructor to set up the window
    public StudentApplicationUI(String stdId, String reg, String comp, String loc, String job, String desc) {
        // save the passed data
        this.studentId = stdId;
        this.regNo = reg;
        
        // window settings
        setTitle("Apply & Upload Documents");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // header title
        JLabel title = new JLabel("Apply & Upload Documents", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.PLAIN, 28));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // main container
        JPanel mainPanel = new JPanel(null); 
        mainPanel.setBackground(Color.WHITE);

        // button to choose file
        JButton btnImport = new JButton("Import File");
        btnImport.setBounds(50, 30, 150, 30);
        // logic for file chooser
        btnImport.addActionListener(e -> {
            JFileChooser ch = new JFileChooser();
            // restrict file types
            ch.setFileFilter(new FileNameExtensionFilter("Documents & Images", "pdf", "jpg", "png"));
            // if user picks a file
            if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = ch.getSelectedFile();
                // show name
                lblFileName.setText("Selected: " + selectedFile.getName());
                // show preview
                updatePreview(selectedFile);
            }
        });
        mainPanel.add(btnImport);

        // label to show selected filename
        lblFileName = new JLabel("No file selected");
        lblFileName.setBounds(220, 30, 300, 30);
        mainPanel.add(lblFileName);

        // panel to show the image preview
        filePanel = new JPanel(new BorderLayout());
        filePanel.setBounds(50, 80, 400, 250);
        filePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        filePanel.setBackground(new Color(245, 245, 245));
        
        lblPreview = new JLabel("File Preview", SwingConstants.CENTER);
        lblPreview.setForeground(Color.GRAY);
        filePanel.add(lblPreview, BorderLayout.CENTER);
        mainPanel.add(filePanel);

        // show job info
        JLabel lblInfo = new JLabel("<html><b>Company:</b> " + comp + "<br><b>Job:</b> " + job + "</html>");
        lblInfo.setBounds(480, 80, 250, 50);
        mainPanel.add(lblInfo);
        
        // box for notes
        txtDesc = new JTextArea("Notes for employer...");
        txtDesc.setBounds(480, 140, 250, 190);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(txtDesc);

        add(mainPanel, BorderLayout.CENTER);

        // bottom buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Confirm Application");
        JButton btnBack = new JButton("Cancel");
        
        btnSubmit.setBackground(new Color(100, 200, 100));
        
        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        // submit action
        btnSubmit.addActionListener(e -> {
            // check if resume exists
            File resumeCheck = DatabaseHelper.getResumeFile(studentId); 
            if (resumeCheck == null) {
                // ask if sure
                int confirm = JOptionPane.showConfirmDialog(this, "No resume uploaded. Apply anyway?");
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            
            // apply via db helper
            DatabaseHelper.applyForInternship(studentId, regNo, comp); 
            JOptionPane.showMessageDialog(this, "Application Submitted!");
            dispose();
        });

        // close window
        btnBack.addActionListener(e -> dispose());
    }

    // helper function to show preview
    private void updatePreview(File file) {
        String name = file.getName().toLowerCase();
        
        // handle pdfs differently
        if (name.endsWith(".pdf")) {
            lblPreview.setIcon(null);
            lblPreview.setText("<html><center>PDF Document Selected<br>" + file.getName() + "</center></html>");
        } else {
            // handle images
            try {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    // scale image to fit panel
                    Image scaled = img.getScaledInstance(filePanel.getWidth(), filePanel.getHeight(), Image.SCALE_SMOOTH);
                    lblPreview.setIcon(new ImageIcon(scaled));
                    lblPreview.setText(""); 
                } else {
                    lblPreview.setIcon(null);
                    lblPreview.setText("Preview Not Available");
                }
            } catch (Exception ex) {
                lblPreview.setIcon(null);
                lblPreview.setText("Error Loading Preview");
            }
        }
    }
}