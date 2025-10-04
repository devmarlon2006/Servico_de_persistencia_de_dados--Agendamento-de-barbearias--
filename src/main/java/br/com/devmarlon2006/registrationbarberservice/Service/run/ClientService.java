package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MesagerComplements;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.repositorymanager.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import br.com.devmarlon2006.registrationbarberservice.Service.verificationservices.Validation;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class ClientService {

    private final ClientRepositoryManagerService managerClient;
    private final TestConectivity test;
    private final Execute execute;

    public ClientService(ClientRepositoryManagerService managerClient, TestConectivity test, Execute execute) {
        this.managerClient = managerClient;
        this.test = test;
        this.execute = execute;
    }

    /**
     * Processa a tentativa de persistência de um Client no repositório, construindo mensagens de retorno
     * que descrevem o resultado da operação.
     *
     * Fluxo:
     * 1) Validação de instância: se o objeto informado NÃO for considerado uma instância válida
     *    por {@code managerClient.IsInstace(client)}, o método sinaliza "Invalid Object" no retorno e encerra.
     * 2) Persistência: se {@code client != null}, tenta salvar via {@code managerClient.PostOnRepository(client)}:
     *    - SUCCESS: registra sucesso;
     *    - Diferente de SUCCESS: registra "Data Error" como erro;
     *    - NullPointerException durante a operação: registra "Repository Error" como erro.
     * 4) Agregação e retorno:
     *    - Itera sobre os resultados coletados; ao encontrar SUCCESS, adiciona "Success" ao container e retorna imediatamente.
     *    - Para WARNING/ERROR, adiciona as mensagens correlacionadas pré-preenchidas no array {@code Sa}.
     *
     * Observações:
     * - O container de mensagens retornado pode conter múltiplas entradas (avisos/erros), dependendo do fluxo.
     * - A limpeza via {@code Validation.ClearObject(client)} não altera a referência recebida pelo chamador.
     *
     * @param client objeto Client a ser validado e persistido
     * @return MessageContainer contendo mensagens de sucesso, aviso e/ou erro sobre a operação executada
     */
    public MessageContainer<MesagerComplements<?>, String> ProcessClientRegistration(@NonNull Client client) {
        MessageContainer<MesagerComplements<?>, String> clientMessageContainer = new MessageContainer<>();

        try{
            test.TestConectionData();
        }catch (ConnectionDestroyed e){
            clientMessageContainer.addMesage(
                    clientMessageContainer.newAresponseComplements(
                            ResponseMessages.ERROR, "Fatal Error"));
            clientMessageContainer.addResponse("Error");
            return clientMessageContainer;
        }

        List<ResponseMessages> list = execute.ListResponseMessages();

        String[] logMessages = execute.ArrayLogMessages(4);

        try{
            if(!(managerClient.isInstance( client.getClass() ))){
                Validation.ClearObject(client);
                clientMessageContainer.addMesage(
                        clientMessageContainer.newAresponseComplements(
                                ResponseMessages.WARNING, "Invalid Object"));
                return clientMessageContainer;
            }

            if(managerClient.postOnRepository(client).equals(ResponseMessages.SUCCESS)){
                list.add( ResponseMessages.SUCCESS );
                logMessages[0] = execute.FormatLog( client.getId(), ResponseMessages.SUCCESS, client.getClass());
            }else {
                list.add( ResponseMessages.ERROR );
                logMessages[0] = execute.FormatLog( client.getId(), ResponseMessages.ERROR, client.getClass() );
            }
        }catch (NullPointerException e) {
            list.add( ResponseMessages.ERROR );
            logMessages[0] = "ERRO NO REPOSITORIO AS:" + Timestamp.from( Instant.now() );
        }

        if(list.contains(ResponseMessages.ERROR)){
            for (int INDEX = 0; INDEX < list.size(); INDEX++) {
                if(list.get( INDEX ) == ResponseMessages.ERROR ) {
                    clientMessageContainer.addResponse("Error");
                    clientMessageContainer.addMesage(
                            clientMessageContainer.newAresponseComplements(
                                    ResponseMessages.ERROR, logMessages[INDEX]));
                }
            }
            return clientMessageContainer;
        }

        for (int INDEX = 0; INDEX < list.size(); INDEX++) {
            if(list.get( INDEX ) == ResponseMessages.SUCCESS ) {

                clientMessageContainer.addResponse("Success");
                clientMessageContainer.addMesage(
                        clientMessageContainer.newAresponseComplements(
                                ResponseMessages.SUCCESS, logMessages[INDEX]));

            } else if(list.get( INDEX ) == ResponseMessages.WARNING ) {
                clientMessageContainer.addMesage(
                        clientMessageContainer.newAresponseComplements(
                                ResponseMessages.WARNING, logMessages[INDEX]));
            }
        }
        return clientMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }
}
