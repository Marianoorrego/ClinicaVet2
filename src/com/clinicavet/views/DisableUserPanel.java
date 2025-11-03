package com.clinicavet.views;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.UserService;
import com.clinicavet.model.services.RolService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class DisableUserPanel extends JPanel {

    private final UserService userService;
    private final RolService rolService;

    private JTextField txtSearch;
    private JButton btnSearch, btnDisable;
    private JLabel lblResult;
    private User foundUser;

    public DisableUserPanel(UserService userService, RolService rolService) {
        this.userService = userService;
        this.rolService = rolService;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Desactivar Usuario", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(60, 60, 60));

        add(title, BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Buscar por email o nombre:"), gbc);

        gbc.gridx = 1;
        txtSearch = new JTextField();
        txtSearch.setPreferredSize(new Dimension(220, 30));
        formPanel.add(txtSearch, gbc);

        gbc.gridx = 2;
        btnSearch = new JButton("Buscar");
        btnSearch.addActionListener(e -> searchUser());
        formPanel.add(btnSearch, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        lblResult = new JLabel(" ");
        lblResult.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(lblResult, gbc);

        gbc.gridy = 2;
        btnDisable = new JButton("Desactivar Usuario");
        btnDisable.setBackground(new Color(200, 0, 0));
        btnDisable.setForeground(Color.WHITE);
        btnDisable.setEnabled(false); 
        btnDisable.addActionListener(e -> disableUser());
        formPanel.add(btnDisable, gbc);

        return formPanel;
    }

    private void searchUser() {
        String query = txtSearch.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un email o nombre para buscar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<User> userOpt = userService.getByEmail(query);
        if (!userOpt.isPresent()) {
            userOpt = userService.getByName(query);
        }

        if (userOpt.isPresent()) {
            foundUser = userOpt.get();
            lblResult.setText("Encontrado: " + foundUser.toString());
            btnDisable.setEnabled(true);
        } else {
            lblResult.setText("No se encontró ningún usuario con: " + query);
            btnDisable.setEnabled(false);
        }
    }

    private void disableUser() {
    	if (foundUser != null) {
    		try {
                userService.deactivateUser(foundUser.getId()); 
                JOptionPane.showMessageDialog(this, "Usuario desactivado exitosamente");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }

            txtSearch.setText("");
            lblResult.setText(" ");
            btnDisable.setEnabled(false);
            foundUser = null;
        }
    }
}

