package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.model.barber.barberdto.BarberRegistrationDTO;
import lombok.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class BarberService {

    private final BarberRepositoryManagerService managerBarber;

    private final PasswordEncoder passwordEncoder;

    public BarberService(BarberRepositoryManagerService managerBarber, PasswordEncoder passwordEncoder) {
        this.managerBarber = managerBarber;
        this.passwordEncoder = passwordEncoder;
    }

    @NonNull
    public MessageContainer<MesagerComplements<String>> ProcessBarberRegistration(BarberRegistrationDTO barberDTO){

        Barber barber = Barber.of();
        barber.updateFromRegistration( barberDTO );
        barber.setPassword( passwordEncoder.encode( barber.getPassword() ) );

        try{
            // Validação de tipo - garante que a entidade seja uma instância válida de Barber
            if(!(managerBarber.isInstance( barber.getClass() , barber ))) {

                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                        MesagerComplements.complementsOnlyBody( OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Erro interno tente novamente mais tarde" )));

            }

            barber.generateId(); barber.DeafullScore();
            MesagerComplements<String> saveResponse = managerBarber.postOnRepository(barber);

            if (saveResponse.getStatus().equals( ResponseStatus.ERROR)) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                        MesagerComplements.complementsOnlyBody( saveResponse.getBody() ) );
            }

        }catch (Exception e) {

            return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), MesagerComplements.complementsOnlyBody(
                    OperationStatusCode.ERROR_UNEXPECTED.getFormattedMessage( "Erro interno tente novamente mais tarde" )));

        }

        return new MessageContainer<>(ResponseStatus.SUCCESS.getResponseMessage() ,
                MesagerComplements.complementsOnlyBody( OperationStatusCode.SUCCESS_ENTITY_CREATED.getFormattedMessage( "Usuario registrado com sucesso" ) ) ) ;

    }

}
