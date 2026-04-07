package inventory.management.system;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;

/**
 * Opens after employee login: sales workspace (new sale + sales history).
 */
public class EmployeeDashboardFrame extends JFrame {

    public EmployeeDashboardFrame() {
        setTitle("Inventory Management System - Employee");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon("Src\\Icons\\Logo.png");
        setIconImage(image.getImage());
        getContentPane().setBackground(new Color(14, 9, 41));

        JPanel header = new JPanel();
        header.setBackground(new Color(15, 23, 42));
        header.setLayout(new FlowLayout(FlowLayout.CENTER));
        header.setPreferredSize(new Dimension(getWidth(), 70));
        JLabel title = new JLabel("Employee Workspace — Sales");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(2, 1, 5, 5));
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBackground(new Color(30, 30, 60));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnLogout = new JButton("Log Out");
        btnLogout.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnLogout.setBackground(new Color(42, 21, 127));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        sidebar.add(btnLogout);

        add(sidebar, BorderLayout.WEST);

        add(new EmployeeSalesPanel(), BorderLayout.CENTER);

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

    public static void main(String[] args) {
        new EmployeeDashboardFrame();
    }
}
