package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BarberService {

    private final BarberRepositoryManagerService managerBarber;
    private final Execute execute;
    private final TestConectivity test;

    public BarberService(BarberRepositoryManagerService managerBarber,
                         Execute execute, TestConectivity test) {
        this.managerBarber = managerBarber;
        this.execute = execute;
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
     * @param barber Entidade Barber a ser registrada
     * @return MessageContainer contendo status da operação e mensagens de log
     */
    @NonNull
    public MessageContainer<MesagerComplements<?>, String> ProcessBarberRegistration(Barber barber){
        MessageContainer<MesagerComplements<?> ,String> barberMessageContainer = new MessageContainer<>();
        List<ResponseMessages> list = new ArrayList<>();
        
        // Teste de conectividade - operação crítica que bloqueia o fluxo em caso de falha
        try{

            ResponseMessages statusOperation  = test.TestConectionData();

            if (statusOperation.equals(ResponseMessages.SUCCESS)){

                barberMessageContainer.addResponse("Success");
                barberMessageContainer.addMesage( barberMessageContainer.newAresponseComplements(
                        ResponseMessages.SUCCESS, "Success" ), 0 );

            }

        }catch (ConnectionDestroyed e){
            barberMessageContainer.addResponse("Error");

            barberMessageContainer.addMesage(
                    barberMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Erro fatal inesperado - ID Erro: Br20"), 0);

            return barberMessageContainer;

        }

        try{
            // Validação de tipo - garante que a entidade seja uma instância válida de Barber
            if(!(managerBarber.isInstance( barber.getClass() ))){
                Validation.ClearObject(barber);

                barberMessageContainer.addMesage(
                        barberMessageContainer.newAresponseComplements(
                                ResponseMessages.WARNING, "Objeto invalido"), 0);

                return barberMessageContainer;
            }

            barber.DeafullScore();
            MesagerComplements<?> operationResult = managerBarber.postOnRepository(barber);
            barberMessageContainer.addMesage(operationResult, 1);

        }catch (NullPointerException e) {

            barberMessageContainer.addMesage(
                    barberMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Fatal Error"), 0);

        }

        for (int INDEX = 0; INDEX <= barberMessageContainer.getResponseComplements().size(); INDEX++){
            list.add(barberMessageContainer.getResponseComplements().get( INDEX ).getStatus());
        }

        if (list.stream().allMatch( ResponseMessages.SUCCESS::equals )){
            barberMessageContainer.setReponse("Success");
        }else {
            barberMessageContainer.setReponse("Error");
        }

        return barberMessageContainer;
    }

}
