/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices;

import java.util.ArrayList;
import java.util.List;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BaseManagerRepository.BaseRepositoryManager;
import org.springframework.stereotype.Service;

import br.com.devmarlon2006.registrationbarberservice.Repository.ClientRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.client.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.InputValidationService;

@Service
public class ClientRepositoryManagerService extends BaseRepositoryManager<Client> {

    private final ClientRepository clientRepository;
    private final ConnectivityService connectivityService;


    public ClientRepositoryManagerService(ClientRepository clientRepository, ConnectivityService connectivityService) {
        this.clientRepository = clientRepository;
        this.connectivityService = connectivityService;
    }

    @Override
    public MesagerComplements<String> postOnRepository(Client ClientRecord) {

        try {
            if (repositoryGET( ClientRecord , TypeOfReturn.NEGATIVE ).equals( ResponseStatus.WARNING )) {

                return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                        OperationStatusCode.ERROR_UNIQUE_CONSTRAINT.getFormattedMessage( "Client" ));
            }
        } catch (NullPointerException e) {
           return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                   OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ));
        }

        if(this.validateAtributesInputs( ClientRecord )) {

            try{
                clientRepository.save( ClientRecord );
            }catch (Exception e){
                return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                        OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ));
            }

        }else {
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                    OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Client" ));
        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS,
                OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage( "Usuario registrado com sucesso" ));
    }

    @Override
    public ResponseStatus repositoryGET(Client ClientRecord, TypeOfReturn typeOfReturn) {
        if (clientRepository.existsById( ClientRecord.getId() ) || clientRepository.existsByEmail( ClientRecord.getEmail()) ||
                clientRepository.existsByUsername( ClientRecord.getUsername() )) {

            return switch (typeOfReturn){
                case POSITIVE -> ResponseStatus.SUCCESS;
                case NEGATIVE -> ResponseStatus.WARNING;
            };
        }
        return connectivityService.retrieveInfo(); //Deafull message
    }



    public boolean validateAtributesInputs(Client client) {
        List<Boolean> list = new ArrayList<>();

        list.add( InputValidationService.NameIsCorrect( client.getName() ) || InputValidationService.MatchCharacter( client.getName() ) );
        list.add( InputValidationService.EmailIsCorrect( client.getEmail() ) || InputValidationService.MatchCharacter( client.getEmail() ) );
        list.add( InputValidationService.PasswordIsCorrect( client.getPassword() ) || InputValidationService.MatchCharacter( client.getPassword()) );
        list.add( InputValidationService.MatchCharacter( client.getId()));

        return list.stream().allMatch( Boolean::booleanValue );
    }

}
