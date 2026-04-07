package inventory.management.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.Locale;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Admin purchase workspace plus sales history viewer.
 * Sales entries are created by employees after employee login and shown here in read-only mode.
 */
public class SalesPurchasePanel extends JPanel {

    private static final String[] PURCHASE_HISTORY_COLS = {
            "ID", "Supplier ID", "Supplier", "Category", "Product", "Qty", "Price/Unit", "Selling Price", "Total", "Date"
    };
    private static final String[] SALES_HISTORY_COLS = {
            "ID", "Customer ID", "Customer", "Category", "Product", "Qty", "Selling Price", "Total", "Date", "Sold By"
    };

    private final DefaultTableModel purchaseHistoryModel;
    private final DefaultTableModel salesHistoryModel;

    private JTabbedPane mainTabs;

    private static final Color BG_DARK = new Color(15, 23, 42);
    private static final Color BG_CARD = new Color(30, 41, 59);
    private static final Color ACCENT = new Color(56, 189, 248);
    private static final Color ACCENT2 = new Color(99, 102, 241);
    private static final Color TEXT_WHITE = new Color(241, 245, 249);
    private static final Color INPUT_BG = new Color(51, 65, 85);
    private static final Color TABLE_HEADER_BLUE = new Color(37, 99, 235);
    private static final Font FONT_LABEL = new Font("SansSerif", Font.BOLD, 16);
    private static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_BTN = new Font("SansSerif", Font.BOLD, 16);

    private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");

