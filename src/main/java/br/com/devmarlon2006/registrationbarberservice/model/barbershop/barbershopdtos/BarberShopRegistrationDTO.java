package br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos;

import br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto.BarberRegistrationDTO;

import java.time.LocalTime;


public record BarberShopRegistrationDTO (
        String name,

        BarberRegistrationDTO ownerId, //Usado apenas caso o barbeiro seja criado junto com a barbearia

        String phone,

        String address,

        LocalTime openTime,

        LocalTime closeTime,

        String holidayTime,

        String description) {

    //--------------------------------
    // * Data-transfer-object class
    //--------------------------------

}
