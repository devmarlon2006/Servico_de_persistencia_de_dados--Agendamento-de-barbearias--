package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class BarberService {

    private final BarberRepositoryManagerService managerBarber;
    private final Execute execute;
    private final TestConectivity test;

    public BarberService(BarberRepositoryManagerService managerBarber, Execute execute, TestConectivity test) {
        this.managerBarber = managerBarber;
        this.execute = execute;
        this.test = test;
    }


    public MessageContainer<MesagerComplements<?>, String> ProcessBarberRegistration(Barber barber){
        MessageContainer<MesagerComplements<?> ,String> barberMessageContainer = new MessageContainer<>();
        try{
            test.TestConectionData();
        }catch (ConnectionDestroyed e){
            barberMessageContainer.addMesage(
                    barberMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Fatal Error"));
            barberMessageContainer.addResponse("Error");
            return barberMessageContainer;
        }

        List<ResponseMessages> list = execute.ListResponseMessages();

        String[] logMessages = execute.ArrayLogMessages(4);
        try{
            if(!(managerBarber.isInstance( barber.getClass() ))){
                Validation.ClearObject(barber);
                barberMessageContainer.addMesage(
                        barberMessageContainer.newAresponseComplements(
                                ResponseMessages.WARNING, "Barber not found" ));
                return barberMessageContainer;
            }

            ResponseMessages operationResult = managerBarber.postOnRepository(barber);

            if(operationResult.equals(ResponseMessages.SUCCESS)){
                barber.DeafullScore();
                list.add( operationResult );
                logMessages[0] = execute.FormatLog( barber.getId(), ResponseMessages.SUCCESS, barber.getClass() );
            }else if(operationResult.equals( ResponseMessages.ERROR )) {
                list.add( operationResult );
                logMessages[0] = execute.FormatLog( barber.getId(), ResponseMessages.ERROR , barber.getClass() );
            }

        }catch (NullPointerException e) {
            list.add( ResponseMessages.ERROR );
            logMessages[0] = "ERRO NO REPOSITORIO AS:" + Timestamp.from( Instant.now() );
        }

        if(list.contains(ResponseMessages.ERROR)){
            for (int INDEX = 0; INDEX < list.size(); INDEX++) {
                if(list.get( INDEX ) == ResponseMessages.ERROR ) {
                    barberMessageContainer.addResponse("Error");
                    barberMessageContainer.addMesage(
                            barberMessageContainer.newAresponseComplements(
                                    ResponseMessages.ERROR, logMessages[INDEX]));
                }
            }
        }

        for (int INDEX = 0; INDEX < list.size(); INDEX++) {
            if(list.get( INDEX ) == ResponseMessages.SUCCESS ) {
                barberMessageContainer.addResponse("Success");
                barberMessageContainer.addMesage(
                        barberMessageContainer.newAresponseComplements(
                        ResponseMessages.SUCCESS, logMessages[INDEX]));
            }
        }
        return barberMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }

}
