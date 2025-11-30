package com.clinicavet.controllers;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.notifications.AuxiliarNotificationObserver;
import com.clinicavet.model.notifications.AppointmentNotificationManager;
import com.clinicavet.views.NotificationsPanel;

import javax.swing.*;

/**
 * Controlador de Notificaciones para Auxiliares
 * Implementa el patrón Observer del RFC9
 */
public class NotificationsController {
    
    private NotificationsPanel view;
    private AuxiliarNotificationObserver observer;
    private User auxiliar;
    private AppointmentNotificationManager notificationManager;
    
    public NotificationsController(NotificationsPanel view, User auxiliar) {
        this.view = view;
        this.auxiliar = auxiliar;
        this.notificationManager = AppointmentNotificationManager.getInstance();
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔄 [NotificationsController] Inicializando para auxiliar...");
        System.out.println("=".repeat(80));
        
        // ✅ PASO 1: Crear observador para este auxiliar
        System.out.println("\n📊 Paso 1: Crear observador");
        this.observer = new AuxiliarNotificationObserver(auxiliar);
        System.out.println("   ✅ Observador creado: " + observer.getObserverId());
        System.out.println("   Usuario: " + auxiliar.getName());
        
        // ✅ PASO 2: PRIMERO - Asignar observador al panel
        System.out.println("\n📋 Paso 2: Asignar observador al panel");
        this.view.setObserver(observer);
        System.out.println("   ✅ Observador asignado al panel");
        
        // ✅ PASO 3: SEGUNDO - Registrar el observador en el manager
        System.out.println("\n🔗 Paso 3: Registrar en AppointmentNotificationManager");
        this.notificationManager.registerObserver(observer);
        System.out.println("   ✅ Observador registrado correctamente");
        System.out.println("   📊 Total de auxiliares registrados: " + notificationManager.getObserverCount());
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✅ NotificationsController inicializado correctamente");
        System.out.println("=".repeat(80) + "\n");
        
        setupListeners();
    }
    
    /**
     * Configurar listeners de botones
     */
    private void setupListeners() {
        System.out.println("🎮 Configurando listeners de botones...");
        
        // Botón: Marcar como leída
        view.getBtnMarkAsRead().addActionListener(e -> markAsRead());
        System.out.println("   ✅ Listener 'Marcar como leída' configurado");
        
        // Botón: Limpiar todo
        view.getBtnClearAll().addActionListener(e -> clearAll());
        System.out.println("   ✅ Listener 'Limpiar todo' configurado");
    }
    
    /**
     * Marcar notificación seleccionada como leída
     */
    private void markAsRead() {
        System.out.println("\n📖 Marcando notificación como leída...");
        int selectedRow = view.getSelectedRow();
        
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, 
                "Selecciona una notificación", 
                "ℹ️ Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        var notifications = observer.getNotifications();
        if (selectedRow < notifications.size()) {
            observer.markNotificationAsRead(notifications.get(selectedRow));
            view.loadNotifications();
            System.out.println("✅ Notificación marcada como leída");
        }
    }
    
    /**
     * Limpiar todas las notificaciones
     */
    private void clearAll() {
        System.out.println("\n🗑️  Mostrando diálogo de confirmación...");
        int confirm = JOptionPane.showConfirmDialog(view,
            "¿Limpiar todas las notificaciones?",
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("   ✅ Usuario confirmó limpieza");
            System.out.println("   🗑️  Limpiando todas las notificaciones...");
            observer.clearAllNotifications();
            view.loadNotifications();
            System.out.println("✅ Todas las notificaciones fueron eliminadas");
        } else {
            System.out.println("   ❌ Usuario canceló limpieza");
        }
    }
    
    /**
     * Refrescar vista de notificaciones
     */
    public void refresh() {
        if (view != null && observer != null) {
            view.loadNotifications();
        }
    }
    
    /**
     * Desregistrar observador al cerrar sesión
     */
    public void unregister() {
        if (observer != null) {
            System.out.println("🔌 Desregistrando observador...");
            notificationManager.unregisterObserver(observer);
            System.out.println("✅ Observador desregistrado: " + auxiliar.getName());
        }
    }
    
    /**
     * Obtener el observador
     */
    public AuxiliarNotificationObserver getObserver() {
        return observer;
    }
    
    /**
     * Obtener el panel
     */
    public NotificationsPanel getView() {
        return view;
    }
}