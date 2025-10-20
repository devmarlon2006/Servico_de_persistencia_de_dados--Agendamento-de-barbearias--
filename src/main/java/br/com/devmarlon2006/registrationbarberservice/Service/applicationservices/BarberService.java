package br.com.devmarlon2006.registrationbarberservice.Service.applicationservices;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.OperationStatusCode;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.ConnectivityService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.repositorymanagerservices.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.barber.barberdto.BarberRegistrationDTO;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarberService {

    private final BarberRepositoryManagerService managerBarber;
    private final ConnectivityService test;

    public BarberService(BarberRepositoryManagerService managerBarber, ConnectivityService test) {
        this.managerBarber = managerBarber;
        this.test = test;
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
    public MessageContainer<MesagerComplements, String> ProcessBarberRegistration(BarberRegistrationDTO barberDTO){
        MessageContainer<MesagerComplements ,String> barberMessageContainer = new MessageContainer<>();

        Barber barber = new Barber();
        barber.tranformEntity( barberDTO );

        try{
            // Validação de tipo - garante que a entidade seja uma instância válida de Barber
            if(!(managerBarber.isInstance( barber.getClass() , barber ))){
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(),
                        new MesagerComplements( ResponseStatus.ERROR , OperationStatusCode.ERROR_UNEXPECTED,
                                OperationStatusCode.ERROR_VALIDATION_FAILED.getFormattedMessage( "Barber" )));
            }

            barber.DeafullScore();   barber.generateId();
            MesagerComplements operationResult = managerBarber.postOnRepository(barber);


            if (operationResult.getStatus().equals(ResponseStatus.SUCCESS)) {
                barberMessageContainer.addResponse(ResponseStatus.SUCCESS.getResponseMessage());
                barberMessageContainer.addMesage(operationResult, 0);
                return  barberMessageContainer;
            }else if (operationResult.getStatus().equals(ResponseStatus.ERROR)) {
                return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), operationResult );
            }

        }catch (NullPointerException e) {

           return new MessageContainer<>();

        }

        return new MessageContainer<>( ResponseStatus.ERROR.getResponseMessage(), new MesagerComplements(ResponseStatus.ERROR ,
                OperationStatusCode.ERROR_UNEXPECTED, OperationStatusCode.ERROR_BUSINESS_RULE.getFormattedMessage( "Barber" )));
    }

}
