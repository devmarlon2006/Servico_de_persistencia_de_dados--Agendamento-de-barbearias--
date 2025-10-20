/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Controllers;


import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.barberdto.BarberRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.applicationservices.BarberService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("DataSave")
public class BarberController {

    private final ConnectivityService connectivityService;
    private final BarberService barberService;

    public BarberController(ConnectivityService connectivityService, BarberService barberService) {
        this.connectivityService = connectivityService;
        this.barberService = barberService;
    }

    @PostMapping("/Barber")
    public ResponseEntity<?> SavBarber(@RequestBody BarberRegistrationDTO barberDTO){
        MessageContainer<?,?> registrationResponse;

        try{
            connectivityService.TestConectionData();
        }catch (ConnectionDestroyed e) {
            return ResponseEntity.status( 400 ).body( "Error" );
        }

        try{

            registrationResponse = barberService.ProcessBarberRegistration( barberDTO );

            if (registrationResponse.getReponse().equals("error")){

                return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Erro Interno ao registrar o barbero" );

            }

        }catch (NullPointerException e){
            return ResponseEntity.status( HttpStatus.INTERNAL_SERVER_ERROR ).body( "Error" );
        }

        return ResponseEntity.status( HttpStatus.OK ).body( registrationResponse );
    }
}
