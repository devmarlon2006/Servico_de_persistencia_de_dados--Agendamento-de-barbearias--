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

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BaseManagerRepository.BaseRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BarberRepositoryManagerService extends BaseRepositoryManager<Barber> {

    private final BarberRepository barberRepository;

    public BarberRepositoryManagerService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    @Override
    public MesagerComplements postOnRepository(Barber barberRecord) {
        try{

            if (repositoryGET( barberRecord, TypeOfReturn.NEGATIVE ) == ResponseStatus.WARNING){
               return new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_VALIDATION_FAILED );
            }

        }catch (NullPointerException e){

           return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);

        }

        if(validateAtribiutesFormat( barberRecord )){

            try{
                barberRepository.save(barberRecord);
            }catch (Exception e){
               return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED);
            }

        }else {
            return new MesagerComplements( ResponseStatus.ERROR, OperationStatusCode.ERROR_VALIDATION_FAILED) ;
        }

        return new MesagerComplements( ResponseStatus.SUCCESS, OperationStatusCode.SUCCESS_ENTITY_CREATED) ;
    }

    @Override
    public ResponseStatus repositoryGET(Barber barber, TypeOfReturn typeOfReturn){
        if( barberRepository.existsById(barber.getId()) || barberRepository.existsByEmail(barber.getEmail()))
        {
            return switch (typeOfReturn){
                case POSITIVE -> ResponseStatus.SUCCESS;
                case NEGATIVE -> ResponseStatus.WARNING;
            };
        }
       return ResponseStatus.SUCCESS;
    }

    public boolean validateAtribiutesFormat(Barber barber){
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( barber.getName()  ) || Validation.MatchCharacter( barber.getName()));
        list.add( Validation.EmailIsCorrect( barber.getEmail()) || Validation.MatchCharacter( barber.getEmail()) );
        list.add( Validation.MatchCharacter( barber.getId()) || Validation.PasswordIsCorrect( barber.getPassword()) );
        list.add(Validation.PhoneIsCorrect( barber.getPhone() ));


        return list.stream().anyMatch( Boolean::booleanValue );
    }

    public boolean phoneVerify (String phone) {
        return barberRepository.existsByPhone(phone);
    }

    public Barber fyndBarber(String id){
        return barberRepository.findById(id).orElse(null); //Se não for encontrado ira retornar um valor em uma optional
    }
}
