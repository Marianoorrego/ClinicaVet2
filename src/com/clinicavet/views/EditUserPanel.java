package com.clinicavet.views;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.services.UserService;
import com.clinicavet.model.services.RolService;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.util.List;
import java.util.Optional;

public class EditUserPanel extends JPanel {

    private final UserService userService;
    private final RolService rolService;

    private JTextField txtSearchEmail;
    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cbRoles;

    private User usuarioActual;

    public EditUserPanel(UserService userService, RolService rolService) {
        this.userService = userService;
        this.rolService = rolService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        JLabel headerLabel = new JLabel("Editar Usuario", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(44, 62, 80));
        add(headerLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Buscar por Email:"), gbc);

        txtSearchEmail = new JTextField();
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0; 
        formPanel.add(txtSearchEmail, gbc);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        gbc.gridx = 3; gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0; 
        formPanel.add(btnBuscar, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        txtName = new JTextField();
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtName, gbc);

        // --- Email ---
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Email:"), gbc);

        txtEmail = new JTextField();
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        txtPassword = new JPasswordField();
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        formPanel.add(new JLabel("Rol:"), gbc);

        cbRoles = new JComboBox<>();
        loadRoles();
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        formPanel.add(cbRoles, gbc);

        add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        JButton btnGuardar = new JButton("Guardar Cambios");
        btnGuardar.setBackground(new Color(39, 174, 96));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);

        buttonPanel.add(btnGuardar);
        buttonPanel.add(btnCancelar);

        add(buttonPanel, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> buscarUsuario());
        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> limpiarCampos());

        bloquearCampos(false);
    }

    private void loadRoles() {
        List<Rol> roles = rolService.listRoles();
        for (Rol rol : roles) {
            cbRoles.addItem(rol);
        }
    }

    private void buscarUsuario() {
        String email = txtSearchEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un email para buscar.");
            return;
        }

        Optional<User> usuarioOpt = userService.getByEmail(email);
        if (usuarioOpt.isPresent()) {
            usuarioActual = usuarioOpt.get();
            txtName.setText(usuarioActual.getName());
            txtEmail.setText(usuarioActual.geteMail());
            txtPassword.setText(usuarioActual.getPassword());
            cbRoles.setSelectedItem(usuarioActual.getRol());
            bloquearCampos(true);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado.");
            limpiarCampos();
            bloquearCampos(false);
        }
    }

    private void guardarCambios() {
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(this, "Primero busque un usuario.");
            return;
        }

        usuarioActual.setName(txtName.getText().trim());
        usuarioActual.seteMail(txtEmail.getText().trim());
        usuarioActual.setPassword(new String(txtPassword.getPassword()).trim());
        usuarioActual.setRol((Rol) cbRoles.getSelectedItem());

        userService.update(usuarioActual);

        JOptionPane.showMessageDialog(this, "Usuario actualizado correctamente.");
        limpiarCampos();
        bloquearCampos(false);
    }

    private void limpiarCampos() {
        txtSearchEmail.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cbRoles.setSelectedIndex(-1);
        usuarioActual = null;
    }

    private void bloquearCampos(boolean enable) {
        txtName.setEnabled(enable);
        txtEmail.setEnabled(enable);
        txtPassword.setEnabled(enable);
        cbRoles.setEnabled(enable);
    }
}

