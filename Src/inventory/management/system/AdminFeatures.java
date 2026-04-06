package inventory.management.system;

import javax.swing.*;
import java.awt.*;

public class AdminFeatures {

    public AdminFeatures() {
        //! ----------------------- Frame ---------------------------
        JFrame frame = new JFrame("Inventory Management System - Admin");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon("Src\\Icons\\Logo.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(14, 9, 41));

        //!------------------- HEADER -------------------------
        JPanel header = new JPanel();
        header.setBackground(new Color(15, 23, 42));
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        header.setPreferredSize(new Dimension(frame.getWidth(), 70));
        JLabel title = new JLabel("Inventory Management System - Admin");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title, JPanel.CENTER_ALIGNMENT);
        frame.add(header, BorderLayout.NORTH);

        //! ---------------------- SIDEBAR ---------------------------
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(new Color(30, 30, 60));

        String[] btnMenu = {
            "Dashboard",
            "Employee Management",
            "Product Management",
            "Suppliers",
            "Sales & Purchase",
            "Reports",
            "Log Out"
        };

        Button dashboardBtn = new Button(btnMenu[0]);
        dashboardBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        dashboardBtn.setBackground(new Color(42, 21, 127));
        dashboardBtn.setForeground(Color.WHITE);
        dashboardBtn.addActionListener(null);
        sidebar.add(dashboardBtn);

        Button employeeMgmtBtn = new Button(btnMenu[1]);
        employeeMgmtBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        employeeMgmtBtn.setBackground(new Color(122, 21, 127));
        employeeMgmtBtn.setForeground(Color.WHITE);
        employeeMgmtBtn.addActionListener(e -> {
            frame.dispose();
            new EmployeeManagementUI();
        });
        sidebar.add(employeeMgmtBtn);

        Button proStockMgmtBtn = new Button(btnMenu[2]);
        proStockMgmtBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        proStockMgmtBtn.setBackground(new Color(42, 21, 127));
        proStockMgmtBtn.setForeground(Color.WHITE);
        proStockMgmtBtn.addActionListener(e -> {
            frame.dispose();;
            new AddProduct();
        });
        sidebar.add(proStockMgmtBtn);

        Button supplierBtn = new Button(btnMenu[3]);
        supplierBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        supplierBtn.setBackground(new Color(122, 21, 127));
        supplierBtn.setForeground(Color.WHITE);
        supplierBtn.addActionListener(e -> {
            frame.dispose();
            new SupplierManagement();
        });
        sidebar.add(supplierBtn);

        Button salePurchaseBtn = new Button(btnMenu[4]);
        salePurchaseBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        salePurchaseBtn.setBackground(new Color(42, 21, 127));
        salePurchaseBtn.setForeground(Color.WHITE);
        salePurchaseBtn.addActionListener(null);
        sidebar.add(salePurchaseBtn);

        Button reportsBtn = new Button(btnMenu[5]);
        reportsBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        reportsBtn.setBackground(new Color(122, 21, 127));
        reportsBtn.setForeground(Color.WHITE);
        reportsBtn.addActionListener(null);
        sidebar.add(reportsBtn);

        Button logOutBtn = new Button(btnMenu[6]);
        logOutBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        logOutBtn.setBackground(new Color(42, 21, 127));
        logOutBtn.setForeground(Color.WHITE);
        logOutBtn.addActionListener(e -> {
            frame.dispose();
            new LoginFrame();
        });
        sidebar.add(logOutBtn);

        frame.add(sidebar, BorderLayout.WEST);

        // MAIN PANEL
        JPanel mainPanel = new JPanel();
        // mainPanel.setLayout(new GridLayout(2, 2, 10, 10));
        mainPanel.setBackground(new Color(30, 30, 60));

        frame.add(mainPanel, BorderLayout.CENTER);

        //! ----------------- FOOTER ------------------------------
        JPanel footer = new JPanel();
        footer.setBackground(Color.GRAY);
        JLabel footText = new JLabel("© 2026 Inventory Management System");
        footText.setForeground(Color.WHITE);
        footText.setFont(new Font("SansSerif", Font.BOLD, 16));
        footer.add(footText);
        footer.setPreferredSize(new Dimension(frame.getWidth(), 30));
        frame.add(footer, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AdminFeatures();

    }
}
