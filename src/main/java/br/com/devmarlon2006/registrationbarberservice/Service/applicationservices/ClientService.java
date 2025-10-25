package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.client.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.model.client.clientdtos.ClientRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

    private final ClientRepositoryManagerService managerClient;
    private final ConnectivityService test;

    public ClientService(ClientRepositoryManagerService managerClient, ConnectivityService test) {

        this.managerClient = managerClient;
        this.test = test;
    }


    public MessageContainer<MesagerComplements> ProcessClientRegistration(@NonNull ClientRegistrationDTO clientDTO) {

        Client client = Client.of();
        client.transformToEntity( clientDTO );

        if (client.getId() == null) {
            client.generateId();
        }


        try {

            test.TestConectionData();

        } catch (ConnectionDestroyed e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                   new MesagerComplements( ResponseStatus.ERROR ,
                           OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ) );
        }

        try {

            if (!(managerClient.isInstance( client.getClass() , client ))) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(
                        ResponseStatus.ERROR, OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Client" ) ) );
            }

            MesagerComplements saveResponse = managerClient.postOnRepository( client );

           if (saveResponse.getStatus().equals( ResponseStatus.ERROR )) {
               return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(
                       ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ) ) );
           }

        } catch (NullPointerException e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(
                   ResponseStatus.ERROR, OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ));
        }

        return new MessageContainer<>();
    }


}
