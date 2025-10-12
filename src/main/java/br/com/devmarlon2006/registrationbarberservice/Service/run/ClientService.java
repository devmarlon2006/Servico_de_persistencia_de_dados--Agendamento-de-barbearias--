package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.model.DataTransferObject;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import lombok.NonNull;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepositoryManagerService managerClient;
    private final TestConectivity test;

    public ClientService(ClientRepositoryManagerService managerClient, TestConectivity test) {

        this.managerClient = managerClient;
        this.test = test;
    }


    public MessageContainer<MesagerComplements<?>, String> ProcessClientRegistration(@NonNull DataTransferObject clientDTO) {
        Client client = (Client) clientDTO.requiredType( Client.class) ;
        MessageContainer<MesagerComplements<?>, String> clientMessageContainer = new MessageContainer<>();

        try {
            test.TestConectionData();

            clientMessageContainer.addMesage( clientMessageContainer.newAresponseComplements(
                    ResponseMessages.SUCCESS, "Banco disponivel" ), 0 );

        } catch (ConnectionDestroyed e) {
            clientMessageContainer.addResponse( "Error" );

            clientMessageContainer.addMesage(
                    clientMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Erro fatal inesperado - ID: Erro: cl20" ), 0 );

            return clientMessageContainer;
        }

        try {

            if (!(managerClient.isInstance( client.getClass() ))) {
                Validation.ClearObject( client );

                clientMessageContainer.addResponse( "Error" );
                clientMessageContainer.addMesage(
                        clientMessageContainer.newAresponseComplements(
                                ResponseMessages.WARNING, "Invalid Object" ), 0 );

                return clientMessageContainer;
            }

            MesagerComplements<?> saveResponse = managerClient.postOnRepository( client );

            if (saveResponse.getStatus().equals(ResponseMessages.SUCCESS)) {
                clientMessageContainer.addMesage(saveResponse, 1);
            }else if (saveResponse.getStatus().equals(ResponseMessages.ERROR)) {
                clientMessageContainer.addMesage(saveResponse, 1);
            }

        } catch (NullPointerException e) {
            clientMessageContainer.addResponse( "Error" );

            clientMessageContainer.addMesage(
                    clientMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Repository Error" ), 0 );

            return clientMessageContainer;
        }

        List<ResponseMessages> list = new ArrayList<>();
        try{
            for (int INDEX = 0; INDEX <= clientMessageContainer.getResponseComplements().size(); INDEX++){
                list.add(clientMessageContainer.getResponseComplements().get( INDEX ).getStatus());
            }
        }catch (IndexOutOfBoundsException e){
            list.add(ResponseMessages.ERROR);
        }

        if (list.stream().allMatch( ResponseMessages.SUCCESS::equals )){
            clientMessageContainer.setReponse("Success");
        }else {
            clientMessageContainer.setReponse("Error");
        }


        return clientMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }
}
