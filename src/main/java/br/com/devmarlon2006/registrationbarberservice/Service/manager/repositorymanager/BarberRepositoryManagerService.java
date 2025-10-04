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

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.SuperRepositoryManager;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class BarberRepositoryManagerService implements SuperRepositoryManager<Barber,String> {

    private final BarberRepository barberRepository;
    private final TestConectivity testConectivity;

    public BarberRepositoryManagerService(BarberRepository barberRepository, TestConectivity testConectivity) {
        this.barberRepository = barberRepository;
        this.testConectivity = testConectivity;
    }

    @Override
    public ResponseMessages postOnRepository(Barber barberRecord) {

        try{
            if (repositoryGET( barberRecord, TypeOfReturn.NEGATIVE ).equals( ResponseMessages.ERROR )){
               return ResponseMessages.ERROR;
            }
        }catch (NullPointerException e){
            return ResponseMessages.ERROR;
        }

        if ( phoneVerify(barberRecord.getPhone())){
            return ResponseMessages.ERROR;
        }

        if(validateAtribiutesFormat( barberRecord )){
            barberRecord.DeafullScore(); // Se o score for nulo ou maior que 0 ele e setado como 0.
            barberRepository.save(barberRecord);
        }else {
            Validation.ClearObject(barberRecord);
            return ResponseMessages.ERROR;
        }
        return ResponseMessages.SUCCESS ;
    }

    @Override
    public ResponseMessages repositoryGET(Barber barber, TypeOfReturn typeOfReturn){
        if( barberRepository.existsById(barber.getId()) || barberRepository.existsByEmail(barber.getEmail()))
        {
            return testConectivity.retrieveError();
        }
       return ResponseMessages.SUCCESS;
    }

    public boolean validateAtribiutesFormat(Barber barber){
        List<Boolean> list = new ArrayList<>();

        list.add( Validation.NameIsCorrect( barber.getName()) );
        list.add( Validation.EmailIsCorrect( barber.getEmail()) );
        list.add( barber.getPassword() != null );
        list.add( Validation.MatchCharacter( barber.getId()) );
        list.add(Validation.PhoneIsCorrect( barber.getPhone() ));

        return list.stream().allMatch( Boolean::booleanValue );
    }

    @Override
    public boolean isInstance(Class<?> obj){
        return obj.isAssignableFrom(Barber.class);
    }

    public boolean phoneVerify (String phone) {
        return barberRepository.existsByPhone(phone);
    }

    public Barber fyndBarber(String id){
        return barberRepository.findById(id).orElse(null); //Se não for encontrado ira retornar um valor em uma optional
    }
}
