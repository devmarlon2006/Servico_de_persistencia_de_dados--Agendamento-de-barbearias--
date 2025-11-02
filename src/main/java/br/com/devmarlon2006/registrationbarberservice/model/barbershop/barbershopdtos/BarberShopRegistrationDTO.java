package br.com.devmarlon2006.registrationbarberservice.model.barbershop.barbershopdtos;

import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarberShopRegistrationDTO {

    //--------------------------------
    // * Data-transfer-object class
    //--------------------------------

    private String name;

    private Barber owerId;

    private String phone;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    private String holidayTime;

    private String description;

}
