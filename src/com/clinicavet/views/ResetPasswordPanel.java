package com.clinicavet.views;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.UserService;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class ResetPasswordPanel extends JPanel {

    private final UserService userService;
    private JTextField txtSearchEmail;
    private JPasswordField txtNewPassword;
    private JButton btnSearch;
    private JButton btnSave;
    private JButton btnCancel;
    private JLabel lblResult;
    private User foundUser;

    public ResetPasswordPanel(UserService userService) {
        this.userService = userService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Restablecer contraseña", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(lblTitle, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(new JLabel("Buscar por e-mail:"), gbc);

        txtSearchEmail = new JTextField();
        txtSearchEmail.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        contentPanel.add(txtSearchEmail, gbc);

        btnSearch = new JButton("Buscar");
        btnSearch.setBackground(new Color(66, 139, 202));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        gbc.gridx = 2;
        contentPanel.add(btnSearch, gbc);

        lblResult = new JLabel(" ");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        contentPanel.add(lblResult, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(new JLabel("Nueva contraseña:"), gbc);

        txtNewPassword = new JPasswordField();
        txtNewPassword.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        contentPanel.add(txtNewPassword, gbc);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        btnSave = new JButton("Guardar");
        btnSave.setBackground(new Color(92, 184, 92));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);

        btnCancel = new JButton("Cancelar");
        btnCancel.setBackground(new Color(217, 83, 79));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        add(buttonPanel, BorderLayout.SOUTH);

        btnSearch.addActionListener(e -> searchUser());
        btnSave.addActionListener(e -> resetPassword());
        btnCancel.addActionListener(e -> clearForm());
    }

    private void searchUser() {
        String email = txtSearchEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un e-mail para buscar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Optional<User> userOpt = userService.getByEmail(email);
        if (userOpt.isPresent()) {
            foundUser = userOpt.get();
            lblResult.setText("Encontrado: Usuario: " + foundUser.getName() + " correo: " + foundUser.geteMail());
        } else {
            foundUser = null;
            lblResult.setText("Usuario no encontrado");
        }
    }

    private void resetPassword() {
        if (foundUser == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un usuario válido", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newPassword = new String(txtNewPassword.getPassword()).trim();
        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una nueva contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        foundUser.setPassword(newPassword);
        userService.update(foundUser);

        JOptionPane.showMessageDialog(this, "Contraseña actualizada exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        clearForm();
    }

    private void clearForm() {
        txtSearchEmail.setText("");
        txtNewPassword.setText("");
        lblResult.setText(" ");
        foundUser = null;
    }
}
