package com.clinicavet.controllers;

import java.util.Optional;

import com.clinicavet.model.entities.User;
import com.clinicavet.model.services.IUserService;
import com.clinicavet.model.services.RolService;
import com.clinicavet.model.services.UserService;
import com.clinicavet.views.AdminWindow;

public class LoginControler {
	
	private final IUserService userService;
	private final RolService rolService;
    private User usuarioAutenticado;

    public LoginControler(IUserService userService, RolService rolService) {
    	this.rolService = rolService;
        this.userService = userService;
    }

    public boolean login(String email, String password) {
    	Optional<User> usuarioOpt = userService.getByEmail(email);

        if (usuarioOpt.isPresent()) {
            User usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password) && usuario.isActivo()) {
                usuarioAutenticado = usuario;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        usuarioAutenticado = null;
    }

    public User getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    public void redirectToMainMenu() {
        if (usuarioAutenticado == null) 
        	return;

        switch (usuarioAutenticado.getRol().getName().toUpperCase()) {
            case "ADMIN":
            	AdminWindow adminWindow = new AdminWindow(this, (UserService) userService, rolService);
                adminWindow.setVisible(true);
                break;
            case "MEDICO":
                System.out.println("ventana médico...");
                break;
            case "AUXILIAR":
                System.out.println("ventana auxiliar...");
                break;
            default:
                System.out.println("Rol no existente.");
        }
    }
    

}
