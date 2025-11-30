package com.clinicavet.model.notifications;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manager de Notificaciones - Patrón Observer Singleton
 * Gestiona el registro de observadores y envío de notificaciones
 */
public class AppointmentNotificationManager {

    private static AppointmentNotificationManager instance;
    private List<INotificationObserver> observers;

    private AppointmentNotificationManager() {
        this.observers = new ArrayList<>();
        System.out.println("✅ [AppointmentNotificationManager] Singleton inicializado");
    }

    /**
     * Obtener la instancia singleton
     */
    public static synchronized AppointmentNotificationManager getInstance() {
        if (instance == null) {
            instance = new AppointmentNotificationManager();
        }
        return instance;
    }

    /**
     * Registrar un observador
     */
    public void registerObserver(INotificationObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("✅ [AppointmentNotificationManager] Observador registrado. Total: " + observers.size());
        }
    }

    /**
     * Desregistrar un observador
     */
    public void unregisterObserver(INotificationObserver observer) {
        if (observers.remove(observer)) {
            System.out.println("✅ [AppointmentNotificationManager] Observador desregistrado. Total: " + observers.size());
        }
    }

    /**
     * Obtener cantidad de observadores
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * ✅ CORREGIDO: Notificar a todos los observadores sobre cancelación de cita
     * Recibe AppointmentNotification (no appointment, medico por separado)
     */
    public void notifyAppointmentCancelled(Appointment appointment, User medico) {
        System.out.println("\n📢 [AppointmentNotificationManager] Notificando cancelación de cita a auxiliares...");
        System.out.println("   📊 Total de observadores: " + observers.size());
        
        // Crear notificación
        AppointmentNotification notification = new AppointmentNotification(
            appointment, 
            medico, 
            AppointmentNotification.NotificationType.APPOINTMENT_CANCELLED,
            java.time.LocalDateTime.now()
        );
        
        for (INotificationObserver observer : observers) {
            observer.onAppointmentCancelled(notification);
        }
        
        System.out.println("✅ Todos los observadores fueron notificados");
    }

    /**
     * ✅ CORREGIDO: Notificar a todos los observadores sobre disponibilidad
     */
    public void notifyAvailability(int availableSlots, LocalDate availableDate, User medico) {
        System.out.println("\n📢 [AppointmentNotificationManager] Notificando disponibilidad a auxiliares...");
        System.out.println("   📊 Total de observadores: " + observers.size());
        
        // Crear notificación de disponibilidad
        AppointmentNotification notification = new AppointmentNotification(
            null,  // No hay appointment para notificaciones de disponibilidad
            medico,
            AppointmentNotification.NotificationType.AVAILABILITY_UPDATED,
            java.time.LocalDateTime.now(),
            availableSlots,
            availableDate
        );
        
        for (INotificationObserver observer : observers) {
            observer.onAvailabilityUpdated(notification);
        }
        
        System.out.println("✅ Todos los observadores fueron notificados");
    }

    /**
     * Obtener lista de observadores (para debugging)
     */
    public List<INotificationObserver> getObservers() {
        return new ArrayList<>(observers);
    }
}