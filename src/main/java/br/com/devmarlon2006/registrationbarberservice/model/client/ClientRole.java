package br.com.devmarlon2006.registrationbarberservice.model.client;

import java.awt.event.ComponentListener;

public enum ClientRole {

    ADMIN("ADMIN"),
    CLIENTE("CLIENTE");

    private final String role;
    ClientRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}
