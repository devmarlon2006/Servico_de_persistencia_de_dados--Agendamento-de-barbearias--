package br.com.devmarlon2006.registrationbarberservice.model.barber;

public enum BarberRoles {

    ADMIN("ADMIN"),
    BARBEIRO("BARBEIRO"),
    DONO_BARBEARIA("DONO_BARBEARIA");

    private final String role;
    BarberRoles(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
