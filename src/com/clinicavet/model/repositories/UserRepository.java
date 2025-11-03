package com.clinicavet.model.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.clinicavet.model.entities.User;

public class UserRepository implements IUserRepository{
	
	private final List<User> users = new ArrayList<>();
	 private int nextId = 1;

	@Override
	public void addUser(User user) {
		user.setId(nextId++);
		users.add(user);
	}

	@Override
	public Optional<User> findById(int id) {
		return users.stream().filter(user -> user.getId() == id).findFirst();
	}

	@Override
	public Optional<User> findByName(String name) {
		return users.stream().filter(user -> user.getName().equals(name)).findFirst();
	}
	
	@Override
	public Optional<User> findByEmail(String email) {
	    return users.stream()
	                .filter(user -> user.geteMail().equals(email))
	                .findFirst();
	}

	@Override
	public List<User> findAll() {
		return users;
	}
	
	@Override
	public void updateUser(User user) {
		for (int i = 0; i < users.size(); i++) {
	        if (users.get(i).getId() == user.getId()) {
	            users.set(i, user); 
	            return;
	        }
	    }
	    throw new IllegalArgumentException("Usuario no encontrado con id: " + user.getId());
	}


}
