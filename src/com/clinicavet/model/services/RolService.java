package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;
import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.repositories.IRolRepository;
import com.clinicavet.model.repositories.RolRepository;

public class RolService implements IRolService {
	
	private IRolRepository rolRepository = null;

 
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

	@Override
	public Optional<Rol> getById(int id) {
		return rolRepository.findById(id);
	}

	@Override
	public Optional<Rol> getByName(String nombre) {
		return rolRepository.findByName(nombre);
	}

	@Override
	public void createRol(Rol rol) {
		if (rol == null || rol.getName() == null || rol.getName().isEmpty()) {
            throw new IllegalArgumentException("El rol no puede ser nulo ni tener nombre vacío");
        }

        if ("ADMIN".equalsIgnoreCase(rol.getName()) 
                && rolRepository.findByName("ADMIN").isPresent()) {
            throw new IllegalArgumentException("Ya existe un rol ADMIN, no se puede crear otro.");
        }

        rolRepository.addRol(rol);
        
	}

	@Override
	public List<Rol> listRoles() {
		return rolRepository.findAll();
	}	
	
}
