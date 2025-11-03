package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;

import com.clinicavet.model.entities.Rol;

public interface IRolService {
	
	Optional<Rol> getById(int id);
	Optional<Rol> getByName(String nombre);
    void createRol(Rol rol);
    List<Rol> listRoles();
    

}
