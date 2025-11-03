package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.modelsassemblersmanagers.ClientAssembler;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.model.client.Client;
import br.com.devmarlon2006.registrationbarberservice.model.client.clientdtos.ClientRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.stereotype.Service;


@Service
public class ClientService {

    private final ClientRepositoryManagerService managerClient;
    private final ConnectivityService test;
    private final ClientAssembler clientAssembler;

    public ClientService(ClientRepositoryManagerService managerClient, ConnectivityService test, ClientAssembler clientAssembler) {
        this.managerClient = managerClient;
        this.test = test;
        this.clientAssembler = clientAssembler;
    }

    public MessageContainer<MesagerComplements<String>> ProcessClientRegistration(@NonNull ClientRegistrationDTO clientDTO) {

        Client client = clientAssembler.clientAssembler( clientDTO ).getBody();

        try {

            test.TestConectionData();

        } catch (ConnectionDestroyed e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                   MesagerComplements.complementsOnlyBody(
                           OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ) ));
        }

        try {

            if (!(managerClient.isInstance( client.getClass() , client ))) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Client" ) ) );
            }

            MesagerComplements<String> saveResponse = managerClient.postOnRepository( client );

           if (saveResponse.getStatus().equals( ResponseStatus.ERROR )) {
               return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody(
                       OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ) ) );
           }

        } catch (NullPointerException e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody(
                   OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ));
        }

        return new MessageContainer<>();
    }


}
