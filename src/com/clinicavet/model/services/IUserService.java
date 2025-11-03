package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;

import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;

public interface IUserService {
	
	void createUser(User user);
	Optional<User> getByEmail(String email);
	Optional<User> getById(int id);
	Optional<User> getByName(String name);
	List<User> listUsers();
	List<Rol> getAllRoles();
	void update(User user);
	void deactivateUser(int userId);


}
