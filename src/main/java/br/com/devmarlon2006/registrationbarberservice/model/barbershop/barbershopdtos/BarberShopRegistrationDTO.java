package br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;

import java.time.LocalTime;


public record BarberShopRegistrationDTO (
        String name,

        Barber owerId,

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
