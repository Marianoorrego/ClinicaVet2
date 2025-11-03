package com.clinicavet.views;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListUsersPanel extends JPanel {

    private final UserService userService;
    private JTable table;
    private DefaultTableModel tableModel;

    public ListUsersPanel(UserService userService) {
        this.userService = userService;
        initComponents();
        loadUsers();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Listado de Usuarios", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Nombre", "Email", "Rol", "Activo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnRefresh = new JButton("Refrescar");
        btnRefresh.setBackground(new Color(66, 139, 202));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        btnRefresh.addActionListener(e -> loadUsers());

        buttonPanel.add(btnRefresh);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadUsers() {
        tableModel.setRowCount(0); 
        List<User> users = userService.listUsers();

        for (User user : users) {
            tableModel.addRow(new Object[]{
                    user.getName(),
                    user.geteMail(),
                    user.getRol() != null ? user.getRol().getName() : "Sin rol",
                    user.isActivo() ? "Sí" : "No"
            });
        }
    }
}

