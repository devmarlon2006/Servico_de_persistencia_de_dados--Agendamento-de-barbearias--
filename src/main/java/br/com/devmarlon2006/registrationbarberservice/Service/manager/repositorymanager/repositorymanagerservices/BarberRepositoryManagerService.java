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
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.model.barbershop.BarberShop;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.InputValidationService;
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
    public MesagerComplements<String> postOnRepository(Barber barberRecord) {

        barberRecord.deafullRole();

        try{

            if (repositoryGET( barberRecord, TypeOfReturn.NEGATIVE ) == ResponseStatus.WARNING){
               return MesagerComplements.complementsComplete( ResponseStatus.ERROR , OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Barber" ) );
            }

        }catch (NullPointerException e){

           return MesagerComplements.complementsComplete( ResponseStatus.ERROR, OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ));

        }

        if(this.validateAtributesInputs( barberRecord )){

            try{
                barberRepository.save(barberRecord);
            }catch (Exception e){
               return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                       OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" ));
            }

        }else {
            return MesagerComplements.complementsComplete( ResponseStatus.ERROR,
                    OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Barber" )) ;
        }

        return MesagerComplements.complementsComplete( ResponseStatus.SUCCESS,
                OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage( "Usuario registrado com sucesso" )) ;
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

    public boolean validateAtributesInputs(Barber barber){
        List<Boolean> list = new ArrayList<>();

        list.add( InputValidationService.NameIsCorrect(barber.getName()) || InputValidationService.MatchCharacter( barber.getName()));
        list.add( InputValidationService.EmailIsCorrect(barber.getEmail()) || InputValidationService.MatchCharacter( barber.getEmail()) );
        list.add( InputValidationService.MatchCharacter(barber.getId()) || InputValidationService.PasswordIsCorrect( barber.getPassword()) );
        list.add( InputValidationService.PhoneIsCorrect(barber.getPhone()));

        return list.stream().anyMatch( Boolean.TRUE::equals );
    }


    public Barber fyndBarber(String id){
        return barberRepository.findById(id).orElse(null); //Se não for encontrado ira retornar um valor em uma optional
    }

    public Barber findName (String name) {
        return barberRepository.findByName( name );
    }

    public Barber fyndBarberByEmail(String email){
        return barberRepository.findByEmail(email);
    }

    public Boolean existsBarber(BarberShop barberShop){

        if(barberRepository.existsByBarbershop( barberShop )){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }
}
