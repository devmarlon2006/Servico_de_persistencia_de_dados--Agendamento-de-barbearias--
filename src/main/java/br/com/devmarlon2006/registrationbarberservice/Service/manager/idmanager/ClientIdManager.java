package br.com.devmarlon2006.registrationbarberservice.Service.manager.idmanager;

import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.RestConections;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.supersmanagers.SuperIdManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ClientIdManager implements SuperIdManager {

    @Value( "${registration-api.configuration.id_creation}" )
    private String idGeneration;
    private final RestConections<Client> restConections;

    public ClientIdManager(RestConections<Client> restConections) {
        this.restConections = restConections;
    }

    public String createId(Client ClientRecord) {

        ResponseEntity<?> response = restConections.GetConection( idGeneration + "PATH_3", ClientRecord );

       if (response.getStatusCode() == HttpStatus.CREATED){
           ClientRecord.setId( (String) response.getBody() ); //Safe cast
       }

       return ClientRecord.getId();
    }
}
