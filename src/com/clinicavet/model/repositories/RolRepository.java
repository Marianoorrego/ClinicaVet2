package com.clinicavet.model.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.clinicavet.model.entities.Rol;

public class RolRepository implements IRolRepository{
	
	List<Rol> roles = new ArrayList<>();

	@Override
	public void addRol(Rol rol) {
		roles.add(rol);
	}

	@Override
	public Optional<Rol> findById(int id) {
		return roles.stream().filter(rol -> rol.getId() == id).findFirst();
	}

	@Override
	public Optional<Rol> findByName(String name) {
		return roles.stream().filter(rol -> rol.getName().equals(name)).findFirst();
	}

	@Override
	public List<Rol> findAll() {
		return roles;
	}


}
