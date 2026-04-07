
package inventory.management.system;

import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ExistingProduct extends JFrame {

    JTextField productNameField, productIdField, category;
    JTextArea description;
    Button addProduct, clear, updateProduct, back, search, removeProduct;
    JPanel imagePanel;
    Canvas imgCanvas;

    // ! ── Constructor ────────────────────────────────────────────────────────────
    public ExistingProduct() {
        setLayout(null); // Set layout BEFORE adding components
        getContentPane().setBackground(new Color(30, 30, 60));
        ImageIcon image = new ImageIcon("Src\\Icons\\Logo.png");
        setIconImage(image.getImage());

        // ! Get the screen size for the header width
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();

        JLabel header = new JLabel("Inventory Management System - Manage Existing Product", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 36));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(15, 23, 42));
        header.setOpaque(true); // Make the label paint its background
        header.setBounds(1, 0, screenWidth, 70);
        add(header);

        JPanel form = new JPanel();
        form.setLayout(null);
        form.setBounds(30, 180, 735, 660);
        form.setBackground(new Color(30, 41, 59));
        form.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel productIdlbl = new JLabel("Product ID: ");
        productIdlbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        productIdlbl.setForeground(Color.WHITE);
        productIdlbl.setBackground(getForeground());
        productIdlbl.setOpaque(true); // Make the label paint its background
        productIdlbl.setBounds(40, 50, 250, 30);
        form.add(productIdlbl);

        productIdField = new JTextField(25);
        productIdField.setFont(new Font("SansSerif", Font.BOLD, 16));
        productIdField.setBackground(Color.WHITE);
        productIdField.setForeground(Color.BLACK);
        productIdField.setBounds(350, 50, 350, 30);
        form.add(productIdField);

        JLabel productNamelbl = new JLabel("Product Name: ");
        productNamelbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        productNamelbl.setForeground(Color.WHITE);
        productNamelbl.setBackground(getForeground());
        productNamelbl.setOpaque(true); // Make the label paint its background
        productNamelbl.setBounds(40, 150, 250, 30);
        form.add(productNamelbl);

        productNameField = new JTextField(25);
        productNameField.setFont(new Font("SansSerif", Font.BOLD, 16));
        productNameField.setBackground(Color.WHITE);
        productNameField.setForeground(Color.BLACK);
        productNameField.setBounds(350, 150, 350, 30);
        productNameField.setEditable(false);
        form.add(productNameField);

        JLabel categorylbl = new JLabel("Category: ");
        categorylbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        categorylbl.setForeground(Color.WHITE);
        categorylbl.setBackground(getForeground());
        categorylbl.setOpaque(true); // Make the label paint its background
        categorylbl.setBounds(40, 210, 250, 30);
        form.add(categorylbl);

        category = new JTextField(35);
        category.setFont(new Font("SansSerif", Font.BOLD, 16));
        category.setBackground(Color.WHITE);
        category.setForeground(Color.BLACK);
        category.setBounds(350, 210, 350, 30);
        form.add(category);

        JLabel descriptionlbl = new JLabel("Description: ");
        descriptionlbl.setFont(new Font("SansSerif", Font.BOLD, 26));
        descriptionlbl.setForeground(Color.WHITE);
        descriptionlbl.setBackground(getForeground());
        descriptionlbl.setOpaque(true); // Make the label paint its background
        descriptionlbl.setBounds(40, 270, 250, 30);
        form.add(descriptionlbl);

        description = new JTextArea(20, 30);
        description.setFont(new Font("SansSerif", Font.BOLD, 16));
        description.setBackground(Color.WHITE);
        description.setForeground(Color.BLACK);
        description.setBounds(350, 270, 350, 120);
        form.add(description);

        // ! ------------- Label to show status ----------------------
        JLabel statuslbl = new JLabel("", JLabel.CENTER);
        statuslbl.setFont(new Font("SansSerif", Font.ITALIC, 14));
        statuslbl.setBackground(getForeground());
        statuslbl.setForeground(Color.RED);
        statuslbl.setBounds(320, 390, 400, 40);
        form.add(statuslbl);

        // ! ------------- Buttons ----------------------
        addProduct = new Button("Update Product");
        addProduct.setFont(new Font("SansSerif", Font.BOLD, 18));
        addProduct.setBounds(350, 450, 150, 35);
        addProduct.setBackground(new Color(59, 130, 246));
        addProduct.setForeground(Color.WHITE);
        form.add(addProduct);

        clear = new Button("Clear");
        clear.setFont(new Font("SansSerif", Font.BOLD, 18));
        clear.setBounds(550, 450, 150, 35);
        clear.setBackground(Color.RED);
        clear.setForeground(Color.WHITE);
        form.add(clear);

        search = new Button("Search");
        search.setFont(new Font("SansSerif", Font.BOLD, 18));
        search.setBounds(550, 90, 150, 35);
        search.setBackground(new Color(59, 130, 246));
        search.setForeground(Color.WHITE);
        form.add(search);

        removeProduct = new Button("Remove Product");
        removeProduct.setFont(new Font("SansSerif", Font.BOLD, 18));
        removeProduct.setBounds(350, 570, 350, 35);
        removeProduct.setBackground(new Color(99, 102, 241));
        removeProduct.setForeground(Color.WHITE);
        form.add(removeProduct);

        updateProduct = new Button("Manage New Product");
        updateProduct.setFont(new Font("SansSerif", Font.BOLD, 18));
        updateProduct.setBounds(350, 510, 350, 35);
        updateProduct.setBackground(Color.CYAN);
        updateProduct.setForeground(Color.BLACK);
        form.add(updateProduct);

        back = new Button("Back");
        back.setFont(new Font("SansSerif", Font.BOLD, 18));
        back.setBounds(1300, 820, 150, 35);
        back.setBackground(Color.CYAN);
        back.setForeground(Color.BLACK);
        add(back);

        form.setVisible(true);
        add(form);

        // ! ------------- Image Panel ----------------------
        imagePanel = new JPanel(null) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
            }
        };

        imagePanel.setLayout(null);
        imagePanel.setBounds(800, 180, 700, 600);
        add(imagePanel);

        try {
            // ! ------------- Trying to load the image from system ----------------------
            Image adminImg = Toolkit.getDefaultToolkit().getImage("Src/Icons/AddProduct.png");
            imgCanvas = new Canvas(null) {
                public void paint(Graphics g) {
                    g.drawImage(adminImg, 0, 0, getWidth(), getHeight(), this);
                }
            };
            imagePanel.setLayout(null);
            imgCanvas.setBounds(0, 0, 700, 600);
            imagePanel.add(imgCanvas, Canvas.CENTER_ALIGNMENT);

        } catch (Exception e) {
            // Fallback: show placeholder if image not found
            JLabel placeholder = new JLabel("👨‍💼\nAdmin Image", JLabel.CENTER);
            placeholder.setFont(new Font("SansSerif", Font.BOLD, 24));
            placeholder.setForeground(new Color(56, 189, 248));
            placeholder.setBounds(20, 20, 340, 380);
            imagePanel.add(placeholder);
        }

        // ! ======= Action Listeners ==========

        addProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ! ------------ Will check in future what details are needed to perform this
                // operations.
                String productName = productNameField.getText().trim();
                // String productId = productIdField.getText().trim();
                String categoryinfo = category.getText().trim();
                String prodDescription = description.getText().trim();

                // ! ------------- Input validations ----------------------

                if (prodDescription.isEmpty() || productName.isEmpty() || categoryinfo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter all feilds first.", "Empty fields",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {

                    statuslbl.setText("Adding Product.......");
                    // TODO: Logic to Update product into database.

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        removeProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // ! Will check in future what details are needed to perform this action.
                String productName = productNameField.getText().trim();
                // String productId = productIdField.getText().trim();
                String categoryinfo = category.getText().trim();
                String prodDescription = description.getText().trim();

                if (prodDescription.isEmpty() || productName.isEmpty() || categoryinfo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter all feilds first.", "Empty fields",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                try {
                    // TODO: Logic to delete product from the database
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });

        search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = productIdField.getText().trim();
                if (id.isEmpty()) {
                    statuslbl.setText("Please enter product ID before search.");
                    return;
                }
                // TODO: Fetch Data from database of a product and display on the screen.

                productNameField.setText("To be fetched from database");
                category.setText("To be fetched from database");
                description.setText("To be fetched from database");
                return;
            }
        });

        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productNameField.setText("");
                productIdField.setText("");
                category.setText("");;
                description.setText("");
                statuslbl.setText("");
                return;
            }
        });

        updateProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AddProduct();
                return;
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new AdminFeatures();
                return;
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) {
        new ExistingProduct();
    }
}
