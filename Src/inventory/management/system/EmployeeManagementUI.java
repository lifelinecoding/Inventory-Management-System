package inventory.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeeManagementUI extends JFrame implements ActionListener {

    JPanel leftPanel, rightPanel;
    CardLayout cardLayout;
    boolean found;

    Button btnUpdate, btnAdd, btnShow, btnDelete, btnExit;

    // ✅ Table Model (global)
    DefaultTableModel tableModel;
    JTable table;

    // ! Function to check is the entered email is valid or not.
    boolean isValidEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(regex);
    }

    // ! Function to check is the entered email ibelongs to the organization or not.
    boolean isCompanyEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@bigbazaar\\.com$");
    }

    public EmployeeManagementUI() {
        setTitle("Employee Management");

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon("Src\\Icons\\Logo.png");
        setIconImage(image.getImage());
        setResizable(false);

        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(new Color(15, 23, 42));
        header.setPreferredSize(new Dimension(getWidth(), 70));
        JLabel title = new JLabel("Inventory Management System - Employee Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title, JPanel.CENTER_ALIGNMENT);
        add(header, BorderLayout.NORTH);

        // ! ===== LEFT PANEL =====
        leftPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        leftPanel.setBackground(new Color(30, 30, 60));
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        btnUpdate = new Button("Update Employee");
        btnUpdate.setBackground(new Color(42, 21, 127));
        btnAdd = new Button("Add Employee");
        btnAdd.setBackground(new Color(122, 21, 127));
        btnShow = new Button("Show Employee");
        btnShow.setBackground(new Color(42, 21, 127));
        btnDelete = new Button("Delete Employee");
        btnDelete.setBackground(new Color(122, 21, 127));
        btnExit = new Button("Exit/Back");
        btnExit.setBackground(new Color(42, 21, 127));

        Button[] buttons = { btnUpdate, btnAdd, btnShow, btnDelete, btnExit };

        for (Button btn : buttons) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.setForeground(Color.WHITE);
            btn.setPreferredSize(new Dimension(200, 0));
            btn.addActionListener(this);
            leftPanel.add(btn);
        }

        add(leftPanel, BorderLayout.WEST);

        // ! ===== RIGHT PANEL =====
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);

        rightPanel.add(createShowPanel(), "SHOW");
        rightPanel.add(createAddPanel(), "ADD");
        rightPanel.add(createUpdatePanel(), "UPDATE");
        rightPanel.add(createDeletePanel(), "DELETE");

        add(rightPanel, BorderLayout.CENTER);

        cardLayout.show(rightPanel, "SHOW");

        JPanel footer = new JPanel();
        footer.setBackground(Color.GRAY);
        JLabel footText = new JLabel("© 2026 Inventory Management System");
        footText.setForeground(Color.WHITE);
        footText.setFont(new Font("SansSerif", Font.BOLD, 16));
        footer.add(footText);
        footer.setPreferredSize(new Dimension(getWidth(), 30));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ! ===== SHOW PANEL =====
    private JPanel createShowPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(new Color(30, 30, 60));

        JLabel title = new JLabel("Show Employees");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {
                "ID", "Name", "Mobile", "Age", "Email",
                "Address", "Gender", "Department", "Designation", "Salary"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setForeground(Color.WHITE);
        table.setBackground(getForeground());
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(56, 189, 248));
        table.getTableHeader().setForeground(Color.WHITE);

        // table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(3).setPreferredWidth(20);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);
        table.getColumnModel().getColumn(6).setPreferredWidth(45);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(30, 41, 59));
        scrollPane.getViewport().setForeground(Color.WHITE);

        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        loadTableData(); // ✅ load DB data

        return panel;
    }

    // ! ===== LOAD DATA FROM DB =====
    private void loadTableData() {
        tableModel.setRowCount(0);
        try {
            Connection conn = DBConnection.getConnection();
            String loadEmployeeDataQuery = "SELECT * FROM EMPLOYEE;";
            PreparedStatement psStmt = conn.prepareStatement(loadEmployeeDataQuery);
            ResultSet res = psStmt.executeQuery();

            while (res.next()) {
                tableModel.addRow(new Object[] {
                        res.getString("EMPLOYEE_ID"),
                        res.getString("EMPLOYEE_NAME"),
                        res.getString("MOBILE_NO"),
                        res.getInt("AGE"),
                        res.getString("EMAIL"),
                        res.getString("ADDRESS"),
                        res.getString("GENDER"),
                        res.getString("DEPARTMENT"),
                        res.getString("DESIGNATION"),
                        res.getDouble("SALARY")
                });
            }

            conn.close();
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ! ================ ADD PANEL ======================
    private JPanel createAddPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Add Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(15, 23, 42));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(10, 2, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JTextField txtId = new JTextField("EMP-BGB-0001");
        JTextField txtName = new JTextField();
        JTextField txtMobile = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();

        String[] genderOptions = { "Male", "Female", "Other" };
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);

        JTextField txtDepartment = new JTextField();
        JTextField txtDesignation = new JTextField();
        JTextField txtSalary = new JTextField();

        JTextField[] fields = { txtId, txtName, txtMobile, txtAge, txtEmail,
                txtAddress, txtDepartment, txtDesignation, txtSalary };

        for (JTextField field : fields) {
            field.setFont(new Font("SansSerif", Font.BOLD, 18));
            field.setForeground(Color.BLACK);
            field.setBackground(Color.WHITE);

        }

        txtId.setForeground(Color.GRAY);

        JLabel idLbl = new JLabel("Employee ID: ");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        idLbl.setBackground(Color.WHITE);

        JLabel nameLbl = new JLabel("Name: ");
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setBackground(Color.WHITE);

        JLabel mobileLbl = new JLabel("Mobile No: ");
        mobileLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        mobileLbl.setForeground(Color.WHITE);
        mobileLbl.setBackground(Color.WHITE);

        JLabel ageLbl = new JLabel("Age: ");
        ageLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        ageLbl.setForeground(Color.WHITE);
        ageLbl.setBackground(Color.WHITE);

        JLabel emailLbl = new JLabel("Email: ");
        emailLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        emailLbl.setForeground(Color.WHITE);
        emailLbl.setBackground(Color.WHITE);

        JLabel addressLbl = new JLabel("Address: ");
        addressLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        addressLbl.setForeground(Color.WHITE);
        addressLbl.setBackground(Color.WHITE);

        JLabel genderLbl = new JLabel("Gender:");
        genderLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        genderLbl.setForeground(Color.WHITE);
        genderLbl.setBackground(Color.WHITE);

        JLabel departmentLbl = new JLabel("Department: ");
        departmentLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        departmentLbl.setForeground(Color.WHITE);
        departmentLbl.setBackground(Color.WHITE);

        JLabel designationLbl = new JLabel("Designation");
        designationLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        designationLbl.setForeground(Color.WHITE);
        designationLbl.setBackground(Color.WHITE);

        JLabel salaryLbl = new JLabel("Salary");
        salaryLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        salaryLbl.setForeground(Color.WHITE);
        salaryLbl.setBackground(Color.WHITE);

        placeHolder(txtId); // ! Setting the placeholder

        form.add(idLbl);
        form.add(txtId);
        form.add(nameLbl);
        form.add(txtName);
        form.add(mobileLbl);
        form.add(txtMobile);
        form.add(ageLbl);
        form.add(txtAge);
        form.add(emailLbl);
        form.add(txtEmail);
        form.add(addressLbl);
        form.add(txtAddress);
        form.add(genderLbl);
        form.add(genderBox);
        form.add(departmentLbl);
        form.add(txtDepartment);
        form.add(designationLbl);
        form.add(txtDesignation);
        form.add(salaryLbl);
        form.add(txtSalary);

        genderBox.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(null);
        btnPanel.setBackground(new Color(15, 23, 42));
        btnPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 50));

        Button saveBtn = new Button("Save");
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setBackground(new Color(42, 21, 127));
        saveBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        saveBtn.setBounds(50, 7, 105, 35);

        Button resetBtn = new Button("Reset");
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBackground(new Color(122, 21, 127));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetBtn.setBounds(980, 7, 105, 35);

        btnPanel.add(saveBtn, JPanel.LEFT_ALIGNMENT);
        btnPanel.add(resetBtn, JPanel.RIGHT_ALIGNMENT);
        btnPanel.setVisible(true);

        // ! ----------------- SAVE TO DATABASE -------------------
        saveBtn.addActionListener(e -> {

            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String mobileNo = txtMobile.getText().trim();
            String ageStr = txtAge.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();
            String gender = genderBox.getSelectedItem().toString();
            String department = txtDepartment.getText().trim();
            String designation = txtDesignation.getText().trim();
            String salaryStr = txtSalary.getText().trim();
            String password = generatePassword();

            int age = 0;
            double salary = 0;
            try {
                age = Integer.parseInt(ageStr);
                salary = Double.parseDouble(salaryStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Age or Salary Should be number", "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (id.isEmpty() || name.isEmpty() || mobileNo.isEmpty() || ageStr.isEmpty() || email.isEmpty()
                    || address.isEmpty() || gender.isEmpty() || department.isEmpty() || designation.isEmpty()
                    || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all the fields.", "Empty fields",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (id.length() != 12 || !id.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$") || !id.startsWith("EMP-BGB-")) {
                JOptionPane.showMessageDialog(panel, "Invalid ID Format", "Invalid ID",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (age < 0) {
                JOptionPane.showMessageDialog(panel, "Age should be positive number", "Invalid Age",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (salary < 0) {
                JOptionPane.showMessageDialog(panel, "Salary should be positive number", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mobileNo.length() != 10 || !mobileNo.matches("\\d+")) {
                JOptionPane.showMessageDialog(panel, "Invalid Mobile Number. It should Contain 10 digits.",
                        "Invalid mobile number",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Invalid Email Format", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isCompanyEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Please provide company email to employees.", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                Connection conn = DBConnection.getConnection();
                String addEmployeeQuery = "INSERT INTO EMPLOYEE (EMPLOYEE_ID, EMPLOYEE_NAME, MOBILE_NO, AGE, EMAIL, ADDRESS, GENDER, DEPARTMENT, DESIGNATION, SALARY, PASSWORD) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement psStmt = conn.prepareStatement(addEmployeeQuery);
                psStmt.setString(1, id);
                psStmt.setString(2, name);
                psStmt.setString(3, mobileNo);
                psStmt.setInt(4, age);
                psStmt.setString(5, email);
                psStmt.setString(6, address);
                psStmt.setString(7, gender);
                psStmt.setString(8, department);
                psStmt.setString(9, designation);
                psStmt.setDouble(10, salary);
                psStmt.setString(11, password);

                int affectedRows = psStmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "Employee Created Successfuly\nUsername: " + email + "\nPassword: " + password + "",
                            "Employee Creation", JOptionPane.INFORMATION_MESSAGE);
                }

                txtId.setText("EMP-BGB-0001");
                txtName.setText("");
                txtMobile.setText("");
                txtAge.setText("");
                txtEmail.setText("");
                txtAddress.setText("");
                txtDepartment.setText("");
                txtDesignation.setText("");
                txtSalary.setText("");
                genderBox.setSelectedIndex(0);

                placeHolder(txtId);

                loadTableData();

                conn.close();
                return;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ! -------------------- RESET ----------------------------
        resetBtn.addActionListener(e -> {
            txtId.setText("EMP-BGB-0001");
            txtName.setText("");
            txtMobile.setText("");
            txtAge.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            txtDepartment.setText("");
            txtDesignation.setText("");
            txtSalary.setText("");
            genderBox.setSelectedIndex(0);

            placeHolder(txtId);
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ! ============== UPDATE PANEL ====================
    private JPanel createUpdatePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Update Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(10, 2, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JTextField txtId = new JTextField("EMP-BGB-0001");
        JTextField txtName = new JTextField();
        JTextField txtMobile = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();
        String[] genderOptions = { "Male", "Female", "Other" };
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);
        JTextField txtDepartment = new JTextField();
        JTextField txtDesignation = new JTextField();
        JTextField txtSalary = new JTextField();

        JTextField[] fields = { txtId, txtName, txtMobile, txtAge, txtEmail,
                txtAddress, txtDepartment, txtDesignation, txtSalary };

        for (JTextField field : fields) {
            field.setFont(new Font("SansSerif", Font.BOLD, 18));
            field.setForeground(Color.BLACK);
            field.setBackground(Color.WHITE);
        }

        txtId.setForeground(Color.GRAY);

        JLabel idLbl = new JLabel("Search By Employee ID: ");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        idLbl.setBackground(Color.WHITE);

        JLabel nameLbl = new JLabel("Name: ");
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setBackground(Color.WHITE);

        JLabel mobileLbl = new JLabel("Mobile No: ");
        mobileLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        mobileLbl.setForeground(Color.WHITE);
        mobileLbl.setBackground(Color.WHITE);

        JLabel ageLbl = new JLabel("Age: ");
        ageLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        ageLbl.setForeground(Color.WHITE);
        ageLbl.setBackground(Color.WHITE);

        JLabel emailLbl = new JLabel("Email: ");
        emailLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        emailLbl.setForeground(Color.WHITE);
        emailLbl.setBackground(Color.WHITE);

        JLabel addressLbl = new JLabel("Address: ");
        addressLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        addressLbl.setForeground(Color.WHITE);
        addressLbl.setBackground(Color.WHITE);

        JLabel genderLbl = new JLabel("Gender:");
        genderLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        genderLbl.setForeground(Color.WHITE);
        genderLbl.setBackground(Color.WHITE);

        JLabel departmentLbl = new JLabel("Department: ");
        departmentLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        departmentLbl.setForeground(Color.WHITE);
        departmentLbl.setBackground(Color.WHITE);

        JLabel designationLbl = new JLabel("Designation");
        designationLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        designationLbl.setForeground(Color.WHITE);
        designationLbl.setBackground(Color.WHITE);

        JLabel salaryLbl = new JLabel("Salary");
        salaryLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        salaryLbl.setForeground(Color.WHITE);
        salaryLbl.setBackground(Color.WHITE);

        genderBox.setFont(new Font("SansSerif", Font.BOLD, 18));

        placeHolder(txtId); // ! Setting the placeholder

        form.add(idLbl);
        form.add(txtId);
        form.add(nameLbl);
        form.add(txtName);
        form.add(mobileLbl);
        form.add(txtMobile);
        form.add(ageLbl);
        form.add(txtAge);
        form.add(emailLbl);
        form.add(txtEmail);
        form.add(addressLbl);
        form.add(txtAddress);
        form.add(genderLbl);
        form.add(genderBox);
        form.add(departmentLbl);
        form.add(txtDepartment);
        form.add(designationLbl);
        form.add(txtDesignation);
        form.add(salaryLbl);
        form.add(txtSalary);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(null);
        btnPanel.setBackground(new Color(15, 23, 42));
        btnPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 50));

        Button loadBtn = new Button("Search");
        loadBtn.setForeground(Color.WHITE);
        loadBtn.setBackground(new Color(42, 21, 127));
        loadBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        loadBtn.setBounds(50, 7, 105, 35);

        Button updateBtn = new Button("Update");
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(new Color(222, 21, 127));
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        updateBtn.setBounds(495, 7, 105, 35);

        Button resetBtn = new Button("Reset");
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBackground(new Color(122, 21, 127));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetBtn.setBounds(980, 7, 105, 35);

        btnPanel.add(loadBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(resetBtn);

        final int[] foundIndex = new int[] { -1 };

        loadBtn.addActionListener(e -> {

            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter an Employee ID to search.", "Input needed",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (id.length() != 12 || !id.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$") || !id.startsWith("EMP-BGB-")) {
                JOptionPane.showMessageDialog(panel, "Invalid ID Format", "Invalid ID",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tableModel == null || tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "No employee data available. Click 'Show Employee' to load data.",
                        "No data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object val = tableModel.getValueAt(i, 0);
                if (val != null && id.equals(val.toString())) {
                    // populate fields from model columns (match columns in show panel)
                    txtName.setText(safeToString(tableModel.getValueAt(i, 1)));
                    txtMobile.setText(safeToString(tableModel.getValueAt(i, 2)));
                    txtAge.setText(safeToString(tableModel.getValueAt(i, 3)));
                    txtEmail.setText(safeToString(tableModel.getValueAt(i, 4)));
                    txtAddress.setText(safeToString(tableModel.getValueAt(i, 5)));
                    genderBox.setSelectedItem(safeToString(tableModel.getValueAt(i, 6)));
                    txtDepartment.setText(safeToString(tableModel.getValueAt(i, 7)));
                    txtDesignation.setText(safeToString(tableModel.getValueAt(i, 8)));
                    txtSalary.setText(safeToString(tableModel.getValueAt(i, 9)));
                    foundIndex[0] = i;
                    found = true;
                    break;
                }
            }
        });

        updateBtn.addActionListener(e -> {

            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String mobileNo = txtMobile.getText().trim();
            String ageStr = txtAge.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();
            String gender = genderBox.getSelectedItem().toString();
            String department = txtDepartment.getText().trim();
            String designation = txtDesignation.getText().trim();
            String salaryStr = txtSalary.getText().trim();

            int age = 0;
            double salary = 0;
            try {
                age = Integer.parseInt(ageStr);
                salary = Double.parseDouble(salaryStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Age or Salary Should be number", "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (id.isEmpty() || name.isEmpty() || mobileNo.isEmpty() || ageStr.isEmpty() || email.isEmpty()
                    || address.isEmpty() || gender.isEmpty() || department.isEmpty() || designation.isEmpty()
                    || salaryStr.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all the fields.", "Empty fields",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (id.length() != 12 || !id.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$") || !id.startsWith("EMP-BGB-")) {
                JOptionPane.showMessageDialog(panel, "Invalid ID Format", "Invalid ID",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (age < 0) {
                JOptionPane.showMessageDialog(panel, "Age should be positive number", "Invalid Age",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (salary < 0) {
                JOptionPane.showMessageDialog(panel, "Salary should be positive number", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mobileNo.length() != 10 || !mobileNo.matches("\\d+")) {
                JOptionPane.showMessageDialog(panel, "Invalid Mobile Number. It should Contain 10 digits.",
                        "Invalid mobile number",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Invalid Email Format", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isCompanyEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Please provide company email to employees.", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                Connection conn = DBConnection.getConnection();
                String updateEmployeeQuery = "UPDATE EMPLOYEE SET EMPLOYEE_NAME = ?, MOBILE_NO = ?, AGE = ?, EMAIL = ?, ADDRESS = ?, GENDER = ?, DEPARTMENT = ?, DESIGNATION = ?, SALARY = ? WHERE EMPLOYEE_ID = ?";
                PreparedStatement psStmt = conn.prepareStatement(updateEmployeeQuery);
                psStmt.setString(1, name);
                psStmt.setString(2, mobileNo);
                psStmt.setInt(3, age);
                psStmt.setString(4, email);
                psStmt.setString(5, address);
                psStmt.setString(6, gender);
                psStmt.setString(7, department);
                psStmt.setString(8, designation);
                psStmt.setDouble(9, salary);
                psStmt.setString(10, id);

                int affectedRows = psStmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(panel,
                            "Employee Updated Successfuly\n",
                            "Employee Creation", JOptionPane.INFORMATION_MESSAGE);
                    loadTableData();
                }

                conn.close();

                txtId.setText("EMP-BGB-0001");
                txtName.setText("");
                txtMobile.setText("");
                txtAge.setText("");
                txtEmail.setText("");
                txtAddress.setText("");
                txtDepartment.setText("");
                txtDesignation.setText("");
                txtSalary.setText("");
                genderBox.setSelectedIndex(0);

                placeHolder(txtId);

                return;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        resetBtn.addActionListener(e -> {
            txtId.setText("EMP-BGB-0001");
            txtName.setText("");
            txtMobile.setText("");
            txtAge.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            txtDepartment.setText("");
            txtDesignation.setText("");
            txtSalary.setText("");
            genderBox.setSelectedIndex(0);

            placeHolder(txtId);
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ! ====================== DELETE PANEL ====================
    private JPanel createDeletePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Delete Employee");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        title.setForeground(Color.WHITE);

        // Form: show search row + employee details (read-only)
        JPanel form = new JPanel(new GridLayout(10, 2, 10, 25));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JLabel idLbl = new JLabel("Employee ID:");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        JTextField txtId = new JTextField("EMP-BGB-0001");
        txtId.setFont(new Font("SansSerif", Font.BOLD, 18));
        txtId.setBackground(Color.WHITE);
        txtId.setForeground(Color.GRAY);

        Button searchBtn = new Button("Search");
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setBackground(new Color(42, 21, 127));
        searchBtn.setFont(new Font("SansSerif", Font.BOLD, 18));

        // Empty placeholder so the Search button aligns in the grid
        JLabel placeholder = new JLabel("");

        JTextField txtName = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtGender = new JTextField();
        JTextField txtDepartment = new JTextField();
        JTextField txtDesignation = new JTextField();

        JTextField[] detailFields = { txtName, txtAge, txtEmail,
                txtAddress, txtGender, txtDepartment, txtDesignation };

        for (JTextField f : detailFields) {
            f.setFont(new Font("SansSerif", Font.BOLD, 18));
            f.setForeground(Color.BLACK);
            f.setBackground(Color.WHITE);
            f.setEditable(false);
        }

        JLabel nameLbl = new JLabel("Name:");
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        nameLbl.setForeground(Color.WHITE);

        JLabel ageLbl = new JLabel("Age:");
        ageLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        ageLbl.setForeground(Color.WHITE);

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        emailLbl.setForeground(Color.WHITE);

        JLabel addressLbl = new JLabel("Address:");
        addressLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        addressLbl.setForeground(Color.WHITE);

        JLabel genderLbl = new JLabel("Gender:");
        genderLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        genderLbl.setForeground(Color.WHITE);

        JLabel departmentLbl = new JLabel("Department:");
        departmentLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        departmentLbl.setForeground(Color.WHITE);

        JLabel designationLbl = new JLabel("Designation:");
        designationLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        designationLbl.setForeground(Color.WHITE);

        placeHolder(txtId); // ! Setting the placeholder

        // Add components to form (search row first)
        form.add(idLbl);
        form.add(txtId);
        form.add(placeholder);
        form.add(searchBtn);

        form.add(nameLbl);
        form.add(txtName);
        form.add(ageLbl);
        form.add(txtAge);
        form.add(emailLbl);
        form.add(txtEmail);
        form.add(addressLbl);
        form.add(txtAddress);
        form.add(genderLbl);
        form.add(txtGender);
        form.add(departmentLbl);
        form.add(txtDepartment);
        form.add(designationLbl);
        form.add(txtDesignation);

        // ! Button panel (consistent with other panels)
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(null);
        btnPanel.setBackground(new Color(15, 23, 42));
        btnPanel.setPreferredSize(new Dimension(rightPanel.getWidth(), 50));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(new Color(42, 21, 127));
        deleteBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        deleteBtn.setBounds(50, 7, 105, 35);

        Button resetBtn = new Button("Reset");
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setBackground(new Color(122, 21, 127));
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        resetBtn.setBounds(980, 7, 105, 35);

        btnPanel.add(deleteBtn);
        btnPanel.add(resetBtn);

        // Keep track of the index found in the table model
        final int[] foundIndex = new int[] { -1 };

        searchBtn.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter an Employee ID to search.", "Input needed",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (id.length() != 12 || !id.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$") || !id.startsWith("EMP-BGB-")) {
                JOptionPane.showMessageDialog(panel, "Invalid ID Format", "Invalid ID",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (tableModel == null || tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "No employee data available. Click 'Show Employee' to load data.",
                        "No data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            found = false;
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Object val = tableModel.getValueAt(i, 0);
                if (val != null && id.equals(val.toString())) {
                    // populate fields from model columns (match columns in show panel)
                    txtName.setText(safeToString(tableModel.getValueAt(i, 1)));
                    txtAge.setText(safeToString(tableModel.getValueAt(i, 3)));
                    txtEmail.setText(safeToString(tableModel.getValueAt(i, 4)));
                    txtAddress.setText(safeToString(tableModel.getValueAt(i, 5)));
                    txtGender.setText(safeToString(tableModel.getValueAt(i, 6)));
                    txtDepartment.setText(safeToString(tableModel.getValueAt(i, 7)));
                    txtDesignation.setText(safeToString(tableModel.getValueAt(i, 8)));
                    foundIndex[0] = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(panel, "Employee with ID '" + id + "' not found.", "Not found",
                        JOptionPane.INFORMATION_MESSAGE);
                foundIndex[0] = -1;
                clearDetailFields(detailFields);
            }
        });

        deleteBtn.addActionListener(e -> {
            String id = txtId.getText().trim();
            if (foundIndex[0] == -1) {
                JOptionPane.showMessageDialog(panel, "No employee selected to delete. Please search first.",
                        "Nothing to delete", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (id.length() != 12 || !id.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$") || !id.startsWith("EMP-BGB-")) {
                JOptionPane.showMessageDialog(panel, "Invalid ID Format", "Invalid ID",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete this employee?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {

                try {

                    Connection conn = DBConnection.getConnection();
                    String deleteEmployeeQuery = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
                    PreparedStatement psStmt = conn.prepareStatement(deleteEmployeeQuery);
                    psStmt.setString(1, id);

                    int affectedRow = psStmt.executeUpdate();

                    if (affectedRow > 0) {
                        JOptionPane.showMessageDialog(panel, "Employee Deleted Successfully", "Delete Employee",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadTableData();
                    }
                    conn.close();
                    foundIndex[0] = -1;
                    txtId.setText("EMP-BGB-0001");
                    clearDetailFields(detailFields);
                    return;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else if(confirm == JOptionPane.NO_OPTION) {
                return;
            }
        });

        resetBtn.addActionListener(e -> {
            txtId.setText("");
            foundIndex[0] = -1;
            clearDetailFields(detailFields);
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(form, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ! -------- small helpers used by the delete panel --------------
    private String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }

    private void clearDetailFields(JTextField[] fields) {
        for (JTextField f : fields)
            f.setText("");
    }

    // ! ==================== SIMPLE PANEL ======================
    // private JPanel simplePanel(String text) {
    // JPanel panel = new JPanel();
    // JLabel label = new JLabel(text);
    // label.setFont(new Font("SansSerif", Font.BOLD, 26));
    // panel.add(label);
    // return panel;
    // }

    // ! ================== BUTTON ACTION ========================
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnShow) {
            loadTableData(); // refresh when clicked
            cardLayout.show(rightPanel, "SHOW");
        } else if (e.getSource() == btnAdd) {
            cardLayout.show(rightPanel, "ADD");
        } else if (e.getSource() == btnUpdate) {
            cardLayout.show(rightPanel, "UPDATE");
        } else if (e.getSource() == btnDelete) {
            cardLayout.show(rightPanel, "DELETE");
        } else if (e.getSource() == btnExit) {
            dispose();
            new AdminFeatures();
        }
    }

    // ! ================== Random 10 digit password generator

    public static String generatePassword() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }

    private void placeHolder(JTextField txtId) {
        txtId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtId.getText().equals("EMP-BGB-0001")) {
                    txtId.setText("");
                    txtId.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtId.getText().isEmpty()) {
                    txtId.setForeground(Color.GRAY);
                    txtId.setText("EMP-BGB-0001");
                }
            }
        });
    }

    public static void main(String[] args) {
        new EmployeeManagementUI();
    }
}