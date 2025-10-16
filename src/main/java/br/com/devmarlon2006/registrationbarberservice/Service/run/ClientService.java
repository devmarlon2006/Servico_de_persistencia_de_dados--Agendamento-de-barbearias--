package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.StatusOperation;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.idmanager.ClientIdManager;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepositoryManagerService managerClient;
    private final ClientIdManager clientIdManager;
    private final TestConectivity test;

    public ClientService(ClientRepositoryManagerService managerClient, ClientIdManager clientIdManager, TestConectivity test) {

        this.managerClient = managerClient;
        this.clientIdManager = clientIdManager;
        this.test = test;
    }


    public MessageContainer<MesagerComplements, String> ProcessClientRegistration(@NonNull DataTransferObject clientDTO) {
        Client client = (Client) clientDTO.requiredType( Client.class) ;
        MessageContainer<MesagerComplements, String> clientMessageContainer = new MessageContainer<>();

        try {

            test.TestConectionData();

        } catch (ConnectionDestroyed e) {
           return new MessageContainer<>(ResponseMessages.ERROR.getResponseMessage(),
                   new MesagerComplements( ResponseMessages.ERROR , StatusOperation.ERROR_DB_CONNECTION ,
                           StatusOperation.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ) );
        }

        try {

            if (!(managerClient.isInstance( client.getClass() ))) {
                return new MessageContainer<>( ResponseMessages.ERROR.getResponseMessage(), new MesagerComplements(
                        ResponseMessages.ERROR, StatusOperation.ERROR_ENTITY_NOT_FOUND,
                        StatusOperation.ERROR_VALIDATION_FAILED.getFormattedMessage( "Client" ) ) );
            }

            MesagerComplements saveResponse = managerClient.postOnRepository( client );

            if (saveResponse.getStatus().equals( ResponseMessages.SUCCESS ) )
            {
                clientMessageContainer.addResponse( ResponseMessages.SUCCESS.getResponseMessage() );
            }
            else if ( saveResponse.getStatus().equals( ResponseMessages.ERROR) )
            {
                return new MessageContainer<>( ResponseMessages.ERROR.getResponseMessage(), saveResponse );
            }

        } catch (NullPointerException e) {
           return new MessageContainer<>( ResponseMessages.ERROR.getResponseMessage(), new MesagerComplements(
                   ResponseMessages.ERROR, StatusOperation.ERROR_UNEXPECTED ,
                   StatusOperation.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() ) ));
        }

        List<ResponseMessages> list = new ArrayList<>();

        try{
            for (int INDEX = 0; INDEX <= clientMessageContainer.getResponseComplements().size(); INDEX++){
                list.add(clientMessageContainer.getResponseComplements().get( INDEX ).getStatus());
            }
        }catch (IndexOutOfBoundsException e){
            return new MessageContainer<>(ResponseMessages.ERROR.getResponseMessage(),
                    new MesagerComplements( ResponseMessages.ERROR , StatusOperation.ERROR_UNEXPECTED ,
                            StatusOperation.ERROR_BUSINESS_RULE.getFormattedMessage( e.getMessage() )));
        }

        if (list.stream().allMatch( ResponseMessages.SUCCESS::equals )){
            clientMessageContainer.setReponse("Success");
        }else {
            clientMessageContainer.setReponse("Error");
        }

        return clientMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }

    public void processIdCreation(Client client){

        clientIdManager.createId(client);

        client.setId( "" );
    }
}
