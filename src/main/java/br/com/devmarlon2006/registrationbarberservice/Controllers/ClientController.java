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

import br.com.devmarlon2006.registrationbarberservice.model.client.clientdtos.ClientRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.applicationservices.ClientService;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("${endpoints.persistence}")
public class ClientController {

    private final ConnectivityService connectivityService;
    private final ClientService executeClient;

    public ClientController(ConnectivityService connectivityService, ClientService executeClient) {
        this.connectivityService = connectivityService;
        this.executeClient = executeClient;
    }


    @PostMapping("${api.entity's.client}")
    public ResponseEntity<?> SavClient(@NonNull @RequestBody ClientRegistrationDTO client){

        try {

            try{
                connectivityService.TestConectionData();
            }catch (ConnectionDestroyed e){
                return ResponseEntity.status( 400 ).body( new MessageContainer<>( "Error" , e.getMessage()));
            }

            MessageContainer<?> registrationResponse =  executeClient.ProcessClientRegistration( client );

            if (registrationResponse.getReponse().equals("error")) {
                return ResponseEntity.status( 400 ).body( "Error" );
            }

            return ResponseEntity.status( 200 ).body( registrationResponse);

        }catch (Exception e){

            return ResponseEntity.status( 400 ).body( "Erro inesperado tente novamente mais tarde" );
        }

    }
}
