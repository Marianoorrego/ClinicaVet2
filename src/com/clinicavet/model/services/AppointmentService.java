package com.clinicavet.model.services;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Estado;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.notifications.AppointmentNotificationManager;
import com.clinicavet.model.repositories.IAppointmentRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio de Citas - RFC9: Notificaciones de Disponibilidad
 */
public class AppointmentService implements IAppointmentService {

    private IAppointmentRepository appointmentRepository;
    private AppointmentNotificationManager notificationManager;

    public AppointmentService(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
        this.notificationManager = AppointmentNotificationManager.getInstance();
        System.out.println("✅ [AppointmentService] Inicializado");
    }

    @Override
    public boolean createAppointment(Appointment appointment) {
        System.out.println("\n📅 [AppointmentService] Creando cita...");
        
        // Validar solapamiento de citas
        if (appointmentRepository.hasOverlap(appointment.getMedico(), appointment.getFecha(), 
                                            appointment.getHora(), appointment.getDuracion(), null)) {
            System.err.println("❌ Solapamiento de citas detectado");
            return false;
        }
        
        appointmentRepository.addAppointment(appointment);
        System.out.println("✅ Cita creada: " + appointment.getMascota().getName() + 
                         " - Dr. " + appointment.getMedico().getName());
        return true;
    }

    @Override
    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public Optional<Appointment> getAppointmentById(UUID id) {
        return appointmentRepository.findById(id);
    }

    @Override
    public List<Appointment> getAppointmentsByMedico(User medico) {
        return appointmentRepository.findByMedico(medico);
    }

    @Override
    public List<Appointment> getAppointmentsByMascota(Pet mascota) {
        return appointmentRepository.findByMascota(mascota);
    }

    @Override
    public List<Appointment> getAppointmentsByFecha(LocalDate fecha) {
        return appointmentRepository.findByFecha(fecha);
    }

    @Override
    public boolean updateAppointment(Appointment appointment) {
        System.out.println("\n📅 [AppointmentService] Actualizando cita...");
        
        // Validar solapamiento excluyendo la cita actual
        if (appointmentRepository.hasOverlap(appointment.getMedico(), appointment.getFecha(), 
                                            appointment.getHora(), appointment.getDuracion(), 
                                            appointment.getId())) {
            System.err.println("❌ Solapamiento de citas detectado");
            return false;
        }
        
        appointmentRepository.update(appointment);
        System.out.println("✅ Cita actualizada");
        return true;
    }

    @Override
    public void deleteAppointment(UUID id) {
        System.out.println("\n📅 [AppointmentService] Eliminando cita...");
        appointmentRepository.delete(id);
        System.out.println("✅ Cita eliminada");
    }

    /**
     * ✅ NUEVO RFC9: Cancelar cita y notificar a auxiliares
     * 
     * Flujo:
     * 1. Cambiar estado a CANCELADA
     * 2. Guardar cambios
     * 3. Notificar a todos los auxiliares sobre la cancelación
     * 4. Calcular disponibilidades liberadas para fechas >= fecha cancelada
     * 5. Notificar sobre disponibilidades encontradas
     */
    @Override
    public void cancelAppointmentAndNotify(UUID appointmentId) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("🔄 [AppointmentService] RFC9 - Cancelando cita y notificando...");
        System.out.println("=".repeat(80));
        
        // Obtener la cita
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        
        if (appointmentOpt.isEmpty()) {
            System.err.println("❌ Cita no encontrada con ID: " + appointmentId);
            return;
        }
        
        Appointment appointment = appointmentOpt.get();
        User medico = appointment.getMedico();
        LocalDate cancelledDate = appointment.getFecha();
        LocalTime cancelledTime = appointment.getHora();
        int duration = appointment.getDuracion();
        
        // === PASO 1: Cambiar estado a CANCELADA ===
        System.out.println("\n📝 Paso 1: Cambiar estado a CANCELADA");
        appointment.setEstado(Estado.CANCELADA);
        appointmentRepository.update(appointment);
        
