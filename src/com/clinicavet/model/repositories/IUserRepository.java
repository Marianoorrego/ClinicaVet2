package com.clinicavet.model.repositories;

import java.util.List;
import java.util.Optional;
import com.clinicavet.model.entities.User;

public interface IUserRepository {
	
	void addUser(User user);
	Optional<User> findById(int id);
	Optional<User> findByName(String name);
	Optional<User> findByEmail(String email);
	List<User> findAll();
	void updateUser(User user);

	
}
