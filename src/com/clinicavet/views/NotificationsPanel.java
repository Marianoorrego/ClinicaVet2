package com.clinicavet.views;

import com.clinicavet.model.notifications.AppointmentNotification;
import com.clinicavet.model.notifications.AuxiliarNotificationObserver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationsPanel extends JPanel {
    
    private JTable tableNotifications;
    private DefaultTableModel tableModel;
    private JLabel lblUnreadCount;
    private JButton btnMarkAsRead;
    private JButton btnClearAll;
    private AuxiliarNotificationObserver observer;
    private javax.swing.Timer refreshTimer;

    /**
     * Constructor - observer puede ser null, se asigna después
     */
    public NotificationsPanel(AuxiliarNotificationObserver observer) {
        this.observer = observer;
        initComponents();
        if (observer != null) {
            loadNotifications();
            startAutoRefresh();
        }
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        
        // === PANEL SUPERIOR ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(52, 152, 219));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel lblTitle = new JLabel("🔔 NOTIFICACIONES");
        lblTitle.setFont(new Font("DejaVu Sans", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        topPanel.add(lblTitle);
        
        lblUnreadCount = new JLabel("(0 sin leer)");
        lblUnreadCount.setFont(new Font("DejaVu Sans", Font.PLAIN, 12));
        lblUnreadCount.setForeground(new Color(46, 204, 113));
        topPanel.add(Box.createHorizontalStrut(20));
        topPanel.add(lblUnreadCount);
        
        add(topPanel, BorderLayout.NORTH);
        
        // === TABLA CENTRAL ===
        tableModel = new DefaultTableModel(
            new Object[]{"Tipo", "Descripción", "Fecha", "Estado"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableNotifications = new JTable(tableModel);
        tableNotifications.setRowHeight(35);
        tableNotifications.setFont(new Font("DejaVu Sans", Font.PLAIN, 11));
        tableNotifications.getTableHeader().setFont(new Font("DejaVu Sans", Font.BOLD, 12));
        tableNotifications.setBackground(Color.WHITE);
        tableNotifications.setSelectionBackground(new Color(52, 152, 219));
        tableNotifications.setSelectionForeground(Color.WHITE);
        
        // Ajustar ancho de columnas
        tableNotifications.getColumnModel().getColumn(0).setPreferredWidth(150);
        tableNotifications.getColumnModel().getColumn(1).setPreferredWidth(350);
        tableNotifications.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableNotifications.getColumnModel().getColumn(3).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(tableNotifications);
        add(scrollPane, BorderLayout.CENTER);
        
        // === PANEL INFERIOR ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(Color.WHITE);
        
        btnMarkAsRead = new JButton("✓ Marcar como leída");
        btnMarkAsRead.setPreferredSize(new Dimension(170, 35));
        btnMarkAsRead.setFont(new Font("DejaVu Sans", Font.PLAIN, 11));
        btnMarkAsRead.setBackground(new Color(46, 204, 113));
        btnMarkAsRead.setForeground(Color.WHITE);
        btnMarkAsRead.setFocusPainted(false);
        bottomPanel.add(btnMarkAsRead);
        
        btnClearAll = new JButton("🗑️ Limpiar todo");
        btnClearAll.setPreferredSize(new Dimension(130, 35));
        btnClearAll.setFont(new Font("DejaVu Sans", Font.PLAIN, 11));
        btnClearAll.setBackground(new Color(231, 76, 60));
        btnClearAll.setForeground(Color.WHITE);
        btnClearAll.setFocusPainted(false);
        bottomPanel.add(btnClearAll);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * ✅ NUEVO: Iniciar refresh automático cada 2 segundos
     */
    private void startAutoRefresh() {
        System.out.println("⏱️  Iniciando auto-refresh de notificaciones (cada 2s)...");
        
        refreshTimer = new javax.swing.Timer(2000, e -> {
            if (observer != null) {
                loadNotifications();
            }
        });
        refreshTimer.start();
    }
    
    /**
     * Cargar notificaciones en la tabla
     */
    public void loadNotifications() {
        tableModel.setRowCount(0);
        
        if (observer == null) {
            tableModel.addRow(new Object[]{"ℹ️", "Esperando notificaciones...", "", ""});
            lblUnreadCount.setText("(0 sin leer)");
            return;
        }
        
        List<AppointmentNotification> notifications = observer.getNotifications();
        int unreadCount = observer.getUnreadNotifications().size();
        
        if (notifications.isEmpty()) {
            tableModel.addRow(new Object[]{"ℹ️", "No hay notificaciones", "", ""});
        } else {
            for (AppointmentNotification notif : notifications) {
                String tipo = notif.getType() == AppointmentNotification.NotificationType.APPOINTMENT_CANCELLED
                    ? "📅 Cita Cancelada"
                    : "📊 Disponibilidad";
                
                String descripcion = notif.toString();
                String fecha = notif.getCreatedAt().toLocalDate().toString();
                String estado = notif.isRead() ? "✓ Leída" : "● Nueva";
                
                tableModel.addRow(new Object[]{tipo, descripcion, fecha, estado});
            }
        }
        
        lblUnreadCount.setText("(" + unreadCount + " sin leer)");
    }
    
    public void stopAutoRefresh() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
            System.out.println("⏸️  Auto-refresh detenido");
        }
    }

    /**
     * Asignar el observador (se hace en el controlador)
     */
    public void setObserver(AuxiliarNotificationObserver observer) {
        this.observer = observer;
        if (observer != null) {
            loadNotifications();
            startAutoRefresh();
        }
    }

    
    public JButton getBtnMarkAsRead() {
        return btnMarkAsRead;
    }
    
    public JButton getBtnClearAll() {
        return btnClearAll;
    }
    
    public int getSelectedRow() {
        return tableNotifications.getSelectedRow();
    }
    
    public AuxiliarNotificationObserver getObserver() {
        return observer;
    }

}