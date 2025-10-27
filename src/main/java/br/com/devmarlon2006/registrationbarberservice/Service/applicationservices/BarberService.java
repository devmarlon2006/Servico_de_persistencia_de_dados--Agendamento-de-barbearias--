package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Repository.BarberShopRepository;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.barberdto.BarberRegistrationDTO;
import lombok.NonNull;
import org.springframework.stereotype.Service;



@Service
public class BarberService {

    private final BarberRepositoryManagerService managerBarber;

    public BarberService(BarberRepositoryManagerService managerBarber) {
        this.managerBarber = managerBarber;
    }

    /**
     * Processa o registro de um barbeiro no sistema.
     * 
     * Fluxo de operação:
     * 1. Verifica conectividade com o banco de dados
     * 2. Valida se a instância é do tipo Barber
     * 3. Persiste o barbeiro no repositório
     * 4. Registra logs de operação (sucesso/erro)
     * 5. Constrói e retorna container com mensagens de resposta
     * 
     * @param barberDTO Entidade Barber a ser registrada
     * @return MessageContainer contendo status da operação e mensagens de log
     */
    @NonNull
    public MessageContainer<MesagerComplements<String>> ProcessBarberRegistration(BarberRegistrationDTO barberDTO){

        Barber barber = Barber.of();
        barber.tranformEntity( barberDTO );

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
