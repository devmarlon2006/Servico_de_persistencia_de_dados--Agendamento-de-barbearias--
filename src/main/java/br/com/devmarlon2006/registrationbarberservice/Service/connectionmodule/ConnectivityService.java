/*
 * Copyright (c) 2024 devmarlon2006
 * Autor: devmarlon2006
 *
 * Este código-fonte é de minha autoria e propriedade.
 * Por favor, respeite meu trabalho. Se precisar fazer alterações,
 * entre em contato para discutirmos e obter meu consentimento.
 * Agradeço sua compreensão.
 */

package br.com.devmarlon2006.registrationbarberservice.Service.connectionmodule;

import br.com.devmarlon2006.registrationbarberservice.Service.apimessage.ResponseStatus;
import br.com.devmarlon2006.registrationbarberservice.Service.systemexeptions.ConnectionDestroyed;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLException;
import java.sql.Timestamp;
import javax.sql.DataSource;

@Service
public class ConnectivityService {

    private static final String EXPECTED_OK = "OK" ;
    private final DataSource dataSource;

    /**
     * Payload simples para envio de log de tentativa de conexão a um endpoint externo.
     * Contém um timestamp da tentativa e uma mensagem estática de teste.
     */
    static class ConectionLog{
        public Timestamp timestamp;
        public final String message = "Teste";
    }

    private final RestTemplate restTemplate;

    /**
     * Construtor do serviço de teste de conectividade.
     *
     * Responsável por injetar a fonte de dados para teste de conexão com o banco
     * e instanciar o {@link RestTemplate} usado para testes de conexão com serviços HTTP externos.
     *
     * @param dataSource fonte de dados configurada para o aplicativo
     */
    public ConnectivityService(DataSource dataSource) {
        this.restTemplate = new RestTemplate();
        this.dataSource = dataSource;
    }

    /**
     * Retorna o estado de sucesso para padronização de respostas do serviço.
     *
     * @return {@link ResponseStatus#SUCCESS}
     */
    public ResponseStatus retrieveSuccess(){
        return ResponseStatus.SUCCESS;
    }

    /**
     * Retorna o estado de aviso para padronização de respostas do serviço.
     *
     * @return {@link ResponseStatus#WARNING}
     */
    public ResponseStatus retrieveWarning(){
        return ResponseStatus.WARNING;
    }

    /**
     * Retorna o estado de erro para padronização de respostas do serviço.
     *
     * @return {@link ResponseStatus#ERROR}
     */
    public ResponseStatus retrieveError(){
        return ResponseStatus.ERROR;
    }

    /**
     * Retorna o estado informativo para padronização de respostas do serviço.
     *
     * @return {@link ResponseStatus#INFO}
     */
    public ResponseStatus retrieveInfo(){
        return ResponseStatus.INFO;
    }

    public ResponseStatus TestConection(String url){
        ConectionLog log = new ConectionLog();
        log.timestamp = new Timestamp(System.currentTimeMillis());
        try{
            final String response = restTemplate.postForObject(url, log, String.class);
            return EXPECTED_OK.equals(response) ? retrieveSuccess() : retrieveWarning();
        }catch (RestClientException e) {
            return retrieveWarning();
        }
    }



    public ResponseStatus TestConectionData() throws ConnectionDestroyed {
         try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new ConnectionDestroyed( e.getMessage() );
        }
        return retrieveSuccess();
    }
}
