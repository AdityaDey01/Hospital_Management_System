import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class DoctorLogin extends JFrame {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    JTextField txtId;
    JPasswordField txtPassword;
    JButton btnLogin;

    public DoctorLogin() {
        connectDB();
        createUI();
        animateWindow();
    }

    void connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_db", "root", "your_password");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "DB Connection Failed: " + e.getMessage());
        }
    }

    void createUI() {
        setTitle("Doctor Login");
        setSize(300, 250);
        setLayout(new GridLayout(5, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        txtId = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");

        add(new JLabel("Doctor ID:"));
        add(txtId);
        add(new JLabel("Password:"));
        add(txtPassword);
        add(btnLogin);

        btnLogin.addActionListener(e -> loginDoctor());

        setOpacity(0f);
        setVisible(true);
    }

    void loginDoctor() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String pass = new String(txtPassword.getPassword());

            pst = con.prepareStatement("SELECT * FROM doctors WHERE id = ? AND password = ?");
            pst.setInt(1, id);
            pst.setString(2, pass);
            rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new HospitalDashboard(con);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid ID or Password!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login Error: " + e.getMessage());
        }
    }

    void animateWindow() {
        Timer timer = new Timer(20, null);
        timer.addActionListener(new ActionListener() {
            float opacity = 0f;
            public void actionPerformed(ActionEvent e) {
                opacity += 0.05f;
                if (opacity >= 1f) {
                    opacity = 1f;
                    timer.stop();
                }
                setOpacity(opacity);
            }
        });
        timer.start();
    }
}
