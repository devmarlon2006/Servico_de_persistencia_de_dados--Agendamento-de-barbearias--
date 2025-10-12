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
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
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

    public BarberRepositoryManagerService(BarberRepository barberRepository) {
        this.barberRepository = barberRepository;
    }

    @Override
    public MesagerComplements<String> postOnRepository(Barber barberRecord) {

        MesagerComplements<String> message = new MesagerComplements<>();

        try{
            if (repositoryGET( barberRecord, TypeOfReturn.NEGATIVE ) == ResponseMessages.WARNING){
               message.setStatus( ResponseMessages.ERROR );
               message.setMessage("Erro interno - ID Erro: ba10x876");
               return message;
            }
        }catch (NullPointerException e){
            message.setMessage("Erro inesperado");
            message.setMessage( "Erro interno ao tentar buscar o registro - ID Erro: ba11x11" );
            return message;
        }

        if(validateAtribiutesFormat( barberRecord )){

            try{
                barberRepository.save(barberRecord);
                message.setMessage("Registro persistido com sucesso");
            }catch (NullPointerException e){
                message.setMessage("Erro interno ao tentar persistir o registro - ID erro: ba11x12");
                message.setStatus(ResponseMessages.ERROR);
               return message;
            }
            message.setStatus(ResponseMessages.SUCCESS);
        }else {
            Validation.ClearObject(barberRecord);
            message.setMessage("Erro interno ao tentar persistir o registro - ID erro: ba12x95");
            message.setStatus(ResponseMessages.ERROR);
            return message ;
        }
        return message ;
    }

    @Override
    public ResponseMessages repositoryGET(Barber barber, TypeOfReturn typeOfReturn){
        if( barberRepository.existsById(barber.getId()) || barberRepository.existsByEmail(barber.getEmail()))
        {
            return switch (typeOfReturn){
                case POSITIVE -> ResponseMessages.SUCCESS;
                case NEGATIVE -> ResponseMessages.WARNING;
            };
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
