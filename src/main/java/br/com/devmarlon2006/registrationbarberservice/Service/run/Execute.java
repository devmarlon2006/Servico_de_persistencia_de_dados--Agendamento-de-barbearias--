/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.run;

import br.com.devmarlon2006.registrationbarberservice.Service.manager.BarberRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.TestConectivity;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.MessageContainer;
import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseMessages;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.ClientRepositoryManagerService;
import br.com.devmarlon2006.registrationbarberservice.Service.manager.Validation;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Barber;
import br.com.devmarlon2006.registrationbarberservice.Service.model.Client;
import br.com.devmarlon2006.registrationbarberservice.Service.myexeptions.ConnectionDestroyed;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
public class Execute {

    private final ClientRepositoryManagerService managerClient;
    private final BarberRepositoryManagerService managerBarber;
    private final TestConectivity test;
    public Execute(ClientRepositoryManagerService managerClient, BarberRepositoryManagerService managerBarber, TestConectivity test) {
        this.managerClient = managerClient;
        this.managerBarber = managerBarber;
        this.test = test;
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
    public MessageContainer<?> ProcessClientRegistration(@NonNull Client client) {
        MessageContainer<String> clientMessageContainer = Container();

        try{
            test.TestConectionData();
        }catch (ConnectionDestroyed e){
            clientMessageContainer.addMesage("Fatal Error");
            clientMessageContainer.addResponse("Error");
            return clientMessageContainer;
        }

        List<ResponseMessages> list = ListResponseMessages();

        String[] logMessages = ArrayLogMessages();

        try{
            if(!(managerClient.IsInstance( client.getClass() ))){
                Validation.ClearObject(client);
                clientMessageContainer.addMesage("Invalid Object");
                return clientMessageContainer;
            }

            if(managerClient.PostOnRepository(client).equals(ResponseMessages.SUCCESS)){
                list.add( ResponseMessages.SUCCESS );
                logMessages[0] = FormatLog( client.getId(), ResponseMessages.SUCCESS, client.getClass());
            }else {
                list.add( ResponseMessages.ERROR );
                logMessages[0] = FormatLog( client.getId(), ResponseMessages.ERROR, client.getClass() );
            }
        }catch (NullPointerException e) {
            list.add( ResponseMessages.ERROR );
            logMessages[0] = "ERRO NO REPOSITORIO AS:" + Timestamp.from( Instant.now() );
        }

        if(list.contains(ResponseMessages.ERROR)){
            for (int INDEX = 0; INDEX < list.size(); INDEX++) {
                if(list.get( INDEX ) == ResponseMessages.ERROR ) {
                    clientMessageContainer.addResponse("Error");
                    clientMessageContainer.addMesage(logMessages[INDEX]);
                }
            }
            return clientMessageContainer;
        }

        for (int INDEX = 0; INDEX < list.size(); INDEX++) {
            if(list.get( INDEX ) == ResponseMessages.SUCCESS ) {

                clientMessageContainer.addResponse("Success");
                clientMessageContainer.addMesage(logMessages[INDEX]);

            } else if(list.get( INDEX ) == ResponseMessages.WARNING ) {
                clientMessageContainer.addMesage(logMessages[INDEX]);
            }
        }
        return clientMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }

    /**
     * Este metodo faz a mesma função que o @method {@link #ProcessClientRegistration(Client)}
     * com a diferença que recebe um objeto @class Barber, comalgumas modificações no fluxo.
     * Facilitando o encapsulamento do registro das entidades.
     * @param barber
     * @return MessageContainer
     */
    public MessageContainer<?> ProcessBarberRegistration(Barber barber){
        MessageContainer<String> barberMessageContainer = Container();
        try{
            test.TestConectionData();
        }catch (ConnectionDestroyed e){
            barberMessageContainer.addMesage("Fatal Error");
            barberMessageContainer.addResponse("Error");
            return barberMessageContainer;
        }

        List<ResponseMessages> list = ListResponseMessages();

        String[] logMessages = ArrayLogMessages();
        try{
            if(!(managerBarber.IsInstance( barber.getClass() ))){
                Validation.ClearObject(barber);
                barberMessageContainer.addMesage("Invalid Object");
                return barberMessageContainer;
            }

            ResponseMessages operationResult = managerBarber.PostOnRepository(barber);

            if(operationResult.equals(ResponseMessages.SUCCESS)){
                barber.DeafullScore();
                list.add( operationResult );
                logMessages[0] = FormatLog( barber.getId(), ResponseMessages.SUCCESS , barber.getClass() );
            }else if(operationResult.equals( ResponseMessages.ERROR )) {
                list.add( operationResult );
                logMessages[0] = FormatLog( barber.getId(), ResponseMessages.ERROR , barber.getClass() );
            }

        }catch (NullPointerException e) {
            list.add( ResponseMessages.ERROR );
            logMessages[0] = "ERRO NO REPOSITORIO AS:" + Timestamp.from( Instant.now() );
        }

        if(list.contains(ResponseMessages.ERROR)){
            for (int INDEX = 0; INDEX < list.size(); INDEX++) {
                if(list.get( INDEX ) == ResponseMessages.ERROR ) {
                    barberMessageContainer.addResponse("Error");
                    barberMessageContainer.addMesage(logMessages[INDEX]);
                }
            }
        }

        for (int INDEX = 0; INDEX < list.size(); INDEX++) {
            if(list.get( INDEX ) == ResponseMessages.SUCCESS ) {
                barberMessageContainer.addResponse("Success");
                barberMessageContainer.addMesage(logMessages[INDEX]);
            }
        }
        return barberMessageContainer; //Log de operações!! não recomendado para voltar para o usuario, apenas para testes e registro de Log.
    }

    private String FormatLog(String identifier, ResponseMessages status, Class<?> clazz){
        if(clazz.isAssignableFrom( Barber.class ))
        {
            return "SALVAR BARBER DE ID:" + identifier + " * STATUS:" + status;
        }
        return "SALVAR CLIENTE DE ID:" + identifier + " * STATUS:" + status;
    }

    private MessageContainer<String> Container(){
        return new MessageContainer<>();
    }

    private List<ResponseMessages> ListResponseMessages(){
        return new ArrayList<>();
    }

    private String[] ArrayLogMessages(){
        return new String[4];
    }
}
