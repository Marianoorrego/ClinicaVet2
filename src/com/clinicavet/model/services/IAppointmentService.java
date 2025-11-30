package com.clinicavet.model.services;

import com.clinicavet.model.entities.Appointment;
import com.clinicavet.model.entities.Pet;
import com.clinicavet.model.entities.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAppointmentService {
    
    boolean createAppointment(Appointment appointment);
    
    List<Appointment> listAppointments();
    
    Optional<Appointment> getAppointmentById(UUID id);
    
    List<Appointment> getAppointmentsByMedico(User medico);
    
    List<Appointment> getAppointmentsByMascota(Pet mascota);
    
    List<Appointment> getAppointmentsByFecha(LocalDate fecha);
    
    boolean updateAppointment(Appointment appointment);
    
    void deleteAppointment(UUID id);
    
    /**
     * ✅ RFC9: Cancelar cita y notificar automáticamente a auxiliares
     * @param appointmentId El ID de la cita a cancelar
     */
    void cancelAppointmentAndNotify(UUID appointmentId);
    
    /**
     * ✅ RFC9: Verificar si hay solapamiento en un horario específico
     * @param medico El médico
     * @param fecha La fecha a verificar
     * @param hora La hora de inicio
     * @param duracion La duración en minutos
     * @return true si hay solapamiento, false si está disponible
     */
    boolean hasOverlap(User medico, LocalDate fecha, LocalTime hora, int duracion);
}