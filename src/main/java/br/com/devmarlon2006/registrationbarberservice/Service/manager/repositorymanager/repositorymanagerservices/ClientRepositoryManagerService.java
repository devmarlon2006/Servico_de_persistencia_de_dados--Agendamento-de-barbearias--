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
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;

@Service
public class ClientRepositoryManagerService extends BaseRepositoryManager<Client> {

    private final ClientRepository clientRepository;
    private final ConnectivityService connectivityService;


    public ClientRepositoryManagerService(ClientRepository clientRepository, ConnectivityService connectivityService) {
        this.clientRepository = clientRepository;
        this.connectivityService = connectivityService;
    }


    @Override
    public MesagerComplements postOnRepository(Client ClientRecord) {

        try {
            if (repositoryGET( ClientRecord , TypeOfReturn.NEGATIVE ).equals( ResponseStatus.WARNING )) {

                return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNIQUE_CONSTRAINT);
            }
        } catch (NullPointerException e) {
           return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
        }

        if(validateAtribiutesFormat( ClientRecord )) {

            try{
                clientRepository.save( ClientRecord );
            }catch (Exception e){
                return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
            }

        }else {
            return  new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_VALIDATION_FAILED);
        }

        return new MesagerComplements( ResponseStatus.SUCCESS, OperationStatusCode.SUCCESS_ENTITY_CREATED);
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



    public boolean validateAtribiutesFormat(Client client) {
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( client.getName() ) || Validation.MatchCharacter( client.getName() ) );
        list.add( Validation.EmailIsCorrect( client.getEmail() ) || Validation.MatchCharacter( client.getEmail() ) );
        list.add( Validation.PasswordIsCorrect( client.getPassword() ) || Validation.MatchCharacter( client.getPassword()) );
        list.add( Validation.MatchCharacter( client.getId()));

        return list.stream().allMatch( Boolean::booleanValue );
    }

}
