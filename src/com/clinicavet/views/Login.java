package com.clinicavet.views;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


import com.clinicavet.controllers.LoginControler;

import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Font;


public class Login extends JFrame {	
	private final LoginControler loginControler;
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public Login(LoginControler loginControler) {
        this.loginControler = loginControler;
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Clínica Veterinaria");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal con borde y márgenes
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // Panel del título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel clinicLabel = new JLabel("Clínica Veterinaria", SwingConstants.CENTER);
        clinicLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        clinicLabel.setForeground(new Color(0, 102, 204)); // Azul profesional

        titlePanel.add(clinicLabel, BorderLayout.NORTH);
       

        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 15));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Panel para email
        JPanel emailPanel = new JPanel(new BorderLayout(10, 5));
        emailPanel.setBackground(Color.WHITE);
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblEmail.setForeground(new Color(51, 51, 51));
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        emailPanel.add(lblEmail, BorderLayout.WEST);
        emailPanel.add(txtEmail, BorderLayout.CENTER);

        // Panel para contraseña
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 5));
        passwordPanel.setBackground(Color.WHITE);
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPassword.setForeground(new Color(51, 51, 51));
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        passwordPanel.add(lblPassword, BorderLayout.WEST);
        passwordPanel.add(txtPassword, BorderLayout.CENTER);

        // Botón de login
        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para el botón
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 82, 184));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(0, 102, 204));
            }
        });

        btnLogin.addActionListener(e -> onLogin());

        // Agregar componentes al formulario
        formPanel.add(emailPanel);
        formPanel.add(passwordPanel);
        formPanel.add(btnLogin);

        // Agregar todo al panel principal
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
    }

    private void onLogin() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        boolean success = loginControler.login(email, password);

        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Inicio de sesión exitoso",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            loginControler.redirectToMainMenu();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
