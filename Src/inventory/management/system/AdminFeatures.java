package inventory.management.system;

import javax.swing.*;
import java.awt.*;

public class AdminFeatures {

    public AdminFeatures() {
        // ! ----------------------- Frame ---------------------------
        JFrame frame = new JFrame("Inventory Management System - Admin");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon("Src\\Icons\\Logo.png");
        frame.setIconImage(image.getImage());
        frame.getContentPane().setBackground(new Color(14, 9, 41));

        // !------------------- HEADER -------------------------
        JPanel header = new JPanel();
        header.setBackground(new Color(15, 23, 42));
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        header.setPreferredSize(new Dimension(frame.getWidth(), 70));
        JLabel title = new JLabel("Inventory Management System - Admin");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        header.add(title, JPanel.CENTER_ALIGNMENT);
        frame.add(header, BorderLayout.NORTH);

        // ! ---------------------- SIDEBAR ---------------------------
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(7, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(300, 0));
        sidebar.setBackground(new Color(30, 30, 60));

        String[] btnMenu = {
                "Dashboard",
                "Employee Management",
                "Product Management",
                "Suppliers",
                "Purchases",
                "Reports",
                "Log Out"
        };

        Button dashboardBtn = new Button(btnMenu[0]);
        dashboardBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        dashboardBtn.setBackground(new Color(42, 21, 127));
        dashboardBtn.setForeground(Color.WHITE);
        sidebar.add(dashboardBtn);

        Button employeeMgmtBtn = new Button(btnMenu[1]);
        employeeMgmtBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        employeeMgmtBtn.setBackground(new Color(122, 21, 127));
        employeeMgmtBtn.setForeground(Color.WHITE);
        employeeMgmtBtn.addActionListener(e -> {
            new EmployeeManagementUI();
            frame.dispose();
        });
        sidebar.add(employeeMgmtBtn);

        Button proStockMgmtBtn = new Button(btnMenu[2]);
        proStockMgmtBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        proStockMgmtBtn.setBackground(new Color(42, 21, 127));
        proStockMgmtBtn.setForeground(Color.WHITE);
        proStockMgmtBtn.addActionListener(e -> {
            new AddProduct();
            frame.dispose();
        });
        sidebar.add(proStockMgmtBtn);

        Button supplierBtn = new Button(btnMenu[3]);
        supplierBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        supplierBtn.setBackground(new Color(122, 21, 127));
        supplierBtn.setForeground(Color.WHITE);
        supplierBtn.addActionListener(e -> {
            new SupplierManagement();
            frame.dispose();
        });
        sidebar.add(supplierBtn);

        Button salePurchaseBtn = new Button(btnMenu[4]);
        salePurchaseBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        salePurchaseBtn.setBackground(new Color(42, 21, 127));
        salePurchaseBtn.setForeground(Color.WHITE);
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
            new LoginFrame();
            frame.dispose();
        });
        sidebar.add(logOutBtn);

        frame.add(sidebar, BorderLayout.WEST);

        // ! ── Main content area: swap views from sidebar (CardLayout) ─────────────
        final JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(30, 30, 60));
        JPanel dashboardHomePanel = new JPanel(new BorderLayout());
        dashboardHomePanel.setBackground(new Color(30, 30, 60));

        // Main content container
        JPanel content = new JPanel();
        content.setBackground(new Color(30, 30, 60));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Title
        JLabel welcome = new JLabel("Welcome to Admin Dashboard");
        welcome.setFont(new Font("SansSerif", Font.BOLD, 32));
        welcome.setForeground(Color.WHITE);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("Inventory Management System");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 18));
        subtitle.setForeground(new Color(180, 180, 200));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Info content
        JLabel info = new JLabel(
                "<html><div style='width:500px;height:400px; text-align:left; display: flex; justify-content: center; align-items: center;'>"
                        + "<br>Manage your entire inventory system efficiently from one place.<br><br><br><br>"
                        + "• Add, update and manage products<br><br>"
                        + "• Track stock levels and availability<br><br>"
                        + "• Manage employees and suppliers<br><br>"
                        + "• Monitor purchases and sales<br><br>"
                        + "• Generate reports for business insights<br><br><br><br>"
                        + "Use the sidebar to navigate through different modules."
                        + "</div></html>");

        info.setFont(new Font("SansSerif", Font.PLAIN, 20));
        // info.setForeground(new Color(200, 200, 220));
        info.setForeground(Color.WHITE);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setHorizontalAlignment(SwingConstants.CENTER);

        // Footer note
        JLabel footerNote = new JLabel("System Status: All services running smoothly");
        footerNote.setFont(new Font("SansSerif", Font.ITALIC, 13));
        footerNote.setForeground(new Color(140, 140, 160));
        footerNote.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add spacing
        content.add(Box.createVerticalStrut(40));
        content.add(welcome);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(55));
        content.add(info);
        content.add(Box.createVerticalStrut(30));
        content.add(footerNote);

        // Add to panel
        dashboardHomePanel.add(content, BorderLayout.CENTER);

        contentPanel.add(dashboardHomePanel, "HOME");
        contentPanel.add(new SalesPurchasePanel(), "SALES_PURCHASE");

        dashboardBtn.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "HOME"));
        salePurchaseBtn
                .addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "SALES_PURCHASE"));

        frame.add(contentPanel, BorderLayout.CENTER);

        // ! ----------------- FOOTER ------------------------------
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
