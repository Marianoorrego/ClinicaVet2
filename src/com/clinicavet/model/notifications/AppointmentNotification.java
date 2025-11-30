package com.clinicavet.model.notifications;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una notificación de cita
 * Almacena información sobre eventos de citas para los auxiliares
 */
public class AppointmentNotification {
    
    public enum NotificationType {
        APPOINTMENT_CANCELLED("Cita Cancelada"),
        AVAILABILITY_UPDATED("Disponibilidad Actualizada");
        
        private final String displayName;
        
        NotificationType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    private UUID id;
    private Appointment appointment;
    private User medico;
    private NotificationType type;
    private LocalDateTime createdAt;
    private boolean read;
    private int availableSlots;
    private LocalDate availableDate;
    
    /**
     * Constructor para notificaciones de citas canceladas
     */
    public AppointmentNotification(Appointment appointment, User medico, 
                                   NotificationType type, LocalDateTime createdAt) {
        this.id = UUID.randomUUID();
        this.appointment = appointment;
        this.medico = medico;
        this.type = type;
        this.createdAt = createdAt;
        this.read = false;
    }
    
    /**
     * Constructor para notificaciones de disponibilidad
     */
    public AppointmentNotification(Appointment appointment, User medico, 
                                   NotificationType type, LocalDateTime createdAt,
                                   int availableSlots, LocalDate availableDate) {
        this(appointment, medico, type, createdAt);
        this.availableSlots = availableSlots;
        this.availableDate = availableDate;
    }
    
    // ========== GETTERS Y SETTERS ==========
    
    public UUID getId() {
        return id;
    }
    
    public Appointment getAppointment() {
        return appointment;
    }
    
    public User getMedico() {
        return medico;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public boolean isRead() {
        return read;
    }
    
    public void setRead(boolean read) {
        this.read = read;
    }
    
    public int getAvailableSlots() {
        return availableSlots;
    }
    
    public LocalDate getAvailableDate() {
        return availableDate;
    }
    
    @Override
    public String toString() {
        if (type == NotificationType.APPOINTMENT_CANCELLED) {
            return "📅 Cita cancelada: " + appointment.getMascota().getName() + 
                   " (" + appointment.getFecha() + ") - Dr. " + medico.getName();
        } else {
            return "📊 Disponibilidad: Dr. " + medico.getName() + 
                   " - " + availableSlots + " slots (" + availableDate + ")";
        }
    }
}