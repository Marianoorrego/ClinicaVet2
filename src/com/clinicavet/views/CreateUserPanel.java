package com.clinicavet.views;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;

public class CreateUserPanel extends JPanel {

    private final UserService userService;
    private final RolService rolService;

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JComboBox<Rol> cmbRoles;
    private JButton btnSave;

    public CreateUserPanel(UserService userService, RolService rolService) {
        this.userService = userService;
        this.rolService = rolService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        
        JLabel titleLabel = new JLabel("Crear Nuevo Usuario");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JPanel formPanel = createFormPanel();    
        
        JPanel buttonPanel = createButtonPanel();

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(51, 51, 51);
        Color fieldBorderColor = new Color(200, 200, 200);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        JLabel lblName = new JLabel("Nombre Completo:");
        lblName.setFont(labelFont);
        lblName.setForeground(labelColor);
        formPanel.add(lblName, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtName = new JTextField();
        txtName.setFont(fieldFont);
        txtName.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fieldBorderColor),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(txtName, gbc);
       
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.3;
        JLabel lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setFont(labelFont);
        lblEmail.setForeground(labelColor);
        formPanel.add(lblEmail, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtEmail = new JTextField();
        txtEmail.setFont(fieldFont);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fieldBorderColor),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(txtEmail, gbc);
       
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.3;
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(labelFont);
        lblPassword.setForeground(labelColor);
        formPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        txtPassword = new JPasswordField();
        txtPassword.setFont(fieldFont);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fieldBorderColor),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.3;
        JLabel lblRol = new JLabel("Rol:");
        lblRol.setFont(labelFont);
        lblRol.setForeground(labelColor);
        formPanel.add(lblRol, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        cmbRoles = new JComboBox<>();
        cmbRoles.setFont(fieldFont);
        cmbRoles.setBackground(Color.WHITE);
        cmbRoles.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(fieldBorderColor),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        for (Rol rol : rolService.listRoles()) {
            cmbRoles.addItem(rol);
        }
    
        cmbRoles.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Rol) {
                    setText(((Rol) value).getName());
                }
                return this;
            }
        });

        formPanel.add(cmbRoles, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        btnSave = new JButton("Crear Usuario");
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSave.setBackground(new Color(39, 174, 96)); 
        btnSave.setForeground(Color.WHITE);
        btnSave.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnSave.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSave.setBackground(new Color(46, 204, 113));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSave.setBackground(new Color(39, 174, 96));
            }
        });

        btnSave.addActionListener(e -> onCreateUser());

        JButton btnClear = new JButton("Limpiar");
        btnClear.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnClear.setBackground(new Color(149, 165, 166));
        btnClear.setForeground(Color.WHITE);
        btnClear.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnClear.setFocusPainted(false);
        btnClear.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnClear.setBackground(new Color(169, 185, 186));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnClear.setBackground(new Color(149, 165, 166));
            }
        });

        btnClear.addActionListener(e -> clearForm());

        buttonPanel.add(btnClear);
        buttonPanel.add(btnSave);

        return buttonPanel;
    }

    private void onCreateUser() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        Rol selectedRol = (Rol) cmbRoles.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || selectedRol == null) {
            showError("Por favor, complete todos los campos.");
            return;
        }

        if (!isValidEmail(email)) {
            showError("Por favor, ingrese un correo electrónico válido.");
            return;
        }

        if (password.length() < 6) {
            showError("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        try {
            User user = new User(name, email, password, selectedRol);
            userService.createUser(user);

            showSuccess("Usuario creado exitosamente.");
            clearForm();

        } catch (Exception ex) {
            showError("Error al crear el usuario: " + ex.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void clearForm() {
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        if (cmbRoles.getItemCount() > 0) {
            cmbRoles.setSelectedIndex(0);
        }
        txtName.requestFocusInWindow();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void refreshRoles() {
        cmbRoles.removeAllItems();
        for (Rol rol : rolService.listRoles()) {
            cmbRoles.addItem(rol);
        }
    }
}
