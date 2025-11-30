package com.clinicavet.model.notifications;

import com.clinicavet.model.entities.User;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Observador de Notificaciones para Auxiliares
 * Implementa el patrón Observer para recibir notificaciones de citas
 */
public class AuxiliarNotificationObserver implements INotificationObserver {

    private String observerId;
    private User auxiliar;
    private List<AppointmentNotification> notifications;
    private LocalDateTime createdAt;

    public AuxiliarNotificationObserver(User auxiliar) {
        this.auxiliar = auxiliar;
        this.observerId = "AUX-" + UUID.randomUUID() + "-" + auxiliar.getName();
        this.notifications = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        
        System.out.println("✅ [AuxiliarNotificationObserver] Observador creado para: " + auxiliar.getName());
    }

    /**
     * ✅ CORREGIDO: Recibe AppointmentNotification
     */
    @Override
    public void onAppointmentCancelled(AppointmentNotification notification) {
        synchronized (notifications) {
            notifications.add(notification);
            System.out.println("🔔 [" + auxiliar.getName() + "] Nueva notificación: Cita Cancelada");
            System.out.println("   Cita: " + notification.getAppointment().getMascota().getName());
            System.out.println("   Total de notificaciones: " + notifications.size());
        }
    }

    /**
     * ✅ CORREGIDO: Recibe AppointmentNotification
     */
    @Override
    public void onAvailabilityUpdated(AppointmentNotification notification) {
        synchronized (notifications) {
            notifications.add(notification);
            System.out.println("🔔 [" + auxiliar.getName() + "] Nueva notificación: Disponibilidad Actualizada");
            System.out.println("   Médico: " + notification.getMedico().getName());
            System.out.println("   Slots disponibles: " + notification.getAvailableSlots());
            System.out.println("   Total de notificaciones: " + notifications.size());
        }
    }

    /**
     * Obtener todas las notificaciones
     */
    public List<AppointmentNotification> getNotifications() {
        synchronized (notifications) {
            return new ArrayList<>(notifications);
        }
    }

    /**
     * Obtener solo notificaciones sin leer
     */
    public List<AppointmentNotification> getUnreadNotifications() {
        synchronized (notifications) {
            return notifications.stream()
                    .filter(n -> !n.isRead())
                    .collect(Collectors.toList());
        }
    }

    /**
     * Marcar una notificación como leída
     */
    public void markNotificationAsRead(AppointmentNotification notification) {
        synchronized (notifications) {
            if (notifications.contains(notification)) {
                notification.setRead(true);
                System.out.println("✅ Notificación marcada como leída");
            }
        }
    }

    /**
     * Limpiar todas las notificaciones
     */
    public void clearAllNotifications() {
        synchronized (notifications) {
            System.out.println("🗑️  Eliminando " + notifications.size() + " notificaciones...");
            notifications.clear();
            System.out.println("✅ Todas las notificaciones eliminadas");
        }
    }

    /**
     * Obtener cantidad de notificaciones
     */
    public int getNotificationCount() {
        synchronized (notifications) {
            return notifications.size();
        }
    }

    /**
     * Obtener cantidad de notificaciones sin leer
     */
    public int getUnreadCount() {
        return (int) getUnreadNotifications().size();
    }

    @Override
    public String getObserverId() {
        return observerId;
    }

    public User getAuxiliar() {
        return auxiliar;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}