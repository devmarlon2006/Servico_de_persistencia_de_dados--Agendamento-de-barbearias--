package br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos;

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

    private String owerId; //(Apenas o id do dono em formato String)

    private String phone;

    private String address;

    private LocalTime openTime;

    private LocalTime closeTime;

    private String holidayTime;

    private String description;

}
