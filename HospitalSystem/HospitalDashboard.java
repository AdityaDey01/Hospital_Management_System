import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HospitalDashboard extends JFrame {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    JTextField txtId, txtName, txtSpec, txtContact, txtPass;
    JTable table;
    DefaultTableModel model;

    public HospitalDashboard(Connection connection) {
        this.con = connection;
        createUI();
        loadDoctors();
    }

    void createUI() {
        setTitle("Doctor Dashboard");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        txtId = new JTextField();
        txtName = new JTextField();
        txtSpec = new JTextField();
        txtContact = new JTextField();
        txtPass = new JTextField();

        panel.add(new JLabel("Doctor ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Specialization:"));
        panel.add(txtSpec);
        panel.add(new JLabel("Contact:"));
        panel.add(txtContact);
        panel.add(new JLabel("Password:"));
        panel.add(txtPass);

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);

        add(panel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Specialization", "Contact", "Password"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtId.setText(model.getValueAt(row, 0).toString());
                txtName.setText(model.getValueAt(row, 1).toString());
                txtSpec.setText(model.getValueAt(row, 2).toString());
                txtContact.setText(model.getValueAt(row, 3).toString());
                txtPass.setText(model.getValueAt(row, 4).toString());
            }
        });

        btnAdd.addActionListener(e -> addDoctor());
        btnUpdate.addActionListener(e -> updateDoctor());
        btnDelete.addActionListener(e -> deleteDoctor());

        setVisible(true);
    }

    void loadDoctors() {
        try {
            model.setRowCount(0);
            Statement stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM doctors");
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("specialization"),
                    rs.getString("contact"),
                    rs.getString("password")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Load Error: " + e.getMessage());
        }
    }

    void addDoctor() {
        try {
            pst = con.prepareStatement("INSERT INTO doctors VALUES (?, ?, ?, ?, ?)");
            pst.setInt(1, Integer.parseInt(txtId.getText()));
            pst.setString(2, txtName.getText());
            pst.setString(3, txtSpec.getText());
            pst.setString(4, txtContact.getText());
            pst.setString(5, txtPass.getText());
            pst.executeUpdate();
            loadDoctors();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Add Error: " + e.getMessage());
        }
    }

    void updateDoctor() {
        try {
            pst = con.prepareStatement("UPDATE doctors SET name=?, specialization=?, contact=?, password=? WHERE id=?");
            pst.setString(1, txtName.getText());
            pst.setString(2, txtSpec.getText());
            pst.setString(3, txtContact.getText());
            pst.setString(4, txtPass.getText());
            pst.setInt(5, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            loadDoctors();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Update Error: " + e.getMessage());
        }
    }

    void deleteDoctor() {
        try {
            pst = con.prepareStatement("DELETE FROM doctors WHERE id=?");
            pst.setInt(1, Integer.parseInt(txtId.getText()));
            pst.executeUpdate();
            loadDoctors();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete Error: " + e.getMessage());
        }
    }
}
