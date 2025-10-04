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

import br.com.devmarlon2006.registrationbarberservice.Service.run.ClientService;
import br.com.devmarlon2006.registrationbarberservice.Service.run.Execute;
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

/**
 * Controlador REST responsável pela gestão de operações relacionadas a clientes no sistema.
 * 
 * <p>Este controller expõe endpoints HTTP para o cadastro e processamento de informações de clientes,
 * atuando como camada de entrada da aplicação e orquestrando validações de conectividade antes de
 * delegar operações de negócio aos serviços apropriados.</p>
 * 
 * <p><b>Mapeamento Base:</b> {@code /DataSave}</p>
 * 
 * <p><b>Responsabilidades:</b></p>
 * <ul>
 *   <li>Receber requisições HTTP de cadastro de clientes</li>
 *   <li>Validar conectividade com banco de dados antes de processar requisições</li>
 *   <li>Validar conectividade com APIs externas de registro</li>
 *   <li>Delegar processamento de negócio ao serviço {@link Execute}</li>
 *   <li>Retornar respostas HTTP adequadas conforme resultado das operações</li>
 * </ul>
 * 
 * @author devmarlon2006
 * @version 1.0
 * @since 2024
 */
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
    public ResponseEntity<?> SavClient(@NonNull @RequestBody Client client){
        try{
            testConectivity.TestConectionData();
        }catch (ConnectionDestroyed e){
            return ResponseEntity.status( 400 ).body( new MessageContainer<>(e.getMessage()));
        }

        if(testConectivity.TestConection( API_URL ) == ResponseMessages.WARNING){

            return ResponseEntity.status( 503 ).body( HttpStatus.SERVICE_UNAVAILABLE );
        }

        MessageContainer<?> a =  executeClient.ProcessClientRegistration( client );

        if (a.getReponse().equals("error")){
            return ResponseEntity.status( 400 ).body( "Error" );
        }

        return ResponseEntity.status( 200 ).body( "Error");
    }
}
