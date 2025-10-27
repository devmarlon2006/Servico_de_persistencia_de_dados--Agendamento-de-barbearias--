package br.com.devmarlon2006.registrationbarberservice.Controllers;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.applicationservices.BarberAppointmentService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barbershop.barbershopdtos.BarberShopWithOwnerRegistrationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${endpoints.persistence}")
public class BarberAndShopController {

    private final BarberAppointmentService barberAppointmentService;


    public BarberAndShopController(BarberAppointmentService plus) {
        this.barberAppointmentService = plus;
    }

    @PostMapping("${api.entity's.barberAndShop}")
    public ResponseEntity<?> controllerShop(BarberShopWithOwnerRegistrationDTO data) {
        MessageContainer<MesagerComplements<String>> registrationResponse;

        try{
            registrationResponse = barberAppointmentService.processAppointment(data);
        }catch (Exception e){
            return ResponseEntity.status( 400 ).body( e.getMessage() );
        }

        return ResponseEntity.status( HttpStatus.CREATED ).body( registrationResponse );
    }
}