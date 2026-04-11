package inventory.management.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Employee-facing sales form (New Sale only).
 * Sales history is visible in admin purchases section.
 */
public class EmployeeSalesPanel extends JPanel {

    private JTabbedPane mainTabs;
    JLabel heading;
    String Username, Password;
    boolean found;
    int availableQuantity = 0;

    private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_CARD = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(56, 189, 248);
    private static final Color ACCENT2 = new Color(99, 102, 241);
    private static final Color TEXT_WHITE = new Color(241, 245, 249);
    private static final Color INPUT_BG = new Color(51, 65, 85);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.BOLD, 15);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 16);

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");

    private final DefaultTableModel productDetailsModel;

    private static final String[] PRODUCT_DETAILS_COLS = {
            "Product ID", "Product ", "Category", "Description", "Qty", "Purchase Price", "Selling Price",
    };

    private static final Color TABLE_HEADER_BLUE = new Color(37, 99, 235);

    public EmployeeSalesPanel(String Username, String Password) {

        this.Username = Username;
        this.Password = Password;

        productDetailsModel = createNonEditableTableModel(PRODUCT_DETAILS_COLS);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        heading = new JLabel("New Sales", SwingConstants.CENTER);
        heading.setFont(FONT_TITLE);
        heading.setForeground(ACCENT);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(heading, BorderLayout.NORTH);

        mainTabs = new JTabbedPane(JTabbedPane.TOP);
        mainTabs.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainTabs.setBackground(BG_CARD);
        mainTabs.setForeground(TEXT_WHITE);

        mainTabs.addTab("New Sale", wrapTab(buildNewSaleTab()));
        mainTabs.addTab("Register Customer", wrapTab(buildNewCustomerTab()));
        mainTabs.addTab("Change Password", wrapTab(buildChangePasswordTab()));
        mainTabs.addTab("Product Details", wrapTab(buildProductDetailsTab()));

        mainTabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane source = (JTabbedPane) e.getSource();
                int selectedIndex = source.getSelectedIndex();
                // System.out.println(selectedIndex); Debugging
                if (selectedIndex == 0) {
                    heading.setText("New Sales");
                    return;
                } else if (selectedIndex == 1) {
                    heading.setText("Register New Customer");
                    return;
                } else if (selectedIndex == 2) {
                    heading.setText("Change Password");
                    return;
                } else if (selectedIndex == 3) {
                    heading.setText("Product Details");
                    return;
                } else {
                    return;
                }

            }
        });

        add(mainTabs, BorderLayout.CENTER);
    }

    private static DefaultTableModel createNonEditableTableModel(String[] columnNames) {
        return new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JPanel wrapTab(JPanel inner) {
        JPanel shell = new JPanel(new BorderLayout());
        shell.setBackground(BG_DARK);
        shell.add(inner, BorderLayout.CENTER);
        return shell;
    }

    private JPanel buildNewSaleTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblCustomerId = makeFormLabel("Customer ID");
        JLabel lblCustomer = makeFormLabel("Customer Name");
        JLabel lblCategory = makeFormLabel("Category");
        JLabel lblProduct = makeFormLabel("Product ID");
        JLabel lblQty = makeFormLabel("Quantity");
        JLabel lblPrice = makeFormLabel("Selling Price");
        JLabel lblDate = makeFormLabel("Date (dd/mm/yyyy)");

        JTextField tfCustomerId = makeField();
        JTextField tfCustomer = makeField();
        tfCustomer.setEditable(false);
        JComboBox<String> cbCategory = makeCombo(ProductCatalog.categoryComboItems());
        JComboBox<String> cbProductId = makeCombo(new String[] { ProductCatalog.PLACEHOLDER });

        JTextField tfQty = makeField();
        JTextField tfPrice = makeField();
        tfPrice.setEditable(false);
        JTextField tfDate = makeField();

        cbCategory.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ProductCatalog.refreshProductCombo(cbProductId, cbCategory.getSelectedItem());
            }
        });

        tfCustomerId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String customerId = tfCustomerId.getText().trim();
                    if (customerId.length() != 12 || !customerId.startsWith("CUS-BGB-")
                            || !customerId.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$")) {

                        JOptionPane.showMessageDialog(panel, "Invalid Customer ID", "Validation",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    try {
                        Connection conn = DBConnection.getConnection();
                        String getSupplierNameQuery = "SELECT CUSTOMER_NAME FROM CUSTOMER WHERE CUSTOMER_ID = ?";
                        PreparedStatement psStmt = conn.prepareStatement(getSupplierNameQuery);
                        psStmt.setString(1, customerId);

                        ResultSet res = psStmt.executeQuery();
                        if (res.next()) {
                            tfCustomer.setText(res.getString("CUSTOMER_NAME").trim());
                            return;

                        } else {
                            JOptionPane.showMessageDialog(panel, "Customer does not exist. Please register first.",
                                    "Not found",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        final int[] foundIndex = new int[] { -1 };

        cbProductId.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String id = cbProductId.getSelectedItem().toString().trim();
                if (id.equals("— Select —")) {
                    return;
                }

                if (productDetailsModel == null || productDetailsModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(panel,
                            "No Product data available. Load or list the product first.",
                            "No data", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                found = false;
                for (int i = 0; i < productDetailsModel.getRowCount(); i++) {
                    Object val = productDetailsModel.getValueAt(i, 0);
                    if (val != null && id.equals(val.toString())) {
                        // populate fields from model columns (match columns in show panel)
                        tfPrice.setText(safeToString(productDetailsModel.getValueAt(i, 6)));
                        Object qtyObj = productDetailsModel.getValueAt(i, 4);
                        availableQuantity = Integer.parseInt(qtyObj.toString());

                        foundIndex[0] = i;
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    JOptionPane.showMessageDialog(panel, "Product with ID '" + id + "' not found.", "Not found",
                            JOptionPane.INFORMATION_MESSAGE);
                    foundIndex[0] = -1;
                    return;
                }
            }
        });

        cbCategory.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {

                ProductCatalog.refreshCategoryCombo();
            }

            @Override
            public void focusLost(FocusEvent e) {
                // System.out.println("ComboBox Lost Focus");
            }
        });

        int row = 0;
        addFormRow(panel, gbc, row++, lblCustomerId, tfCustomerId);
        addFormRow(panel, gbc, row++, lblCustomer, tfCustomer);
        addFormRow(panel, gbc, row++, lblCategory, cbCategory);
        addFormRow(panel, gbc, row++, lblProduct, cbProductId);
        addFormRow(panel, gbc, row++, lblQty, tfQty);
        addFormRow(panel, gbc, row++, lblPrice, tfPrice);
        addFormRow(panel, gbc, row++, lblDate, tfDate);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = makePrimaryButton("Submit Sale");
        submit.addActionListener(e -> {
            String customerIdStr = tfCustomerId.getText().trim();
            String customer = tfCustomer.getText().trim();
            String category = String.valueOf(cbCategory.getSelectedItem());
            String productId = String.valueOf(cbProductId.getSelectedItem());
            String qtyStr = tfQty.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String dateStr = tfDate.getText().trim();

            String idErr = validateEntityId(customerIdStr, "Customer ID");
            if (idErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ProductCatalog.isPlaceholder(category)) {
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ProductCatalog.isPlaceholder(productId)) {
                JOptionPane.showMessageDialog(this, "Please select a product.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            String err = validateSaleRest(customer, qtyStr, priceStr, dateStr);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int qty = 0;
            double price = 0;
            try {
                qty = Integer.parseInt(qtyStr);
                price = Double.parseDouble(priceStr);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Invalid Quantity of Price", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (qty > availableQuantity) {
                JOptionPane.showMessageDialog(panel, "Only " + availableQuantity + " items are in the stock",
                        "Out of Stock", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            mainTabs.setSelectedIndex(0);

            try {
                Connection conn = DBConnection.getConnection();
                String storeSalesDetailQuery = "INSERT INTO SALES (CUSTOMER_ID, CATEGORY, PRODUCT_ID, QUANTITY, SELLING_UNIT_PRICE, DATE, SELLER_EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?);";
                String updateProductStockQuery = "UPDATE PRODUCT_QUANTITY SET QUANTITY = COALESCE(QUANTITY, 0) - ? WHERE PRODUCT_ID = ? AND COALESCE(QUANTITY, 0) >= ?";

                PreparedStatement psStmt = conn.prepareStatement(storeSalesDetailQuery);
                psStmt.setString(1, customerIdStr);
                psStmt.setString(2, category);
                psStmt.setString(3, productId);
                psStmt.setInt(4, qty);
                psStmt.setDouble(5, price);
                psStmt.setString(6, dateStr);
                psStmt.setString(7, this.Username);

                PreparedStatement psStmt1 = conn.prepareStatement(updateProductStockQuery);
                psStmt1.setInt(1, qty);
                psStmt1.setString(2, productId);
                psStmt1.setInt(3, qty);

                int affectedRows = psStmt.executeUpdate();
                psStmt1.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(panel, "Product Sold Successfully", "Sales",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                conn.close();

                loadProductData();
                clearAfterSale(tfCustomerId, tfCustomer, cbCategory, cbProductId, tfQty, tfPrice, tfDate);

                return;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(submit, gbc);

        return panel;
    }

    JPanel buildProductDetailsTab() {
        loadProductData();
        return tableInScrollPane(productDetailsModel);
    }

    private JPanel tableInScrollPane(DefaultTableModel model) {
        JPanel holder = new JPanel(new BorderLayout());
        holder.setBackground(BG_DARK);

        JTable table = new JTable(model);
        table.setFont(FONT_FIELD);
        table.setForeground(TEXT_WHITE);
        table.setBackground(BG_CARD);
        table.setGridColor(new Color(71, 85, 105));
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        styleTableHeader(table);

        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setBackground(BG_CARD);
        left.setForeground(TEXT_WHITE);
        left.setFont(FONT_FIELD);
        for (int c = 0; c < table.getColumnCount(); c++) {
            table.getColumnModel().getColumn(c).setCellRenderer(left);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(ACCENT2, 1));
        scroll.getViewport().setBackground(BG_CARD);
        holder.add(scroll, BorderLayout.CENTER);
        return holder;
    }

    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_BLUE);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setReorderingAllowed(false);
        DefaultTableCellRenderer hr = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                comp.setBackground(TABLE_HEADER_BLUE);
                comp.setForeground(Color.WHITE);
                comp.setFont(new Font("SansSerif", Font.BOLD, 14));
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        };
        header.setDefaultRenderer(hr);
    }

    private void loadProductData() {

        productDetailsModel.setRowCount(0);

        try {
            Connection conn = DBConnection.getConnection();
            String fetchProductDetailsQuery = "SELECT PRODUCT.*, PRODUCT_PRICE.SELLING_UNIT_PRICE,PRODUCT_PRICE.PURCHASE_UNIT_PRICE, PRODUCT_QUANTITY.QUANTITY FROM PRODUCT\n"
                    +
                    "INNER JOIN PRODUCT_PRICE\n" +
                    "INNER JOIN PRODUCT_QUANTITY\n" +
                    "WHERE PRODUCT.PRODUCT_ID = PRODUCT_PRICE.PRODUCT_ID AND PRODUCT.PRODUCT_ID = PRODUCT_QUANTITY.PRODUCT_ID;";
            PreparedStatement psStmt = conn.prepareStatement(fetchProductDetailsQuery);
            ResultSet res = psStmt.executeQuery();

            while (res.next()) {
                productDetailsModel.addRow(new Object[] {
                        res.getString("PRODUCT_ID"),
                        res.getString("PRODUCT_NAME"),
                        res.getString("CATEGORY"),
                        res.getString("DESCRIPTION"),
                        res.getInt("QUANTITY"),
                        res.getDouble("PURCHASE_UNIT_PRICE"),
                        res.getDouble("SELLING_UNIT_PRICE"),
                });
            }

            conn.close();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel buildNewCustomerTab() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblCustomerId = makeFormLabel("Customer ID");
        JLabel lblCustomer = makeFormLabel("Customer Name");
        JLabel lblMobileNo = makeFormLabel("Mobile No.");
        JLabel lblAge = makeFormLabel("Age.");
        JLabel lblGender = makeFormLabel("Gender.");
        JLabel lblEmail = makeFormLabel("Email");
        JLabel lblAddress = makeFormLabel("Address");

        JTextField tfCustomerId = makeField();
        JTextField tfCustomer = makeField();
        JTextField tfMobileNo = makeField();
        JTextField tfAge = makeField();
        JTextField tfEmail = makeField();
        JTextField tfAddress = makeField();

        JComboBox<String> cbGender = makeCombo(new String[] { "Male", "Female", "Others" });

        tfCustomerId.setText("CUS-BGB-0001");
        tfCustomerId.setForeground(Color.GRAY);
        placeHolder(tfCustomerId, tfCustomerId.getText().trim());

        int row = 0;
        addFormRow(panel, gbc, row++, lblCustomerId, tfCustomerId);
        addFormRow(panel, gbc, row++, lblCustomer, tfCustomer);
        addFormRow(panel, gbc, row++, lblMobileNo, tfMobileNo);
        addFormRow(panel, gbc, row++, lblAge, tfAge);
        addFormRow(panel, gbc, row++, lblGender, cbGender);
        addFormRow(panel, gbc, row++, lblEmail, tfEmail);
        addFormRow(panel, gbc, row++, lblAddress, tfAddress);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton register = makePrimaryButton("Register");

        register.addActionListener(e -> {
            String customerIdStr = tfCustomerId.getText().trim();
            String customer = tfCustomer.getText().trim();
            String mobile = tfMobileNo.getText().trim();
            String ageStr = tfAge.getText().trim();
            String gender = cbGender.getSelectedItem().toString().trim();
            String email = tfEmail.getText().trim();
            String address = tfAddress.getText().trim();

            String idErr = validateEntityId(customerIdStr, "Customer ID");
            if (idErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age = 0;

            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Age should be a positive number", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (age < 0) {
                JOptionPane.showMessageDialog(panel, "Age should be a positive number", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (customerIdStr.length() != 12 || !customerIdStr.startsWith("CUS-BGB-")
                    || !customerIdStr.matches("^[A-Z]{3}-[A-Z]{3}-[0-9]{4}$")) {
                JOptionPane.showMessageDialog(this, "Invalid Customer ID format!", "Validation",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String nameErr = validateEntityId(customer, "Customer Name");
            if (nameErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String mobileErr = validateEntityId(mobile, "Mobile Number");
            if (mobileErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String emailErr = validateEntityId(email, "Email");
            if (emailErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String addressErr = validateEntityId(address, "Customer ID");
            if (addressErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {

                Connection conn = DBConnection.getConnection();
                String addCustomerQuery = "INSERT INTO CUSTOMER (CUSTOMER_ID, CUSTOMER_NAME, MOBILE_NO, AGE, GENDER, EMAIL, ADDRESS) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement psStmt = conn.prepareStatement(addCustomerQuery);
                psStmt.setString(1, customerIdStr);
                psStmt.setString(2, customer);
                psStmt.setString(3, mobile);
                psStmt.setInt(4, age);
                psStmt.setString(5, gender);
                psStmt.setString(6, email);
                psStmt.setString(7, address);

                int affectedRows = psStmt.executeUpdate();

                if (affectedRows > 0) {

                    JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                            "Customer Registered Successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    conn.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            tfCustomerId.setText("CUS-BGB-0001");
            tfCustomer.setText("");
            tfMobileNo.setText("");
            tfEmail.setText("");
            tfAddress.setText("");
            tfCustomerId.setForeground(Color.GRAY);
            placeHolder(tfCustomerId, tfCustomerId.getText().trim());
            mainTabs.setSelectedIndex(0);
        });

        panel.add(register, gbc);
        return panel;
    }

    private JPanel buildChangePasswordTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblOldPassword = makeFormLabel("Old Password");
        JLabel lblNewPassword = makeFormLabel("New Password");
        JLabel lblConfirmPassword = makeFormLabel("Confirm Password");

        JTextField tfOldPassword = makeField();
        JTextField tfNewPassword = makeField();
        JTextField tfConfirmPassword = makeField();

        int row = 0;
        addFormRow(panel, gbc, row++, lblOldPassword, tfOldPassword);
        addFormRow(panel, gbc, row++, lblNewPassword, tfNewPassword);
        addFormRow(panel, gbc, row++, lblConfirmPassword, tfConfirmPassword);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton changePassword = makePrimaryButton("Change Password");

        changePassword.addActionListener(e -> {
            String oldPasswordStr = tfOldPassword.getText().trim();
            String newPassword = tfNewPassword.getText().trim();
            String confirmPassword = tfConfirmPassword.getText().trim();

            if (!oldPasswordStr.equals(this.Password)) {
                JOptionPane.showMessageDialog(this, "Current Password does not matched", "Authentication",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Password Mismatched", "Mismatch", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (newPassword.length() < 10) {
                JOptionPane.showMessageDialog(this, "Password length should be more than 10 characters",
                        "Authenticatiion", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if (!newPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{10,16}$")) {
                JOptionPane.showMessageDialog(this,
                        "Password must contain atleast a capital letter, small letter, numbers and a special character.",
                        "Authenticatiion", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.getConnection();
                String changePasswordQuery = "UPDATE EMPLOYEE SET PASSWORD = ? WHERE EMAIL = ?";
                PreparedStatement psStmt = conn.prepareStatement(changePasswordQuery);
                psStmt.setString(1, confirmPassword);
                psStmt.setString(2, this.Username);

                int affectedRows = psStmt.executeUpdate();

                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Password Changed Successfully", "Authentication",
                            JOptionPane.INFORMATION_MESSAGE);

                    mainTabs.setSelectedIndex(0);
                }

                conn.close();
                return;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        panel.add(changePassword, gbc);
        return panel;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, JLabel label, JComboBox<String> combo) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(label, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(combo, gbc);
    }

    // ! -------- small helpers used by the sales panel --------------
    private String safeToString(Object o) {
        return o == null ? "" : o.toString();
    }

    private JLabel makeFormLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_WHITE);
        return l;
    }

    private JTextField makeField() {
        JTextField t = new JTextField(24);
        t.setFont(FONT_FIELD);
        t.setBackground(INPUT_BG);
        t.setForeground(TEXT_WHITE);
        t.setCaretColor(ACCENT);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT2, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return t;
    }

    private JComboBox<String> makeCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(FONT_FIELD);
        c.setBackground(INPUT_BG);
        c.setForeground(TEXT_WHITE);
        return c;
    }

    private JButton makePrimaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BTN);
        b.setBackground(ACCENT2);
        b.setForeground(TEXT_WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 24, 10, 24));
        return b;
    }

    private void clearAfterSale(JTextField tfCustomerId, JTextField tfCustomer, JComboBox<String> cbCategory,
            JComboBox<String> cbProductId, JTextField tfQty, JTextField tfPrice, JTextField tfDate) {
        tfCustomerId.setText("");
        tfCustomer.setText("");
        cbCategory.setSelectedIndex(0);
        ProductCatalog.refreshProductCombo(cbProductId, cbCategory.getSelectedItem());
        tfQty.setText("");
        tfPrice.setText("");
        tfDate.setText("");
    }

    private String validateEntityId(String idStr, String fieldLabel) {
        if (idStr.isEmpty()) {
            return fieldLabel + " is required.";
        }
        try {
            // int id = Integer.parseInt(idStr);
            // if (id <= 0) {
            // return fieldLabel + " must be a positive whole number.";
            // }
        } catch (NumberFormatException ex) {
            return fieldLabel + " must be a valid whole number.";
        }
        return null;
    }

    private void placeHolder(JTextField txtId, String placeHolder) {
        txtId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtId.getText().equals(placeHolder)) {
                    txtId.setText("");
                    txtId.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtId.getText().isEmpty()) {
                    txtId.setForeground(Color.GRAY);
                    txtId.setText(placeHolder);
                }
            }
        });
    }

    private String validateSaleRest(String customer, String qtyStr, String priceStr, String dateStr) {
        if (customer.isEmpty()) {
            return "Customer name is required.";
        }
        if (qtyStr.isEmpty()) {
            return "Quantity is required.";
        }
        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) {
                return "Quantity must be a positive whole number.";
            }
        } catch (NumberFormatException ex) {
            return "Quantity must be a valid whole number.";
        }
        if (priceStr.isEmpty()) {
            return "Price is required.";
        }
        try {
            double price = Double.parseDouble(priceStr);
            if (price < 0) {
                return "Price cannot be negative.";
            }
        } catch (NumberFormatException ex) {
            return "Price must be a valid number (e.g. 199.50).";
        }
        if (dateStr.isEmpty()) {
            return "Date is required.";
        }
        if (!DATE_PATTERN.matcher(dateStr).matches()) {
            return "Date must be in dd/mm/yyyy format.";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
            sdf.setLenient(false);
            sdf.parse(dateStr);
        } catch (Exception ex) {
            return "Invalid calendar date.";
        }
        return null;
    }

}
