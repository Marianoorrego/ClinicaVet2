package com.clinicavet.model.notifications;

/**
 * Interfaz Observer para el patrón Observer
 * Notifica a los observadores sobre cambios en citas
 */
public interface INotificationObserver {
    
    /**
     * Se invoca cuando una cita es cancelada
     * @param notification Notificación con detalles de la cancelación
     */
    void onAppointmentCancelled(AppointmentNotification notification);
    
    /**
     * Se invoca cuando se calcula disponibilidad
     * @param notification Notificación con detalles de disponibilidad
     */
    void onAvailabilityUpdated(AppointmentNotification notification);
    
    /**
     * Obtener el ID del observador (para identificarlo)
     */
    String getObserverId();
}