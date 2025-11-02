package br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto;

import lombok.Data;

public record BarberRegistrationDTO (
        String name,
        String email,
        String phone,
        String password,
        String country
) {

    //--------------------------------
    // * Data-transfer-object class
    //--------------------------------

}
