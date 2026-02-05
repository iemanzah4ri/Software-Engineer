import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginForm extends javax.swing.JFrame {

    public LoginForm() {
        initComponents();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2,dim.height/2-this.getSize().height/2);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        btnAdminLogin = new javax.swing.JButton();
        btnPMLogin = new javax.swing.JButton();
        btnStudentLogin = new javax.swing.JButton();
        btnLecturerLogin = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        btnAdminLogin.setBackground(new java.awt.Color(153, 255, 255));
        btnAdminLogin.setText("Login as Admin");
        btnAdminLogin.addActionListener(evt -> btnAdminLoginActionPerformed(evt));

        btnPMLogin.setBackground(new java.awt.Color(153, 255, 153));
        btnPMLogin.setText("Login as Comp Supp");
        btnPMLogin.addActionListener(evt -> btnPMLoginActionPerformed(evt));

        btnStudentLogin.setBackground(new java.awt.Color(255, 255, 51));
        btnStudentLogin.setText("Login as Student");
        btnStudentLogin.addActionListener(evt -> btnStudentLoginActionPerformed(evt));

        btnLecturerLogin.setBackground(new java.awt.Color(255, 204, 153));
        btnLecturerLogin.setText("Login as Acad Supp");
        btnLecturerLogin.addActionListener(evt -> btnLecturerLoginActionPerformed(evt));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); 
        jLabel3.setText("Academic Guidance Hub");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); 
        jLabel4.setText("Assignment System");

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        jLabel1.setText("Username");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); 
        jLabel2.setText("Password");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtUsername)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(77, 77, 77)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnStudentLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLecturerLogin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnPMLogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(169, 169, 169))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(207, 207, 207)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel4)))
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnAdminLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(btnPMLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btnLecturerLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(btnStudentLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void btnAdminLoginActionPerformed(java.awt.event.ActionEvent evt) {                                              
        String user = txtUsername.getText();
        String pass = String.valueOf(txtPassword.getPassword());
        
        if (user.equals("admin") && pass.equals("admin123")) {
            this.dispose();
            new AdminHome().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!");
        }
    }                                             

    private void btnPMLoginActionPerformed(java.awt.event.ActionEvent evt) {                                           
        String inputUser = txtUsername.getText();
        String inputPass = String.valueOf(txtPassword.getPassword());

        List<String[]> users = DBHelper.getUsersByRole("Company Supervisor", "");
        boolean found = false;

        for (String[] u : users) {
            String[] fullDetails = DBHelper.getUserById(u[0]);
            if (fullDetails != null && fullDetails[1].equals(inputUser) && fullDetails[2].equals(inputPass)) {
                this.dispose();
                new CompanySupervisorHome(u[0]).setVisible(true); 
                found = true;
                break;
            }
        }
        if (!found) JOptionPane.showMessageDialog(this, "Invalid Company Supervisor Credentials!");
    }                                          

    // --- FIX IS HERE ---
    private void btnLecturerLoginActionPerformed(java.awt.event.ActionEvent evt) {                                                 
        String inputUser = txtUsername.getText();
        String inputPass = String.valueOf(txtPassword.getPassword());

        List<String[]> users = DBHelper.getUsersByRole("Academic Supervisor", "");
        boolean found = false;

        for (String[] u : users) {
            String[] fullDetails = DBHelper.getUserById(u[0]);
            if (fullDetails != null && fullDetails[1].equals(inputUser) && fullDetails[2].equals(inputPass)) {
                this.dispose();
                // FIXED: Now correctly passes the ID (u[0]) to the Dashboard
                new AcademicSupervisorHome(u[0]).setVisible(true); 
                found = true;
                break;
            }
        }
        if (!found) JOptionPane.showMessageDialog(this, "Invalid Academic Supervisor Credentials!");
    }                                                

    private void btnStudentLoginActionPerformed(java.awt.event.ActionEvent evt) {                                                
        String inputUser = txtUsername.getText();
        String inputPass = String.valueOf(txtPassword.getPassword());

        List<String[]> users = DBHelper.getUsersByRole("Student", "");
        boolean found = false;
        for (String[] u : users) {
            String[] fullDetails = DBHelper.getUserById(u[0]);
            if (fullDetails != null && fullDetails[1].equals(inputUser) && fullDetails[2].equals(inputPass)) {
                found = true;
                this.dispose();
                new StudentHome(u[0], fullDetails[3]).setVisible(true); 
                break;
            }
        }
        if (!found) JOptionPane.showMessageDialog(this, "Invalid Student Credentials!");
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }

    private javax.swing.JButton btnAdminLogin;
    private javax.swing.JButton btnLecturerLogin;
    private javax.swing.JButton btnPMLogin;
    private javax.swing.JButton btnStudentLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
}