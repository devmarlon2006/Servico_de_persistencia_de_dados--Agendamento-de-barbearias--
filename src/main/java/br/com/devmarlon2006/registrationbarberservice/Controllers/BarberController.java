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
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.run.BarberService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("DataSave")
public class BarberController {

    private final TestConectivity testConectivity;
    private final BarberService barberService;

    public BarberController(TestConectivity testConectivity, BarberService barberService) {
        this.testConectivity = testConectivity;
        this.barberService = barberService;
    }

    @PostMapping("/Barber")
    public ResponseEntity<?> SavBarber(@RequestBody Barber barber){

        try{
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( "Error" );
        }


        MessageContainer<?> a = barberService.ProcessBarberRegistration( barber );

        if (a.getReponse().equals("error")){

            return ResponseEntity.status( 400 ).body( "Error" );

        }

        return ResponseEntity.status( 200 ).body( "Ok" );
    }
}
