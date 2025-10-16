/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.StatusOperation;
import org.springframework.stereotype.Service;

import br.com.devmarlon2006.registrationbarberservice.Repository.ClientRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.supersmanagers.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;

@Service
public class ClientRepositoryManagerService implements SuperRepositoryManager<Client, String> {

    private final ClientRepository clientRepository;
    private final TestConectivity testConectivity;


    public ClientRepositoryManagerService(ClientRepository clientRepository, TestConectivity testConectivity) {
        this.clientRepository = clientRepository;
        this.testConectivity = testConectivity;
    }


    @Override
    public MesagerComplements postOnRepository(Client ClientRecord) {

        try {
            if (repositoryGET( ClientRecord , TypeOfReturn.NEGATIVE ).equals( ResponseMessages.WARNING )) {
                return new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_UNIQUE_CONSTRAINT);
            }
        } catch (NullPointerException e) {
           return new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_UNEXPECTED);
        }

        if(validateAtribiutesFormat( ClientRecord )) {

            try{
                clientRepository.save( ClientRecord );
            }catch (Exception e){
                return new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_UNEXPECTED);
            }

        }else {
            return  new MesagerComplements(ResponseMessages.ERROR, StatusOperation.ERROR_VALIDATION_FAILED);
        }

        return new MesagerComplements(ResponseMessages.SUCCESS, StatusOperation.SUCCESS_ENTITY_CREATED);
    }

    @Override
    public ResponseMessages repositoryGET(Client ClientRecord, TypeOfReturn typeOfReturn) {
        if (clientRepository.existsById( ClientRecord.getId() ) || clientRepository.existsByEmail( ClientRecord.getEmail()) ||
                clientRepository.existsByUsername( ClientRecord.getUsername() )) {

            return switch (typeOfReturn){
                case POSITIVE -> ResponseMessages.SUCCESS;
                case NEGATIVE -> ResponseMessages.WARNING;
            };
        }
        return testConectivity.retrieveInfo(); //Deafull message
    }



    public boolean validateAtribiutesFormat(Client client) {
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( client.getName() ) || Validation.MatchCharacter( client.getName() ) );
        list.add( Validation.EmailIsCorrect( client.getEmail() ) || Validation.MatchCharacter( client.getEmail() ) );
        list.add( Validation.PasswordIsCorrect( client.getPassword() ) || Validation.MatchCharacter( client.getPassword()) );
        list.add( Validation.MatchCharacter( client.getId()));

        return list.stream().allMatch( Boolean::booleanValue );
    }
    
    /**
     * Verifica se o objeto informado é uma instância de {@link Client}.
     *
     * @param obj objeto a ser inspecionado
     * @return true se for instância de Client; false caso contrário
     */

    @Override
    public boolean isInstance(Class<?> obj){
        return obj.isAssignableFrom( Client.class);
    }
}
