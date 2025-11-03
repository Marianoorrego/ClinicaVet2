package com.clinicavet.views;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.clinicavet.controllers.LoginControler;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;

public class AdminWindow extends JFrame {

	private final LoginControler loginControler;
    private final UserService userService;
    private final RolService rolService;

    private JPanel mainPanel; 
    private CardLayout cardLayout;
    private JPanel sidebarPanel;
    private JPanel contentPanel;

    public AdminWindow(LoginControler loginControler, UserService userService, RolService rolService) {
        this.loginControler = loginControler;
        this.userService = userService;
        this.rolService = rolService;
        initComponents();
    }

    private void initComponents() {
        setTitle("Administración - Clínica Veterinaria");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));

        
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(Color.WHITE);

        createSidebar();
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel headerPanel = createHeaderPanel();

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);

        CreateUserPanel panelCrear = new CreateUserPanel(userService, rolService);
        mainPanel.add(wrapPanel(panelCrear, "Gestión de Usuarios - Crear"), "CREAR");

        EditUserPanel editPanel = new EditUserPanel(userService, rolService);
        mainPanel.add(wrapPanel(editPanel, "Gestión de Usuarios - Editar"), "EDITAR");

        DisableUserPanel disablePanel = new DisableUserPanel(userService, rolService);
        mainPanel.add(wrapPanel(disablePanel, "Gestión de Usuarios - Desactivar"), "DESACTIVAR");

        ResetPasswordPanel resetPassword = new ResetPasswordPanel(userService);
        mainPanel.add(wrapPanel(resetPassword, "Gestión de Usuarios - Restablecer Contraseña"), "RESTABLECER");


        ListUsersPanel listUsers = new ListUsersPanel(userService);
        mainPanel.add(wrapPanel(listUsers, "Gestión de Usuarios - Listar"), "LISTAR");

        JPanel dashboardPanel = createDashboardPanel();
        mainPanel.add(wrapPanel(dashboardPanel, "Estadisticas"), "DASHBOARD");

        contentPanel.add(headerPanel, BorderLayout.NORTH);
        contentPanel.add(mainPanel, BorderLayout.CENTER);

        mainContainer.add(sidebarPanel, BorderLayout.WEST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        getContentPane().add(mainContainer);

        cardLayout.show(mainPanel, "DASHBOARD");
    }

    private void createSidebar() {
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(250, getHeight()));
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        
        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(new Color(44, 62, 80));
        JLabel logoLabel = new JLabel("Huellitas Sanas");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoPanel.add(logoLabel);
        sidebarPanel.add(logoPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));
       
        String[] menuItems = {
            "Principal", "Crear Usuario", "Editar Usuario", 
            "Desactivar Usuario", "Restablecer Contraseña", "Listar Usuarios"
        };
        String[] cardNames = {
            "DASHBOARD", "CREAR", "EDITAR", "DESACTIVAR", "RESTABLECER", "LISTAR"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], cardNames[i]);
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        sidebarPanel.add(Box.createVerticalGlue());

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setMaximumSize(new Dimension(200, 40));
        
        logoutButton.addActionListener(e -> {
            loginControler.logout();
            dispose();
            new Login(loginControler).setVisible(true);
        });

        sidebarPanel.add(logoutButton);
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(44, 62, 80));
        button.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
     
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(44, 62, 80));
            }
        });

        button.addActionListener(e -> {
            cardLayout.show(mainPanel, cardName);
            updateHeaderTitle(getPanelTitle(cardName));
        });

        return button;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Principal");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(Color.WHITE);
        JLabel userLabel = new JLabel("Administrador");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.GRAY);

        userPanel.add(userLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel dashboardPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        dashboardPanel.setBackground(Color.WHITE);
        dashboardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
       
        String[] stats = {"Total Usuarios", "Usuarios Activos", "Administradores", "Veterinarios"};
        String[] values = {"0", "0", "1", "0"};
        Color[] colors = {
            new Color(41, 128, 185), new Color(39, 174, 96), 
            new Color(142, 68, 173), new Color(230, 126, 34)
        };

        for (int i = 0; i < stats.length; i++) {
            dashboardPanel.add(createStatCard(stats[i], values[i], colors[i]));
        }

        return dashboardPanel;
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 240, 240)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(Color.GRAY);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(titleLabel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createStyledPanel(String content) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel label = new JLabel(content);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(Color.GRAY);
        
        panel.add(label);
        return panel;
    }

    private JPanel wrapPanel(JPanel innerPanel, String title) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(20, 20, 20, 20));
        wrapper.add(innerPanel, BorderLayout.CENTER);
        return wrapper;
    }

    private String getPanelTitle(String cardName) {
        switch (cardName) {
            case "DASHBOARD": return "Estadisticas";
            case "CREAR": return "Gestión de Usuarios - Crear";
            case "EDITAR": return "Gestión de Usuarios - Editar";
            case "DESACTIVAR": return "Gestión de Usuarios - Desactivar";
            case "RESTABLECER": return "Gestión de Usuarios - Restablecer Contraseña";
            case "LISTAR": return "Gestión de Usuarios - Listar/Buscar";
            default: return "Panel de Administración";
        }
    }

    private void updateHeaderTitle(String title) {
        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                Component[] headerComps = ((JPanel) comp).getComponents();
                for (Component headerComp : headerComps) {
                    if (headerComp instanceof JLabel) {
                        ((JLabel) headerComp).setText(title);
                        return;
                    }
                }
            }
        }
    }
	   
}
