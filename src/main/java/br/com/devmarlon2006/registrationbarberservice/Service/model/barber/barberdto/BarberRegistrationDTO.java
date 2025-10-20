package br.com.devmarlon2006.registrationbarberservice.Service.model.barber.barberdto;

import lombok.Data;

@Data
public class BarberRegistrationDTO {

    //--------------------------------
    // * Data-transfer-object class
    //--------------------------------

    private String name;

    private String email;

    private String phone;

    private String password;

    private String country;

    private Integer score;
}
