package inventory.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
// import java.sql.*; JDBC

public class SupplierManagement extends JFrame implements ActionListener {

    JPanel leftPanel, rightPanel;
    CardLayout cardLayout;
    boolean found;

    Button btnUpdate, btnAdd, btnShow, btnDelete, btnExit;

    // TODO: Database connection
    // Connection con = DBConnection.getConnection();


    //Table Model (global)
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

    public SupplierManagement() {
        setTitle("Supplier Management");

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
        JLabel title = new JLabel("Inventory Management System - Supplier Management");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title, JPanel.CENTER_ALIGNMENT);
        add(header, BorderLayout.NORTH);

        // ! ===== LEFT PANEL =====
        leftPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        leftPanel.setBackground(new Color(30, 30, 60));
        leftPanel.setPreferredSize(new Dimension(300, getHeight()));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        btnUpdate = new Button("Update Supplier");
        btnUpdate.setBackground(new Color(42, 21, 127));
        btnAdd = new Button("Add Supplier");
        btnAdd.setBackground(new Color(122, 21, 127));
        btnShow = new Button("Show Supplier");
        btnShow.setBackground(new Color(42, 21, 127));
        btnDelete = new Button("Delete Supplier");
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

        JLabel title = new JLabel("Show Supplier");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"ID", "Name", "Mobile", "Age", "Email","Address", "Gender" };
                
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(56, 189, 248));
        table.getTableHeader().setForeground(Color.WHITE);

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
        // TODO : Load the data from database and store into Table.
    }

    // ! ================ ADD PANEL ======================
    private JPanel createAddPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel title = new JLabel("Add Supplier");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBackground(new Color(15, 23, 42));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(10, 2, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtMobile = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();

        String[] genderOptions = { "Male", "Female", "Other" };
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);

        JTextField[] fields = { txtId, txtName, txtMobile, txtAge, txtEmail,
                txtAddress};

        for (JTextField field : fields) {
            field.setFont(new Font("SansSerif", Font.BOLD, 18));
            field.setForeground(Color.BLACK);
            field.setBackground(Color.WHITE);

        }

        JLabel idLbl = new JLabel("Supplier ID: ");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        idLbl.setBackground(Color.WHITE);

        JLabel nameLbl = new JLabel("Supplier Name: ");
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

            int age = 0;
            try {
                age = Integer.parseInt(ageStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Age Should be number", "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (id.isEmpty() || name.isEmpty() || mobileNo.isEmpty() || ageStr.isEmpty() || email.isEmpty()
                    || address.isEmpty() || gender.isEmpty() ) {
                JOptionPane.showMessageDialog(panel, "Please fill all the fields.", "Empty fields",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (age < 0) {
                JOptionPane.showMessageDialog(panel, "Age should be positive number", "Invalid Age",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (mobileNo.length() < 10 || mobileNo.length() > 10) {
                JOptionPane.showMessageDialog(panel, "Invalid Mobile Number. It should Contain 10 digits.",
                        "Invalid mobile number",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Invalid Email Format", "Invalid Email",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!isCompanyEmail(email)) {
                JOptionPane.showMessageDialog(panel, "Please provide company email to Supplier.", "Invalid Email",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // TODO : Insert into database
        });

        // ! -------------------- RESET ----------------------------
        resetBtn.addActionListener(e -> {
            txtId.setText("");
            txtName.setText("");
            txtMobile.setText("");
            txtAge.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            genderBox.setSelectedIndex(0);
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

        JLabel title = new JLabel("Update Supplier");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel form = new JPanel(new GridLayout(10, 2, 20, 20));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JTextField txtId = new JTextField();
        JTextField txtName = new JTextField();
        JTextField txtMobile = new JTextField();
        JTextField txtAge = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();
        String[] genderOptions = { "Male", "Female", "Other" };
        JComboBox<String> genderBox = new JComboBox<>(genderOptions);

        JTextField[] fields = { txtId, txtName, txtMobile, txtAge, txtEmail,
                txtAddress};

        for (JTextField field : fields) {
            field.setFont(new Font("SansSerif", Font.BOLD, 18));
            field.setForeground(Color.BLACK);
            field.setBackground(Color.WHITE);
        }

        JLabel idLbl = new JLabel("Search By Supplier ID: ");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        idLbl.setBackground(Color.WHITE);

        JLabel nameLbl = new JLabel("Supplier Name: ");
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

        genderBox.setFont(new Font("SansSerif", Font.BOLD, 18));

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
            // TODO : Load from the Table if user exist.

            String id = txtId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Enter an Supplier ID to search.", "Input needed",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (tableModel == null || tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "No Supplier data available. Click 'Show Supplier' to load data.",
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

            int age = 0;
            try {
                age = Integer.parseInt(ageStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Age Should be number", "Invalid Format",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (id.isEmpty() || name.isEmpty() || mobileNo.isEmpty() || ageStr.isEmpty() || email.isEmpty()
                    || address.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill all the fields.", "Empty fields",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (age < 0) {
                JOptionPane.showMessageDialog(panel, "Age should be positive number", "Invalid Age",
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
                JOptionPane.showMessageDialog(panel, "Please provide company email to Supplier.", "Invalid Salary",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            // TODO : update database logic
        });

        resetBtn.addActionListener(e -> {
            txtId.setText("");
            txtName.setText("");
            txtMobile.setText("");
            txtAge.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            genderBox.setSelectedIndex(0);
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

        JLabel title = new JLabel("Delete Supplier");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        title.setForeground(Color.WHITE);

        // Form: show search row + employee details (read-only)
        JPanel form = new JPanel(new GridLayout(10, 2, 10, 25));
        form.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
        form.setBackground(new Color(30, 41, 59));

        JLabel idLbl = new JLabel("Supplier ID:");
        idLbl.setFont(new Font("SansSerif", Font.BOLD, 22));
        idLbl.setForeground(Color.WHITE);
        JTextField txtId = new JTextField();
        txtId.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtId.setBackground(Color.WHITE);

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

        JTextField[] detailFields = { txtName, txtAge, txtEmail,
                txtAddress, txtGender};

        for (JTextField f : detailFields) {
            f.setFont(new Font("SansSerif", Font.BOLD, 18));
            f.setForeground(Color.BLACK);
            f.setBackground(Color.WHITE);
            f.setEditable(false);
        }

        JLabel nameLbl = new JLabel("Supplier Name:");
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

        // Button panel (consistent with other panels)
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
                JOptionPane.showMessageDialog(panel, "Enter an Supplier ID to search.", "Input needed",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (tableModel == null || tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(panel, "No Supplier data available. Click 'Show Supplier' to load data.",
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
                    foundIndex[0] = i;
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(panel, "Supplier with ID '" + id + "' not found.", "Not found",
                        JOptionPane.INFORMATION_MESSAGE);
                foundIndex[0] = -1;
                clearDetailFields(detailFields);
            }
        });

        deleteBtn.addActionListener(e -> {
            if (foundIndex[0] == -1) {
                JOptionPane.showMessageDialog(panel, "No Supplier selected to delete. Please search first.",
                        "Nothing to delete", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete this Supplier?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (tableModel != null && foundIndex[0] >= 0 && foundIndex[0] < tableModel.getRowCount()) {
                    tableModel.removeRow(foundIndex[0]);
                    JOptionPane.showMessageDialog(panel, "Supplier deleted.", "Deleted",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                foundIndex[0] = -1;
                txtId.setText("");
                clearDetailFields(detailFields);
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

    public static void main(String[] args) {
        new SupplierManagement();
    }
}