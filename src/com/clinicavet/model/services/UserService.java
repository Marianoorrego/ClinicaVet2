package com.clinicavet.model.services;

import java.util.List;
import java.util.Optional;

import com.clinicavet.model.entities.Rol;
import com.clinicavet.model.entities.User;
import com.clinicavet.model.repositories.IUserRepository;
import com.clinicavet.model.repositories.RolRepository;

public class UserService implements IUserService{
	
	private final IUserRepository userRepository;
	private final RolService rolService;
	
	public UserService(IUserRepository userRepository, RolService rolService) {
        this.userRepository = userRepository;
		this.rolService = rolService;
    }
	
	@Override
	public Optional<User> getByEmail(String email) {
	    return userRepository.findByEmail(email);
	}

	@Override
	public void createUser(User user) {
		userRepository.addUser(user);	
	}

	@Override
	public Optional<User> getById(int id) {
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> getByName(String name) {
		return userRepository.findByName(name);
	}

	@Override
	public List<User> listUsers() {
		return userRepository.findAll();
	}

	@Override
	public List<Rol> getAllRoles() {
		return rolService.listRoles();
	}
	
	@Override
	public void update(User user) {
		userRepository.updateUser(user);	
	}
	
	@Override
    public void deactivateUser(int userId) {
		Optional<User> userOpt = userRepository.findById(userId);
	    if (userOpt.isPresent()) {
	        User user = userOpt.get();
	        user.setActivo(false);
	        userRepository.updateUser(user);
	    } else {
	        throw new IllegalArgumentException("Usuario no encontrado con ID: " + userId);
	    }
	}

}