        System.out.println("✅ Cita cancelada exitosamente");
        System.out.println("   ID: " + appointmentId);
        System.out.println("   Médico: " + medico.getName());
        System.out.println("   Fecha: " + cancelledDate);
        System.out.println("   Hora: " + cancelledTime);
        System.out.println("   Mascota: " + appointment.getMascota().getName());
        System.out.println("   Duración liberada: " + duration + " minutos");
        
        // === PASO 2: Notificar a observadores sobre la cancelación ===
        System.out.println("\n🔔 Paso 2: Notificando a auxiliares sobre la cancelación");
        notificationManager.notifyAppointmentCancelled(appointment, medico);
        
        // === PASO 3: Calcular y notificar disponibilidades ===
        System.out.println("\n📊 Paso 3: Calculando disponibilidades liberadas");
        calculateAndNotifyAvailability(medico, cancelledDate, cancelledTime, duration);
        
        System.out.println("\n" + "=".repeat(80));
        System.out.println("✅ Proceso RFC9 completado");
        System.out.println("=".repeat(80) + "\n");
    }

    /**
     * ✅ Calcular disponibilidades liberadas a partir de la fecha cancelada
     * 
     * Busca espacios disponibles en el horario del médico a partir de la fecha
     * de la cita cancelada (próximos 7 días). Solo notifica si hay disponibilidad >= fecha cancelada.
     * 
     * @param medico Médico cuya disponibilidad se calcula
     * @param startDate Fecha de inicio de búsqueda (fecha de cita cancelada)
     * @param cancelledTime Hora de la cita cancelada
     * @param cancelledDuration Duración de la cita cancelada
     */
    private void calculateAndNotifyAvailability(User medico, LocalDate startDate, 
                                             LocalTime cancelledTime, int cancelledDuration) {
    System.out.println("\n   📊 Analizando disponibilidades desde " + startDate + " (próximos 7 días)");
    System.out.println("   ⏰ Slot liberado: " + cancelledTime + " por " + cancelledDuration + " min");
    System.out.println("   🏥 Médico: " + medico.getName());
    
    AppointmentNotificationManager notificationManager = AppointmentNotificationManager.getInstance();
    System.out.println("   👥 Observadores registrados: " + notificationManager.getObserverCount());
    
    LocalDate today = startDate;
    int daysToCheck = 7;
    int notificationsCount = 0;
    
    for (int i = 0; i < daysToCheck; i++) {
        LocalDate dateToCheck = today.plusDays(i);
        
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(18, 0);
        
        int availableSlots = 0;
        
        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
            if (!appointmentRepository.hasOverlap(medico, dateToCheck, time, 30, null)) {
                availableSlots++;
            }
        }
        
        if (availableSlots > 0) {
            System.out.println("      ✓ " + dateToCheck + ": " + availableSlots + 
                             " espacios disponibles");
            
            // NOTIFICAR
            System.out.println("         → Llamando notifyAvailability()...");
            notificationManager.notifyAvailability(availableSlots, dateToCheck, medico);
            System.out.println("         ✅ Notificación enviada");
            
            notificationsCount++;
            if (notificationsCount >= 3) {
                System.out.println("\n      (Limitado a 3 fechas)");
                break;
            }
        }
    }
    
    if (notificationsCount == 0) {
        System.out.println("      ⚠️  No hay disponibilidades");
    } else {
        System.out.println("\n      📢 " + notificationsCount + " notificación(es) enviada(s)");
    }
}

    /**
     * Verificar si hay solapamiento en un horario específico
     */
    @Override
    public boolean hasOverlap(User medico, LocalDate fecha, LocalTime hora, int duracion) {
        return appointmentRepository.hasOverlap(medico, fecha, hora, duracion, null);
    }

    /**
     * Obtener el notification manager
     */
    public AppointmentNotificationManager getNotificationManager() {
        return notificationManager;
    }
}