    public SalesPurchasePanel() {
        purchaseHistoryModel = createNonEditableTableModel(PURCHASE_HISTORY_COLS);
        salesHistoryModel = createNonEditableTableModel(SALES_HISTORY_COLS);

        setLayout(new BorderLayout());
        setBackground(BG_DARK);
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel heading = new JLabel("Purchases", SwingConstants.CENTER);
        heading.setFont(FONT_TITLE);
        heading.setForeground(ACCENT);
        heading.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        add(heading, BorderLayout.NORTH);

        mainTabs = new JTabbedPane(JTabbedPane.TOP);
        mainTabs.setFont(new Font("SansSerif", Font.BOLD, 14));
        mainTabs.setBackground(BG_CARD);
        mainTabs.setForeground(TEXT_WHITE);

        mainTabs.addTab("New Purchase", wrapTab(buildNewPurchaseTab()));
        // Keep Sales History beside Purchase History for quick comparison.
        mainTabs.addTab("Sales History", wrapTab(buildSalesHistoryTab()));
        mainTabs.addTab("Purchase History", wrapTab(buildPurchaseHistoryTab()));

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

    private JPanel buildNewPurchaseTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblSupplierId = makeFormLabel("Supplier ID");
        JLabel lblSupplier = makeFormLabel("Supplier Name");
        JLabel lblCategory = makeFormLabel("Category");
        JLabel lblProduct = makeFormLabel("Product Name");
        JLabel lblQty = makeFormLabel("Quantity");
        JLabel lblPrice = makeFormLabel("Price per Unit");
        JLabel lblSellingPrice = makeFormLabel("Selling Price");
        JLabel lblDate = makeFormLabel("Date (dd/mm/yyyy)");

        JTextField tfSupplierId = makeField();
        JTextField tfSupplier = makeField();
        JComboBox<String> cbCategory = makeCombo(ProductCatalog.categoryComboItems());
        JComboBox<String> cbProduct = makeCombo(new String[] { ProductCatalog.PLACEHOLDER });
        cbCategory.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ProductCatalog.refreshProductCombo(cbProduct, cbCategory.getSelectedItem());
            }
        });

        JTextField tfQty = makeField();
        JTextField tfPrice = makeField();
        JTextField tfSellingPrice = makeField();
        JTextField tfDate = makeField();

        int row = 0;
        addFormRow(panel, gbc, row++, lblSupplierId, tfSupplierId);
        addFormRow(panel, gbc, row++, lblSupplier, tfSupplier);
        addFormRow(panel, gbc, row++, lblCategory, cbCategory);
        addFormRow(panel, gbc, row++, lblProduct, cbProduct);
        addFormRow(panel, gbc, row++, lblQty, tfQty);
        addFormRow(panel, gbc, row++, lblPrice, tfPrice);
        addFormRow(panel, gbc, row++, lblSellingPrice, tfSellingPrice);
        addFormRow(panel, gbc, row++, lblDate, tfDate);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton submit = makePrimaryButton("Submit Purchase");
        submit.addActionListener(e -> {
            String supplierIdStr = tfSupplierId.getText().trim();
            String supplier = tfSupplier.getText().trim();
            String category = String.valueOf(cbCategory.getSelectedItem());
            String product = String.valueOf(cbProduct.getSelectedItem());
            String qtyStr = tfQty.getText().trim();
            String priceStr = tfPrice.getText().trim();
            String sellingPriceStr = tfSellingPrice.getText().trim();
            String dateStr = tfDate.getText().trim();

            String idErr = validateEntityId(supplierIdStr, "Supplier ID");
            if (idErr != null) {
                JOptionPane.showMessageDialog(this, idErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ProductCatalog.isPlaceholder(category)) {
                JOptionPane.showMessageDialog(this, "Please select a category.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (ProductCatalog.isPlaceholder(product)) {
                JOptionPane.showMessageDialog(this, "Please select a product.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String sellingErr = validateMoneyValue(sellingPriceStr, "Selling price");
            if (sellingErr != null) {
                JOptionPane.showMessageDialog(this, sellingErr, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String err = validatePurchaseRest(supplier, qtyStr, priceStr, dateStr);
            if (err != null) {
                JOptionPane.showMessageDialog(this, err, "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int qty = Integer.parseInt(qtyStr);
            double unit = Double.parseDouble(priceStr);
            double selling = Double.parseDouble(sellingPriceStr);
            double total = qty * unit;
            //! int suppId = Integer.parseInt(supplierIdStr);

            clearAfterPurchase(tfSupplierId, tfSupplier, cbCategory, cbProduct, tfQty, tfPrice, tfSellingPrice, tfDate);
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
                    "Purchase recorded successfully.\nSupplier ID: " + supplierIdStr + "\nSupplier: " + supplier
                            + "\nCategory: " + category + "\nProduct: " + product + "\nSelling Price: "
                            + formatRupee(selling) + "\nTotal: " + formatRupee(total),
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            mainTabs.setSelectedIndex(1);

            // TODO: Save the purchase details into database.
        });
        panel.add(submit, gbc);

        return panel;
    }

    private JPanel buildPurchaseHistoryTab() {
        // TODO: To load the purchase data from the database. For demo, new purchases in this session are shown here.
        // !DEMO: purchaseHistoryModel.addRow(new Object[] {1, "52VCD23", "Aditya Patel", "Hardware", "Laptop",  25, 45000, 60000, 45000*25, "12/12/2025" });
        return tableInScrollPane(purchaseHistoryModel);
    }

    private JPanel buildSalesHistoryTab() {
        // TODO: To load the sales data from the database and store it into the sales history model.
        //! DEMO: salesHistoryModel.addRow(new Object[] {1, "52VCD23", "Aditya Patel", "Hardware", "Laptop",  25, 60000, 45000*25, "12/12/2025" });
        return tableInScrollPane(salesHistoryModel);
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

    private void clearAfterPurchase(JTextField tfSupplierId, JTextField tfSupplier, JComboBox<String> cbCategory,
            JComboBox<String> cbProduct, JTextField tfQty, JTextField tfPrice, JTextField tfSellingPrice, JTextField tfDate) {
        tfSupplierId.setText("");
        tfSupplier.setText("");
        cbCategory.setSelectedIndex(0);
        ProductCatalog.refreshProductCombo(cbProduct, cbCategory.getSelectedItem());
        tfQty.setText("");
        tfPrice.setText("");
        tfSellingPrice.setText("");
        tfDate.setText("");
    }

    private String validateMoneyValue(String value, String fieldLabel) {
        if (value.isEmpty()) {
            return fieldLabel + " is required.";
        }
        try {
            double money = Double.parseDouble(value);
            if (money < 0) {
                return fieldLabel + " cannot be negative.";
            }
        } catch (NumberFormatException ex) {
            return fieldLabel + " must be a valid number (e.g. 199.50).";
        }
        return null;
    }

    private String validateEntityId(String idStr, String fieldLabel) {
        if (idStr.isEmpty()) {
            return fieldLabel + " is required.";
        }
        try {
            // int id = Integer.parseInt(idStr);
            // if (id <= 0) {
            //     return fieldLabel + " must be a positive whole number.";
            // }
        } catch (NumberFormatException ex) {
            return fieldLabel + " must be a valid whole number.";
        }
        return null;
    }

    private String validatePurchaseRest(String supplier, String qtyStr, String priceStr, String dateStr) {
        if (supplier.isEmpty()) {
            return "Supplier name is required.";
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

    private static String formatRupee(double amount) {
        return String.format(Locale.US, "₹%,.2f", amount);
    }
}
