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


    public MessageContainer<MesagerComplements, String> ProcessClientRegistration(@NonNull ClientRegistrationDTO clientDTO) {


        Client client = transformToEntity( clientDTO );

        if (client.getId() == null) {
            client.generateId();
        }

        MessageContainer<MesagerComplements, String> clientMessageContainer = new MessageContainer<>();

        try {

            test.TestConectionData();

        } catch (ConnectionDestroyed e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                   new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_DB_CONNECTION ,
                           OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ) );
        }

        try {

            if (!(managerClient.isInstance( client.getClass() , client ))) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(
                        ResponseStatus.ERROR, OperationStatusCode.ERROR_ENTITY_NOT_FOUND,
                        OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Client" ) ) );
            }

            MesagerComplements saveResponse = managerClient.postOnRepository( client );

            if (saveResponse.getStatus().equals( ResponseStatus.SUCCESS ) )
            {
                clientMessageContainer.addResponse( ResponseStatus.SUCCESS.getResponseMessage() );
                clientMessageContainer.addMesage( saveResponse, 0 );
            }
            else if ( saveResponse.getStatus().equals( ResponseStatus.ERROR) )
            {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), saveResponse );
            }

        } catch (NullPointerException e) {
           return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(
                   ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED ,
                   OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ));
        }

        return clientMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }

    public Client transformToEntity(ClientRegistrationDTO clientDTO){
        Client clientRecord = new Client();

        try{

            clientRecord.setAge( clientDTO.getAge() );clientRecord.setName( clientDTO.getName() );clientRecord.setState( clientDTO.getState() );
            clientRecord.setCity(clientDTO.getCity());clientRecord.setHairtype( clientDTO.getHairtype() );clientRecord.setEmail( clientDTO.getEmail() );
            clientRecord.setPassword( clientDTO.getPassword() );

        }catch (NullPointerException e){
            return null;
        }

        return clientRecord;
    }
}
