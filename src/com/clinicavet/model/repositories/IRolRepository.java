package com.clinicavet.model.repositories;

import java.util.List;
import java.util.Optional;
import com.clinicavet.model.entities.Rol;

public interface IRolRepository {
	
	void addRol(Rol rol);
	Optional<Rol> findById(int id);
	Optional<Rol> findByName(String name);
	List<Rol> findAll();

}
