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

import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import br.com.devmarlon2006.registrationbarberservice.Service.run.ClientService;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("DataSave")
public class ClientController {


    @Value( "${registration-api.configuration.user-register-url}")
    private String API_URL;
    private final TestConectivity testConectivity;
    private final ClientService executeClient;

    public ClientController(TestConectivity testConectivity, ClientService executeClient) {
        this.testConectivity = testConectivity;
        this.executeClient = executeClient;
    }


    @PostMapping("/client")
    public ResponseEntity<?> SavClient(@NonNull @RequestBody DataTransferObject client){
        try{
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( new MessageContainer<>(e.getMessage()));
        }

        if(testConectivity.TestConection( API_URL ) == ResponseMessages.WARNING){

            return ResponseEntity.status( 503 ).body( HttpStatus.SERVICE_UNAVAILABLE );
        }

        MessageContainer<?,?> registrationResponse =  executeClient.ProcessClientRegistration( client );

        if (registrationResponse.getReponse().equals("error")){
            return ResponseEntity.status( 400 ).body( "Error" );
        }

        return ResponseEntity.status( 200 ).body( registrationResponse);
    }
}
