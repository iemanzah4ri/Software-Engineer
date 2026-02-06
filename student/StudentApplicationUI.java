//handles the internship application process
//allows resume upload and file preview
package student;
import common.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class StudentApplicationUI extends JFrame {

    private String studentId, regNo;
    private File selectedFile;
    private JLabel lblFileName;
    private JLabel lblPreview; 
    private JPanel filePanel;
    private JTextArea txtDesc;
    
    public StudentApplicationUI(String stdId, String reg, String comp, String loc, String job, String desc) {
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
        btnImport.addActionListener(e -> {
            JFileChooser ch = new JFileChooser();
            ch.setFileFilter(new FileNameExtensionFilter("Documents & Images", "pdf", "jpg", "png"));
            if (ch.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile = ch.getSelectedFile();
                lblFileName.setText("Selected: " + selectedFile.getName());
                updatePreview(selectedFile);
            }
        });
        mainPanel.add(btnImport);

        lblFileName = new JLabel("No file selected");
        lblFileName.setBounds(220, 30, 300, 30);
        mainPanel.add(lblFileName);

        filePanel = new JPanel(new BorderLayout());
        filePanel.setBounds(50, 80, 400, 250);
        filePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        filePanel.setBackground(new Color(245, 245, 245));
        
        lblPreview = new JLabel("File Preview", SwingConstants.CENTER);
        lblPreview.setForeground(Color.GRAY);
        filePanel.add(lblPreview, BorderLayout.CENTER);
        mainPanel.add(filePanel);

        JLabel lblInfo = new JLabel("<html><b>Company:</b> " + comp + "<br><b>Job:</b> " + job + "</html>");
        lblInfo.setBounds(480, 80, 250, 50);
        mainPanel.add(lblInfo);
        
        txtDesc = new JTextArea("Notes for employer...");
        txtDesc.setBounds(480, 140, 250, 190);
        txtDesc.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mainPanel.add(txtDesc);

        add(mainPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSubmit = new JButton("Confirm Application");
        JButton btnBack = new JButton("Cancel");
        
        btnSubmit.setBackground(new Color(100, 200, 100));
        
        bottomPanel.add(btnSubmit);
        bottomPanel.add(btnBack);
        add(bottomPanel, BorderLayout.SOUTH);

        btnSubmit.addActionListener(e -> {
            File resumeCheck = DatabaseHelper.getResumeFile(studentId); // UPDATED
            if (resumeCheck == null) {
                int confirm = JOptionPane.showConfirmDialog(this, "No resume uploaded. Apply anyway?");
                if (confirm != JOptionPane.YES_OPTION) return;
            }
            
            DatabaseHelper.applyForInternship(studentId, regNo, comp); // UPDATED
            JOptionPane.showMessageDialog(this, "Application Submitted!");
            dispose();
        });

        btnBack.addActionListener(e -> dispose());
    }

    private void updatePreview(File file) {
        String name = file.getName().toLowerCase();
        
        if (name.endsWith(".pdf")) {
            lblPreview.setIcon(null);
            lblPreview.setText("<html><center>PDF Document Selected<br>" + file.getName() + "</center></html>");
        } else {
            try {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
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